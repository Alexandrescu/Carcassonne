var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneBoard', ['$d3', function($d3) {
  return {
    restrict: 'E',
    scope : {
      height: '=',
      width: '='
    },
    link: function(scope, element, attrs) {
      var zoomLowerBound = 1;
      var zoomCurrent = 2;
      var zoomUpperBound = 4;
      var height = scope.height;
      var width = scope.width;
      var tileSize = 20;
      // This is the d3 Zoom Behaviour
      // When the event zoom is called then zoomed is triggered
      var zoomBehaviour =
        $d3.behavior.zoom()
          .scaleExtent([zoomLowerBound, zoomUpperBound])
          .scale(zoomCurrent)
          .on('zoom', zoomed);

      function zoomed() {
        grid.attr("transform", "translate(" + $d3.event.translate + ")scale(" + $d3.event.scale + ")");
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
        .append('g')
          .attr("transform", "translate(0,0)")
          .call(zoomBehaviour);

      var rect = svgGroup.append('rect')
        .attr('width', width)
        .attr('height', height)
        .style("fill", "none")
        .style('pointer-events', 'all');

      var grid = svgGroup.append('g')
        .attr('transform', "translate(0,0)scale(" + zoomCurrent + ")");

      var row = grid.selectAll('.row')
          .data($d3.range(0, height, tileSize))
        .enter().append('g')
        .attr('class', 'row');

      var tile = row.selectAll('.tile')
          .data($d3.range(0, width, tileSize))
        .enter().append('rect')
        .attr('fill', 'red')
        .attr('x', function(d, i, j) {
          return i * tileSize;
        })
        .attr('y', function(d, i, j){
          return j * tileSize;
        })
        .attr('width', tileSize - 1)
        .attr('height', tileSize - 1)
        .attr('class', 'tile');
    },
    template: '<div></div>'
  }
}]);
