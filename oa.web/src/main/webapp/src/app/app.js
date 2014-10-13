'use strict';

angular.module('app', ['app.oa', 'app.mobile'])

    .value('PageContext', PageContext)

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, cfpLoadingBarProvider, RestangularProvider) {

        $urlRouterProvider.otherwise('/oa/meetingroom');

        cfpLoadingBarProvider.includeSpinner = false;

        RestangularProvider.setBaseUrl(PageContext.path);

        RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
            if (data.success) {
                return data.data;
            } else {
                deferred.reject(data.message);
            }
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
                    if (response.data && response.data.success === false) {
                        toaster.pop('error', "错误", response.data.message);
                    }
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
