var carcassonne = angular.module('carcassonne');

carcassonne.controller('MainCtrl', ['$scope', '$socket', function($scope, $socket) {
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

  socket.on('connect', function () {
    // Stop the progress circle
    $scope.progressFinish = true;

  });

  socket.on('disconnect', function() {
    $scope.progressFinish = false;
  });

  //Joining room
  socket.on('roomConnected', function(data) {
    $scope.myRoom = data.roomName;
    $scope.roomConnected = true;
    $scope.creatingRoom = false;
  });

  // Join Room
  $scope.joinRoom = function(roomName) {
    socket.emit('joinRoom', {roomName: roomName});
  };

  // Create room and join
  $scope.createRoom = function(roomName) {
    if(roomName && roomName != '') {
      $scope.myRoom = roomName;
      $scope.creatingRoom = true;
      socket.emit('addRoom', {roomName: roomName});
    }
  };

  $scope.$on('$destroy', function() {
    socket.removeAllListeners();
  });
}]);
