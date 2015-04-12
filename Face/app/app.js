'use strict';

var carcassonne = angular.module('carcassonne',[
  'ngRoute',
  'btford.socket-io',
  'ngMaterial'
]).config(function($routeProvider, $locationProvider) {
  $routeProvider
    .when('/', {
      templateUrl: '/index',
      controller: 'MainCtrl'
    })
    .otherwise({
      // Should redirect to an error page
      redirectTo: '/'
    });

  $locationProvider.html5Mode(true);
});
