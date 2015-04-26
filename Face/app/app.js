'use strict';

var carcassonne = angular.module('carcassonne',[
  'ngRoute',
  'btford.socket-io',
  'ngMaterial',
  'ngResource'
]).config(function($routeProvider, $locationProvider) {
  $routeProvider
    .when('/', {
      templateUrl: '/index',
      controller: 'MainCtrl'
    })
    .when('/game/:gameName/:slot?', {
      templateUrl: '/gamePartial',
      controller: 'GameCtrl'
    })
    .when('/test', {
      templateUrl: '/testTile'
    })
    .otherwise({
      // Should redirect to an error page
      redirectTo: '/'
    });

  $locationProvider.html5Mode(true);
});
