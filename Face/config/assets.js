'use stict';

var _ = require('lodash');

exports.client = {
  css: './public/css',
  js: './public/js',
  views: './views/**',
  app: './public/app/'
};

exports.server = {
  sass: './styles/',
  appLocation: './app/',
  config: './config/',
  views: './views/',
  server: 'server.js',
  app: './app/**',
  routes: './routes/**'
};

var npmDeps = ['angular/angular.js', 'angular-route/angular-route.js', 'd3/d3.js',
  'socket.io/node_modules/socket.io-client/socket.io.js', 'angular-socket-io/socket.js',
  'angular-animate/angular-animate.js', 'angular-aria/angular-aria.js',
  'angular-material/angular-material.js', 'angular-messages/angular-messages.js'];

exports.dependencies = _.map(npmDeps, function(s) {return './node_modules/' + s;});
