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
      playMove: '&'
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

      var tileLetter = angular.lowercase(scope.tile);
      var tileSize = scope.tileSize;
      var tileVisible = false;
      var tilePlaced = false;
      var tileDirection = UP;
      var tileMoves = scope.moves;
      var tileRotation = 0;

      var directionMap = ['UP', 'LEFT', 'DOWN', 'RIGHT'];

      console.log(tileMoves);

      var containerSVG = $d3.select(element[0]).append('g')
        .attr('width', tileSize)
        .attr('height', tileSize)
        .attr('class', 'tileContainer');

      var tile = containerSVG
        .append('image')
          .attr('width', tileSize)
          .attr('height', tileSize)
          .attr('xlink:href', '/tiles/' + tileLetter + 'Tile.png')
          .attr('opacity', tileVisible? '1' : '0');

      function removeUnavailable(tileRegions) {
        var direction = directionMap[tileDirection];
        var availableRegions = tileMoves[direction].sections;
        console.log("the regions are:");
        console.log(availableRegions);
        console.log("the entries are:");

        var result = [];
        tileRegions.forEach(function(entry) {
          console.log(entry);
          for(var i = 0; i < availableRegions.length; i++) {
            console.log("comparing " +entry.section + " " + availableRegions[i]);
            if(entry.section == availableRegions[i]) {
              console.log('true');
              result.push(entry);
            }
          }

          console.log('false');
          return false;
        });
        console.log('result');
        console.log(tileRegions);
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
          console.log(tileRegions);

          var availableRegions = removeUnavailable(tileRegions);

          containerSVG.selectAll('rect').data(availableRegions).enter()
            .append('rect')
            .attr('width', tileSize / 10)
            .attr('height', tileSize / 10)
            .attr('x', function(d){
              console.log(d.x * tileSize);
              return d.x * tileSize;
            })
            .attr('y', function(d) {
              return d.y * tileSize;
            })
            .attr('opacity', '1')
            .attr('class', 'follower-place')
            .on('mousedown', function(d) {
              // Marking tile not to be removed
              $d3.select(containerSVG.node().parentNode)
                .classed({
                  'carcassonne-final' : true,
                  'carcassonne' : false
                });

              // Marking follower not to be removed
              $d3.select(this)
                .attr('class', 'follower')
                .attr('fill', scope.color)
                .on('mouseleave', null)
                .on('mouseenter', null)
                .on('mousedown', null);

              // Removing event listeners
              element.off('mouseenter');
              element.off('mousedown');
              element.off('mouseleave');

              // Playing move
              scope.playMove({
                section: d
              });
            })
            .on('mouseenter', function() {
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
        console.log(tileDirection);
        console.log(tileMoves);

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
    }
  }
}]);
