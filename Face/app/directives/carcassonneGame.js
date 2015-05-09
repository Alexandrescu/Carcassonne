var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneGame', ['$socket', '$location', '$routeParams', '$timeout', function($socket, $location, $routeParams, $timeout) {
  return {
    restrict: 'A',
    controller: function($scope) {

      $scope.gameEnded = false;
      var colorMap = ['red', 'blue', 'green', 'yellow', 'purple', 'grey'];
      this.color = function(slot) {
        if(slot == -2 && $routeParams.slot) {
          return colorMap[$routeParams.slot];
        }
        if(slot >= 0 && slot < 6) {
          return colorMap[slot];
        }
        return 'black';
      };

      $scope.color = this.color;
      var host = $location.host();
      var socket = $socket.io('http://' + host + ':1337/' + $routeParams.gameName);
      //var socket = $socket.io('http://' + host + ':1337/');

      if($routeParams.slot) {
        $scope.mySlot = $routeParams.slot;
        socket.emit('connectAs', {
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

      $scope.slots = [];
      socket.on('gameMove', function(move) {
        console.log('[gameMove]', move);

        // Should lock the array... might produce errors. Check this.
        moveQueue.push(move);
        if(!moveProcessing) {
          moveProcessing = true;
          $scope.newMove = move;
        }
      });

      socket.on('gameNext', function(nextMove) {
        console.log("[Possible moves] ", nextMove);
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

      socket.on('gameSlots', function(list) {
        // This is quite a hack... should be ack the messages and then run this.
        $timeout(function() {
          console.log('[gameSlots]', list);
          $scope.slots = list.slots;
        }, 0);
      });

      // Unimplemented
      socket.on('connect', function(){

      });

      socket.on('gameValid', function(valid) {
        console.log("[Valid]", valid);
      });

      socket.on('gameError', function(error) {
        console.log("[Error]", error);
      });

      socket.on('gameEnd', function(slots) {
        console.log("[The End]", slots);
        $scope.gameEnded = true;
        $scope.gameSummary = slots.slots;
      });

      socket.on("followerRemoved", function(removed) {
        $timeout(function() {
          console.log("[Remove Move]", removed);
          $scope.removed = removed;
        }, 0);
      });

      socket.on('gameDraw', function(draw) {
        // Update everything accordingly
        $scope.currentSlot = draw.slot;
        $scope.currentTile = angular.lowercase(draw.tile) + "Tile.png"
      });

      $scope.testRemove = function() {
        $scope.removed =  {x: 1, y: 0, section: 2};
      };

      $scope.test = function() {
        $scope.playing = true;
        $scope.nextMove = {
          tile: "X",
          moveList: [
            {
              moves : {
                //'Up' : [1, 2, 3, 4, -1]
                'Up' : [1, 2, 3, 4, 5, 6, 7, 8, -1]
              },
              x : 0,
              y : 0
            }
          ]
        };
      };

      //$scope.test();
    }
  }
}]);
