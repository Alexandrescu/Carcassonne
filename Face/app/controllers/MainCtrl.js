var carcassonne = angular.module('carcassonne');

carcassonne.controller('MainCtrl', ['$scope', '$socket', function($scope, $socket) {
  $scope.myRoom = {
    roomName : '',
    joined : false
  };
  var socket = $socket.io('http://localhost:1337');

  socket.on('availableRooms', function (data) {
    console.log(data);
    console.log($scope.myRoom);
    console.log($scope.creatingRoom);

    $scope.availableRooms = data.rooms;
    if($scope.creatingRoom) {
      for(var i = 0; i < data.rooms.length; i++) {
        if($scope.myRoom == data.rooms[i].roomName){
          joinRoom($scope.myRoom)
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
    $scope.myRoom.joined = false;
    $scope.progressFinish = false;
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
      //UPDATE ROOM
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

  $scope.$on('$destroy', function() {
    socket.removeAllListeners();
  });

  // Slot section
  var slotNumber = 6;
  $scope.slots = new Array(slotNumber);
}]);
