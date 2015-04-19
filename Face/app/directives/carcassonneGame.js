var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneGame', ['$socket', function($socket) {
  return {
    restrict: 'A',
    controller: function($scope) {
      $scope.andrei = 'andrei';
    }
  }
}]);
