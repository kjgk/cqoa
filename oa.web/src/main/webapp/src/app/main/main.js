'use strict';

angular.module('app', ['app.sms'])

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, cfpLoadingBarProvider, RestangularProvider) {
        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'app/main/main.html',
                controller: 'MainCtrl'
            });

        $urlRouterProvider.otherwise('/');

        cfpLoadingBarProvider.includeSpinner = false;

        RestangularProvider.setBaseUrl(PageContext.path);

        RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
            return data.data;
        });

        $httpProvider.interceptors.push(function ($q, $location, $filter, cfpLoadingBar, toaster) {
            return {
                'request': function (request) {
                    if (request.cfpLoading === undefined || request.cfpLoading) {
                        cfpLoadingBar.start();
                    }
                    return request || $q.when(request);
                },
                'response': function (response) {
                    cfpLoadingBar.complete();
                    return response || $q.when(response);
                },

                'responseError': function (rejection) {
                    cfpLoadingBar.complete();
                    toaster.pop('error', "错误", rejection);
                    return $q.reject(rejection);
                }
            };
        });

    })

    .controller('MainCtrl', function () {
    })
;
