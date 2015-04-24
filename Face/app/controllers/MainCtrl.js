var carcassonne = angular.module('carcassonne');

carcassonne.controller('MainCtrl', ['$scope', '$socket', '$location', '$rootScope', function($scope, $socket, $location, $rootScope) {
  $scope.myRoom = {
    roomName : '',
    joined : false
  };

  var host = $location.host();
  var socket = $socket.io('http://' + host + ':1337');

  socket.emit('getGame');

  socket.on('availableGames', function(data) {
    console.log("GAMES");
    console.log(data);
    $scope.availableGames = [];
    data.rooms.forEach(function(entry) {
      console.log("Testing this entry");
      console.log(entry);
      if(entry.roomName != '') {
        $scope.availableGames.push(entry);
      }
    })
  });

  socket.on('availableRooms', function (data) {
    console.log(data);
    console.log($scope.myRoom);
    console.log($scope.creatingRoom);

    $scope.availableRooms = data.rooms;
    // If I am creating room, and my Room has been created then I can join it here.
    // See issue in which I am telling future improvements for this.
    if($scope.creatingRoom) {
      for(var i = 0; i < data.rooms.length; i++) {
        if($scope.myRoom == data.rooms[i].roomName){
          $scope.joinRoom($scope.myRoom)
        }
      }
    }
  });

  // Stop the progress circle
  socket.on('connect', function () {
    $scope.progressFinish = true;
  });

  // Start the progress circle
  socket.on('disconnect', function() {
    $scope.progressFinish = false;
    $scope.myRoom = {
      joined : false,
      roomName: ''
    }
  });

  //Joining room
  socket.on('roomConnected', function(data) {
    console.log("ROOM CONNECTED");
    console.log(data);
    $scope.myRoom = {
      roomName :data.roomName,
      joined: true
    };
    if(data.slots) {
      $scope.slots = data.slots.sort(function(a, b) { return a.slot - b.slot});
    }
    $scope.creatingRoom = false;
  });

  socket.on('roomUpdate', function(room) {
    $scope.slots = room.slots.sort(function(a, b) { return a.slot - b.slot});
  });

  socket.on('roomLeft', function(room) {
    $scope.myRoom = {
      joined : false,
      roomName: ''
    }
  });

  $scope.leaveRoom = function() {
    socket.emit('leaveRoom', {roomName: $scope.myRoom.roomName});
  };

  // Join Room request
  $scope.joinRoom = function(roomName) {
    socket.emit('joinRoom', {roomName: roomName});
  };

  // Create room and join
  $scope.createRoom = function(roomName) {
    if(roomName && roomName != '') {
      $scope.creatingRoom = true;
      socket.emit('addRoom', {roomName: roomName});
    }
  };
  $scope.id = {};
  $scope.chooseNickname = function() {
    console.log($scope.id.disabled);
    if($scope.id.disabled) {
      console.log("HERE");
      $scope.id.disabled = false;
      if($scope.myRoom.slot > 0) {
        $scope.removeSlot($scope.myRoom.slot);
      }
    }
    else if($scope.id.nickname && $scope.id.nickname != '') {
      $scope.id.disabled = true;
    }
  };

  $scope.joinSlot = function(index) {
    $scope.myRoom.slot = index;
    var data = {
      roomName: $scope.myRoom.roomName,
      playerName: $scope.id.nickname,
      slot: index,
      isAI: false
    };
    console.log("addPlayer event");
    console.log(data);
    socket.emit('addPlayer', data);
  };

  $scope.removeSlot = function(index) {
    var data = {
      roomName: $scope.myRoom.roomName,
      playerName: $scope.id.nickname,
      slot: index,
      isAI: false
    };
    if(index === $scope.myRoom.slot) {
      delete $scope.myRoom.slot;
    }
    socket.emit('removePlayer', data);
  };

  $scope.aiSlot = function(index) {
    var data = {
      roomName: $scope.myRoom.roomName,
      playerName: aiNames[index],
      slot: index,
      isAI: true
    };
    console.log("addAI event");
    console.log(data);
    socket.emit('addPlayer', data);
  };

  $scope.startGame = function() {
    $rootScope.game = {
      slot : $scope.myRoom.slot,
      token : 'token' + $scope.myRoom.slot
    };
    console.log($rootScope.game);
    socket.emit('startGame', {roomName : $scope.myRoom.roomName});
    $location.path('game/' + $scope.myRoom.roomName + "/" + $scope.myRoom.slot);
  };

  $scope.joinGame = function(room) {
    $location.path('game/' + room);
  };

  $scope.stopGame = function(room) {
    socket.emit('stopGame', {roomName: room})
  };

  $scope.$on('$destroy', function() {
    socket.removeAllListeners();
  });

  // Slot section
  var slotNumber = 6;
  var aiNames = [
    'Anca',
    'Bianca',
    'Ciobi',
    'Dan',
    'Emil',
    'Flavius'
  ];
  $scope.slots = new Array(slotNumber);
}]);
