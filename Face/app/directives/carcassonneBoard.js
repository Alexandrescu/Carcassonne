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
      tileSize: '=',
      followerRemoved: '='
    },
    controller : function($scope) {
      this.freezed = false;
      this.placeTile = function(partialMove) {
        this.freezed = true;
      };
      this.unPlaceTile = function() {
        this.freezed = false;
      };

      // These are going to be hooked in link.
      $scope.playMove = function(data) {
        console.log("link not ready");
      };

      // API function
      this.playMove = function(data){
        $scope.playMove(data);
        $d3.select('.cs-board').selectAll('.follower-place').remove();
        $d3.selectAll('.tile').selectAll('.carcassonne').remove();
        this.freezed = false;
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

      var initialTranslationX = offset * tileSize * zoomCurrent + width / 2 - 1.5 * tileSize;
      var initialTranslationY = offset * tileSize * zoomCurrent + height / 2 - 1.5 * tileSize;

      // This is the d3 Zoom Behaviour
      // When the event zoom is called then zoomed is triggered
      var zoomBehaviour =
        $d3.behavior.zoom()
          .scaleExtent([zoomLowerBound, zoomUpperBound])
          .scale(zoomCurrent)
          .translate([initialTranslationX, initialTranslationY])
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
        .attr('transform', "translate(" + (initialTranslationX) + ", " + (initialTranslationY) + ")scale(" + zoomCurrent + ")");

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

      var tilePlaces = {};
      tilePlaces.remove  = function(){};

      scope.tileMoves = {};
      function appendPossibleTiles(newMoves) {
        newMoves.moveList.forEach(function(entry, index) {
          entry.y = -entry.y;
          scope.tileMoves[index] = entry;

          tile.each(function(d, i, j){
            var flagIndex = -1;
            if((entry.x - offset) == i && (entry.y - offset) == j) {
              flagIndex = index;
            }

            if(flagIndex >= 0) {
              // Tile to append to.
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
      }

      function finalTile(move) {
        move.y = -move.y;
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
            $compile(section.node())(scope);
          });

        gameCtrl.moveAdded();
      }

      //removeFinishedFollower({x:1, y:0, section:1});
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

      scope.$watch('myMove', function(after, before) {
        if(after) {
          appendPossibleTiles(after);
        }
      });

      scope.$watch("followerRemove", function(after, before) {
        if(after) {
          removeFinishedFollower(after);
        }
      });

      gameCtrl.loaded();
    },
    template: '<div></div>'
  }
}]);
