var carcassonne = angular.module('carcassonne');

carcassonne.factory('TileRegions', ['$resource', function($resource) {
  return $resource('/tile/:id', null, {
    'get' : {
      method: 'GET',
      isArray : true
    }
  });
}]);
