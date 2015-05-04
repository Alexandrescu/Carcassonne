var carcassonne = angular.module('carcassonne');

carcassonne.factory('$socket', ['socketFactory', function(socketFactory) {
  var socket = {};
  socket.io = function(path) {
    var myIoSocket = io.connect(path);

    var mySocket = socketFactory({
      ioSocket: myIoSocket
    });

    return mySocket;
  };

  return socket;
}]);
