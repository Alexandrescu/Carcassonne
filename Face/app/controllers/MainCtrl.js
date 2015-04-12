var carcassonne = angular.module('carcassonne');

carcassonne.controller('MainCtrl', ['$scope', '$socket', function($scope, $socket) {
  var socket = $socket.io('http://localhost:1337');

  $scope.$on('$destroy', function() {
    socket.removeAllListeners();
  });

  $scope.socketStyle = {
    "background-color": "red"
  };

  socket.on('connect', function () {
    // Stop the progress circle
    $scope.progressFinish = true;

    socket.on('availableRooms', function (data) {
      $scope.availableRooms = data;
    });

    $scope.addRoom = function () {
      socket.emit('addRoom', {roomName: $scope.roomName});
    };
  });

  socket.on('disconnect', function() {
    $scope.progressFinish = false;
  });

  $scope.createRoom = function(roomName) {
    if(roomName && roomName != '') {
      console.log(roomName);
      $scope.creatingRoom = true;
    }
  };
}]);
