var carcassonne = angular.module('carcassonne');

carcassonne.controller('GameCtrl', ['$scope', '$socket', '$location', function($scope, $socket, $location) {
  $scope.move = 0;
  $scope.changeMove = function() {
    $scope.move += 1;
  }
}]);