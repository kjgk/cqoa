'use strict';

angular.module('app.oa', ['base'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('oa', {
                url: '/oa',
                templateUrl: 'app/oa/oa.html',
                controller: 'OaCtrl'
            });

        $urlRouterProvider.when('/oa', '/oa/meetingroom');
    })

    .controller('OaCtrl', function () {

    })
;
