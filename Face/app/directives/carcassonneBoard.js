var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneBoard', ['$d3', '$compile', function($d3, $compile) {
  return {
    require: '^carcassonneGame',
    restrict: 'E',
    scope : {
      height: '=',
      width: '=',
      move: '=',
      myMove: '=',
      tileSize: '='
    },
    controller : function($scope) {
      this.freezed = false;
      this.placeTile = function() {
        this.freezed = true;
      };
      this.unPlaceTile = function() {
        this.freezed = false;
      };

      $scope.playMove = function(data) {
        console.log("link not ready");
      };

      this.playMove = function(data){
        $scope.playMove(data);
        $d3.select('.cs-board').selectAll('.follower-place').remove();
        $d3.selectAll('.tile').selectAll('.carcassonne').remove();
        this.freezed = false;

        console.log("Move in the board");
        console.log(data);
      };
    },
    link: function(scope, element, attrs, gameCtrl) {
      scope.playMove = gameCtrl.playMove;
      var gridSize = 100;
      var offset = -20;
      var zoomLowerBound = 1;
      var zoomCurrent = 2;
      var zoomUpperBound = 4;

      var height = scope.height;
      var width = scope.width;
      var tileSize = scope.tileSize | 20;

      // This is the d3 Zoom Behaviour
      // When the event zoom is called then zoomed is triggered
      var zoomBehaviour =
        $d3.behavior.zoom()
          .scaleExtent([zoomLowerBound, zoomUpperBound])
          .scale(zoomCurrent)
          .translate([offset * tileSize * zoomCurrent, offset * tileSize * zoomCurrent])
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
        .attr('transform', "translate(" + (offset * tileSize * zoomCurrent) + ", " + (offset * tileSize * zoomCurrent) + ")scale(" + zoomCurrent + ")");

      var row = grid.selectAll('.row')
          .data($d3.range(0, gridSize, 1))
        .enter().append('g')
        .attr('class', 'row');

      var tile = row.selectAll('.tile')
          .data($d3.range(0, gridSize, 1))
        .enter().append('svg')
        .attr('x', function(d, i, j) {
          return i * tileSize;
        })
        .attr('y', function(d, i, j){
          return j * tileSize;
        })
        .attr('width', tileSize)
        .attr('height', tileSize)
        .attr('class', 'tile');

      /* Red rectangles
      tile.append('rect')
        .attr('fill', 'red')
        .attr('width', tileSize - 1)
        .attr('height', tileSize - 1);
      */
      var tilePlaces = {};
      tilePlaces.remove  = function(){};

      var places = {};
      scope.tileMoves = {};
      var boom;
      function appendPossibleTiles(newMoves) {
        console.log("THESE ARE THE MOVES");
        console.log(newMoves.moveList);

        newMoves.moveList.forEach(function(entry, index) {
          scope.tileMoves[index] = entry;

          tile.each(function(d, i, j){
            var flagIndex = -1;
            if((entry.x - offset) == i && (entry.y - offset) == j) {
              flagIndex = index;
            }

            if(flagIndex >= 0) {
              // Tile to append to
              $d3.select(this).append('carcassonne-tile')
                .attr('tile-size', tileSize)
                .attr('tile', '"' + newMoves.tile +'"')
                .attr('class', 'carcassonne')
                .attr('moves', 'tileMoves[' + flagIndex + ']')
                .attr('color', '"' + gameCtrl.color(-2)+ '"')
                .attr('play-move', 'playMove')
                .call(function(section) {
                  $compile(section.node())(scope);
                });
            }
          });
        });

        // Simply test moves


        //places.attr('fill', 'green');
        //carcassonne-tile(tile-size='250', tile='"D"', style='display: -webkit-inline-flex;display:inline-flex')

        if(boom) {
          console.log('removing');
          boom = true;
          places.select('.carcassonne').remove();
        }
      }

      function finalTile(move) {
        console.log("finalTile");
        console.log(gameCtrl.color(move.player.slot));
        console.log(move);
        var finalTiles = tile.filter(function(d, i, j){
          return (move.x - offset) == i && (move.y - offset) == j;
        });
        scope.final = {
          direction: move.direction,
          section: move.tile.sectionOwned
        };

        finalTiles.append('carcassonne-tile')
          .attr('tile-size', tileSize)
          .attr('tile', 'move.tile.type')
          .attr('class', 'carcassonne')
          .attr('color', '"'+ gameCtrl.color(move.player.slot) + '"')
          .attr('final', 'final')
          .call(function(section) {
            console.log(this[0]);
            $compile(section.node())(scope);
          });

        gameCtrl.moveAdded();
      }

      function removeFinishedFollower(follower) {
        var finalTiles = tile.filter(function(d, i, j){
          return (follower.x - offset) == i && (follower.y - offset) == j;
        });

        finalTiles.select('.section' + follower.section).remove();
      }

      scope.$watch('move', function(after, before) {
        if(after) {
          finalTile(after);
        }
      });

      //removeFinishedFollower({x:1, y:0, section:1});
      scope.$watch('myMove', function(after, before) {
        if(after) {
          appendPossibleTiles(after);
        }
      });

      gameCtrl.loaded();
    },
    template: '<div></div>'
  }
}]);
