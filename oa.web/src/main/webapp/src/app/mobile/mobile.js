'use strict';

angular.module('app.mobile', ['base'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('mobile', {
                url: '/mobile',
                text: '移动办公平台',
                templateUrl: 'app/mobile/mobile.html',
                controller: 'MobileCtrl'
            });

    })

    .controller('MobileCtrl', function () {

    })
;
