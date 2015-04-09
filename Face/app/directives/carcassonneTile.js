var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneTile', ['$d3', function($d3) {
  return {
    restrict: 'E',
    scope: {
      tileSize: '=',
      tile: '='
    },
    link: function(scope, element, attrs) {
      var tileLetter = angular.lowercase(scope.tile);
      var tileSize = scope.tileSize;
      var tileVisible = false;

      var tile = $d3.select(element[0]).append('svg')
          .attr('width', tileSize)
          .attr('height', tileSize)
        .append('image')
          .attr('width', tileSize)
          .attr('height', tileSize)
          .attr('xlink:href', '/tiles/' + tileLetter + 'Tile.png')
          .attr('visibility', tileVisible? 'visible' : 'hidden');

      var rotation = 0;

      element.bind('mousedown', function(event) {
        console.log(event);
        event.preventDefault();

        rotation = (rotation + 90) % 360;
        tile.attr('transform', 'rotate(' + rotation + ' ' + (tileSize / 2) + ' ' + (tileSize / 2) + ')');
      });

      element.on('contextmenu', function(event) {
        event.preventDefault();
      });

      element.bind('mouseenter mouseleave', function(event) {
        tileVisible = !tileVisible
        tile.attr('visibility', tileVisible? 'visible' : 'hidden');
      });
    }
  }
}]);
