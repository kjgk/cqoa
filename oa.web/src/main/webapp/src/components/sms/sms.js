'use strict';

angular.module('app.sms', ['base'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('sms', {
                url: '/sms',
                templateUrl: 'components/sms/sms.html',
                controller: 'SmsCtrl'
            });

//        $urlRouterProvider.where('/sms', '/sms');
    })

    .controller('SmsCtrl', function () {
    })
;
