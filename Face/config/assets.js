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
  app: './app/**'
};

var deps = ['angular/angular.js', 'angular-route/angular-route.js'];

exports.dependencies = _.map(deps, function(s) {
  return './node_modules/' + s;
});