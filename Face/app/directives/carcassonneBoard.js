var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneBoard', ['$d3', function($d3) {
  return {
    restrict: 'E',
    scope : {
      height: '=',
      width: '='
    },
    link: function(scope, element, attrs) {
      var height = scope.height;
      var width = scope.width;
      var tileSize = 20;
      // This is the d3 Zoom Behaviour
      // When the event zoom is called then zoomed is triggered
      var zoomBehaviour =
        $d3.behavior.zoom()
          .scale([1, 10])
          .on('zoom', zoomed);

      function zoomed() {

      }

      var board = $d3.select(element[0]);

      //Appending SVG.

      /* MDN
       The g element is a container used to group objects.
       Transformations applied to the g element are performed on all of its child elements.
       Attributes applied are inherited by child elements.
       In addition, it can be used to define complex objects that can later be referenced with the <use> element.
       */

      var svgGroup = board.append('svg')
          .attr('class', 'cs-board')
          .attr('width', width)
          .attr('height', height)
        .append('g');

      var rect = svgGroup.append('rect')
        .attr('width', width)
        .attr('height', height)
        .style("fill", "none")
        .style('pointer-events', 'all');

      var grid = svgGroup.append('g');

      var row = grid.selectAll('.row')
          .data($d3.range(0, height, tileSize))
        .enter().append('g')
        .attr('class', 'row');

      var tile = row.selectAll('.tile')
          .data($d3.range(0, width, tileSize))
        .enter().append('rect')
        .attr('fill', 'red')
        .attr('x', function(d, i, j) {
          return xScale(i);
        })
        .attr('y', function(d, i, j){
          return yScale(i, j);
        })
        .attr('width', tileSize)
        .attr('height', tileSize)
        .attr('class', 'tile');

      function xScale(i) {
        return i * tileSize;
      }

      function yScale(i, j) {
        return j * tileSize;
      }
    },
    template: '<div></div>'
  }
}]);
