var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneGame', ['$socket', '$location', '$routeParams', function($socket, $location, $routeParams) {
  return {
    restrict: 'A',
    controller: function($scope) {
      var colorMap = ['red', 'blue'];
      this.color = function(slot) {
        if(slot == -2 && $routeParams.slot) {
          return colorMap[$routeParams.slot];
        }
        if(slot >= 0) {
          return colorMap[slot];
        }
        return 'black';
      };

      var host = $location.host();
      var socket = $socket.io('http://' + host + ':1337/' + $routeParams.gameName);
      //var socket = $socket.io('http://' + host + ':1337/');

      if($routeParams.slot) {
        socket.emit('playerSessionUpdate', {
          slot: $routeParams.slot,
          token: 'token' + $routeParams.slot
        });
      }

      this.playMove = function(move) {
        socket.emit('playerMove', move);
        $scope.playing = false;
      };

      var moveQueue = [];
      var moveProcessing = true;

      this.loaded = function() {
        if(moveQueue.length > 0) {
          $scope.newMove = moveQueue[0];
        }
        else {
          moveProcessing = false;
        }
      };

      this.moveAdded = function() {
        moveQueue.splice(0, 1);
        if(moveQueue.length > 0) {
          $scope.newMove = moveQueue[0];
        }
        else {
          moveProcessing = false;
        }
      };

      socket.on('gameMove', function(move) {
        // Should lock the array... might produce errors. Check this.
        moveQueue.push(move);
        if(!moveProcessing) {
          moveProcessing = true;
          $scope.newMove = move;
        }
      });

      socket.on('gameNext', function(nextMove) {
        $scope.playing = true;
        $scope.nextMove = nextMove;
      });

      socket.on('gameStart', function(move) {
        // Start the game
        // ADD PROGRESS CIRCLE HERE.
        moveQueue.push(move);
        if(!moveProcessing) {
          moveProcessing = true;
          $scope.newMove = move;
        }
      });

      // Unimplemented
      socket.on('connect', function(){

      });

      socket.on('gameValid', function(valid) {

      });

      socket.on('gameError', function(error) {

      });

      socket.on('gameDraw', function(draw) {
        // Update everything accordingly
        console.log('gameDraw');
        console.log(draw);
        $scope.currentTile = angular.lowercase(draw.tile) + "Tile.png"
      });

      $scope.test = function() {
        $scope.playing = true;
        $scope.nextMove = {
          tile: "D",
          moveList: [
            {
              moves : {
                'Up' : [1, 2, 3, 4, -1]
              },
              x : 0,
              y : 0
            }
          ]
        };
      }
    }
  }
}]);
