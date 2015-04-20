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
    .when('/game/:gameName', {
      templateUrl: '/gamePartial',
      controller: 'GameCtrl'
    })
    .otherwise({
      // Should redirect to an error page
      redirectTo: '/'
    });

  $locationProvider.html5Mode(true);
});
