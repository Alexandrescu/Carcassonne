var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneTile', ['$d3', 'TileRegions', function($d3, TileRegions) {
  return {
    restrict: 'E',
    scope: {
      coordinates: '=',
      tileSize: '=',
      tile: '=',
      moves: '=',
      color: '=',
      playMove: '&',
      final: '='
    },
    templateNamespace: 'svg',
    template: '<svg></svg>',
    replace: true,
    require: '^carcassonneBoard',
    link: function(scope, element, attrs, boardCtrl) {
      /*
        The format of moves should be:
        0 -> up
        2 -> down
        1 -> left
        3 -> right
       */
      var UP = 0;
      var DOWN = 2;

      var tileLetter = angular.lowercase(scope.tile);
      var tileSize = scope.tileSize;
      var tileVisible = false;
      var tilePlaced = false;
      var tileDirection = DOWN;
      var tileRotation = 0;
      var tileMoves = {};
      var tileX = 0;
      var tileY = 0;

      if(scope.moves) {
        tileMoves = scope.moves.moves || {};
        tileX = scope.moves.x;
        tileY = -scope.moves.y;
      }

      var directionMap = ['Up', 'Left', 'Down', 'Right'];

      var containerSVG = $d3.select(element[0]).append('g')
        .attr('width', tileSize)
        .attr('height', tileSize);

      containerSVG.append('rect')
        .attr('width', tileSize)
        .attr('height', tileSize)
        .attr('class', 'tileContainer');

      if(scope.final) {
        angular.element(element).ready(function() {
          boardCtrl.tileDoneRendering({x : final.x, y : final.y});
        });

        var final = scope.final;
        tileMoves[final.direction] = {};

        containerSVG
          .append('image')
          .attr('width', tileSize)
          .attr('height', tileSize)
          .attr('xlink:href', '/tiles/' + tileLetter + 'Tile.png');

        TileRegions.get({id: tileLetter}, function(tileRegions) {
          var regions = [];
          tileRegions.forEach(function(entry) {
            if(entry.section == final.section) {
              regions.push(entry);
            }
          });

          $d3.select(containerSVG.node().parentNode)
            .classed({
              'carcassonne-final' : true,
              'carcassonne' : false
            });

          containerSVG.selectAll('.follower').data(regions).enter()
            .append('rect')
            .attr('width', tileSize / 10)
            .attr('height', tileSize / 10)
            .attr('x', function(d){
              return d.x * tileSize;
            })
            .attr('y', function(d) {
              return d.y * tileSize;
            })
            .attr('class', function(d) {
              return 'follower section' + d.section;
            })
            .attr('fill', scope.color);

          rotateTile();
        });
        return;
      }

      var tile = containerSVG
        .append('image')
          .attr('width', tileSize)
          .attr('height', tileSize)
          .attr('xlink:href', '/tiles/' + tileLetter + 'Tile.png')
          .attr('opacity', tileVisible? '1' : '0');

      function removeUnavailable(tileRegions) {
        var direction = directionMap[tileDirection];
        var availableRegions = tileMoves[direction];

        var result = [];
        tileRegions.forEach(function(entry) {
          for(var i = 0; i < availableRegions.length; i++) {
            if(entry.section == availableRegions[i]) {
              result.push(entry);
            }
          }
        });
        return result;
      }

      /*
        This function is going to mark down all the available followers
        on the tile that has been placed.

        On click on one of the available places for followers the move
        will be broadcast and the tile will become final.
       */
      function markFollowers() {
        TileRegions.get({id: tileLetter}, function(tileRegions) {
          var noFollower = {
            section : -1,
            x: 0,
            y: 0
          };

          var availableRegions = removeUnavailable(tileRegions);
          availableRegions.push(noFollower);

          containerSVG.selectAll('.follower-place').data(availableRegions).enter()
            .append('rect')
            .attr('width', tileSize / 10)
            .attr('height', tileSize / 10)
            .attr('x', function(d){
              return d.x * tileSize;
            })
            .attr('y', function(d) {
              return d.y * tileSize;
            })
            .attr('opacity', '1')
            .attr('class', 'follower-place')
            .on('mousedown', function(d) {
              // Removing event listeners
              element.off('mouseenter');
              element.off('mousedown');
              element.off('mouseleave');

              // Playing move
              var move = {
                x : tileX,
                y : tileY,
                direction : directionMap[tileDirection],
                owned: d.section
              };
              boardCtrl.playMove(move);
            })
            .on('mouseenter', function(d) {
              if(d.section == -1) {
                $d3.select(this).attr('fill', 'white');
                return;
              }
              $d3.select(this).attr('fill', scope.color);
            })
            .on('mouseleave', function() {
              $d3.select(this).attr('fill', 'black')
            });
        });


      }

      // Remove followers from containing svg. Required when cancelling move.
      function removeFollowers() {
        containerSVG.selectAll('.follower-place').remove();
      }

      // Call when placing the tile
      function placeTile() {
        // Mark tile as placed
        tilePlaced = true;
        // Adding followers to board
        markFollowers();
        // Let other tiles know this one is placed
        boardCtrl.placeTile();
      }

      function unPlaceTile() {
        tilePlaced = false;
        removeFollowers();
        boardCtrl.unPlaceTile();
      }

      function nextDirection() {
        for(var it = 1; it <= 4; it++) {
          var newDirection = (tileDirection + it) % 4;
          var direction = directionMap[newDirection];

          if(tileMoves[direction]) {
            tileDirection = newDirection;
            return tileDirection;
          }
        }
      }

      function rotateTile() {
        tileRotation = (nextDirection() * 90) % 360;
        containerSVG.attr('transform',
          'rotate(' + tileRotation + ' ' + (tileSize / 2) + ' ' + (tileSize / 2) + ')');
      }

      function active() {
        return !boardCtrl.freezed;
      }

      element.bind('mousedown', function(event) {
        if(!active()) {
          if(event.which == 3 && tilePlaced) {
            event.preventDefault();
            unPlaceTile();
          }
          return;
        }

        event.preventDefault();

        switch(event.which) {
          case 1:
            placeTile();
            break;
          case 3:
            rotateTile();
            break;
          default:
            alert("Either right click for rotation or left click for placement of tile.");
        }
      });

      element.on('contextmenu', function(event) {
        event.preventDefault();
      });

      element.bind('mouseenter mouseleave', function(event) {
        if(!active()) {
          return;
        }

        tileVisible = !tileVisible;
        tile.attr('opacity', tileVisible? '1' : '0');
      });
      rotateTile();
    }
  }
}]);
