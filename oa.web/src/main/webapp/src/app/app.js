'use strict';

angular.module('app', ['app.oa', 'app.mobile', 'app.workflow'])

    .value('PageContext', PageContext)

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $modalProvider, cfpLoadingBarProvider, RestangularProvider, $datepickerProvider, $timepickerProvider) {

        $modalProvider.options.backdrop = 'static';

        $urlRouterProvider.otherwise('/oa/task/pending');

        cfpLoadingBarProvider.includeSpinner = false;   // ignoreLoadingBar

        RestangularProvider.setBaseUrl(PageContext.path);

        RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
            if (data.success) {
                return data.data;
            } else {
                deferred.reject(data.message);
            }
        });

        $httpProvider.interceptors.push(function ($q, $location, $filter, toaster) {
            return {
                'request': function (request) {
                    return request || $q.when(request);
                },
                'response': function (response) {
                    if (response.data && response.data.success === false) {
                        toaster.pop('error', "错误", response.data.message);
                        return $q.reject(response);
                    }
                    return response || $q.when(response);
                },

                'responseError': function (rejection) {
                    toaster.pop('error', rejection.config.method + ':' + rejection.config.url, rejection.status + ':' + rejection.statusText);
                    return $q.reject(rejection);
                }
            };
        });

        angular.extend($datepickerProvider.defaults, {
            dateFormat: 'yyyy年M月d日',
            autoclose: true
        });

        angular.extend($timepickerProvider.defaults, {
            timeFormat: 'HH:mm'
        });

    })

    .controller('MainCtrl', function ($rootScope, $http, PageContext, DateFormat) {

        $rootScope.PageContext = PageContext;
        $rootScope.DateFormat = DateFormat;

        $http({
            url: PageContext.path + '/security/getCurrentUserInfo',
            method: 'GET'
        }).then(function (response) {
            var userInfo = response.data.userInfo;
            $rootScope.PageContext.currentUser = {
                objectId: userInfo.objectId,
                name: userInfo.name,
                organization: {
                    objectId: userInfo.organizationId,
                    name: userInfo.organizationName,
                    type: userInfo.organizationType
                }
            };
        });
    })
;
