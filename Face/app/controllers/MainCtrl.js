var carcassonne = angular.module('carcassonne');

carcassonne.controller('MainCtrl', ['$scope', '$socket', '$location', '$timeout', function($scope, $socket, $location, $timeout) {
  var host = $location.host();
  var socket = $socket.io('http://' + host + ':1337');

  // This receives all the games. i.e. namespaces
  socket.emit('getGame');

  socket.on('availableGames', function(data) {
    $scope.availableGames = [];
    data.rooms.forEach(function(entry) {
      // Removing the default namespace
      if(entry.roomName != '') {
        $scope.availableGames.push(entry);
      }
    })
  });

  socket.on('availableRooms', function (data) {
    $scope.availableRooms = data.rooms;
    // If I am creating room, and my Room has been created then I can join it here.
    // See issue in which I am telling future improvements for this.
    // This happens because creation of a room does not automatically join it.
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
    $scope.myRoom = {
      joined : false,
      roomName: ''
    };
    $scope.progressFinish = false;
  });

  //Joining room
  socket.on('roomConnected', function(data) {
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
      // This flag is used in availableRooms
      $scope.creatingRoom = true;
      socket.emit('addRoom', {roomName: roomName});
    }
  };

  $scope.id = {};
  $scope.chooseNickname = function() {
    if($scope.id.disabled) {
      $scope.id.disabled = false;
      if($scope.myRoom.slot >= 0) {
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
    socket.emit('addPlayer', data);
  };

  $scope.startGame = function() {
    socket.emit('startGame', {roomName : $scope.myRoom.roomName});
    $location.path('game/' + $scope.myRoom.roomName + "/" + $scope.myRoom.slot);
  };

  $scope.joinGame = function(room) {
    $location.path('game' + room);
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

  /* Testing */
  function rooms(name) {
    $scope.createRoom(name);
    $scope.joinRoom(name);
    $scope.myRoom = {
      roomName : name
    };
    $scope.aiSlot(0);
    $scope.aiSlot(1);
    $scope.aiSlot(2);
    $scope.aiSlot(3);
    $scope.aiSlot(4);
    $scope.startGame();

    $scope.myRoom = {
      joined : false,
      roomName: ''
    }
  }
/*
  for(var i = 0; i < 200; i++) {
    rooms("test" + i);
  }
*/
}]);
