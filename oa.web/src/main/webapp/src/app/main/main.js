'use strict';

angular.module('app', ['base', 'meeting'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'app/main/main.html',
                controller: 'MainCtrl'
            });

        $urlRouterProvider.otherwise('/');
    })

    .controller('MainCtrl', function () {
    })
;
