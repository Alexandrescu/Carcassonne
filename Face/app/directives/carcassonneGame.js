var carcassonne = angular.module('carcassonne');

carcassonne.directive('carcassonneGame', ['$socket', '$location', '$routeParams', function($socket, $location, $routeParams) {

  return {
    restrict: 'A',
    controller: function($scope, $rootScope) {
      console.log($routeParams.gameName);
      var host = $location.host();
      //var socket = $socket.io('http://' + host + ':1337/' + $routeParams.gameName);
      var socket = $socket.io('http://' + host + ':1337/');

      if($rootScope.game) {
        socket.emit('playerSessionUpdate', {
          slot: $rootScope.game.slot,
          token: $rootScope.game.token
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

      socket.on('connect', function(){

      });

      socket.on('gameMove', function(move) {
        // Should lock the array... might produce errors. Check this.
        console.log('gameMove');
        console.log(move);

        moveQueue.push(move);
        if(!moveProcessing) {
          moveProcessing = true;
          $scope.newMove = move;
        }
      });

      socket.on('gameNext', function(nextMove) {
        console.log('gameNext');
        console.log(nextMove);

        $scope.playing = true;
        $scope.nextMove = nextMove;
      });

      socket.on('gameDraw', function(draw) {
        // Update everything accordingly
        console.log('gameDraw');
        console.log(draw);
      });

      socket.on('gameStart', function(move) {
        // Start the game
        console.log("gameStart");
        console.log(move);

        moveQueue.push(move);
        if(!moveProcessing) {
          moveProcessing = true;
          $scope.newMove = move;
        }
      });

      socket.on('gameValid', function(valid) {

      });

      socket.on('gameError', function(error) {

      });
    }
  }
}]);
