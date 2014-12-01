'use strict';

angular.module('app', ['app.oa', 'app.mobile', 'app.workflow'])

    .value('PageContext', PageContext)

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $modalProvider, ngDialogProvider, cfpLoadingBarProvider, RestangularProvider) {

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

        ngDialogProvider.setDefaults({
            className: 'ngdialog-theme-default',
            closeByDocument: false,
            closeByEscape: true
        });

        $httpProvider.interceptors.push(function ($q, $location, $filter, cgBusyMessage) {
            return {
                'request': function (request) {
                    if (request.method == 'GET') {
                        cgBusyMessage.setMessage('正在加载，请稍候...');
                    } else {
                        cgBusyMessage.setMessage('正在处理，请稍候...');
                    }
                    return request || $q.when(request);
                },
                'response': function (response) {
                    if (response.data === 'SessionInvalid') {
                        window.location.href = PageContext.path;
                        return $q.reject(response);
                    }
                    if (response.data && response.data.success === false) {
                        Toaster.error(response.data.message);
                        return $q.reject(response);
                    }
                    return response || $q.when(response);
                },

                'responseError': function (rejection) {
                    Toaster.error(rejection.config.method + ':' + rejection.config.url, rejection.status + ':' + rejection.statusText);
                    return $q.reject(rejection);
                }
            };
        });
    })

    .value('cgBusyDefaults', {
        delay: 300
    })

    .controller('MainCtrl', function ($rootScope, $scope, $window, $http, PageContext, DateFormat, InstanceService) {

        $rootScope.PageContext = PageContext;
        $rootScope.DateFormat = DateFormat;

        // 加载登录用户信息
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
                    code: userInfo.organizationCode
                },
                role: {
                    objectId: userInfo.roleId,
                    name: userInfo.roleName,
                    tag: userInfo.roleTag
                },
                isAdmin: userInfo.isAdmin == 1
            };
        });

        // 高度自适应
        $rootScope.windowWidth = document.documentElement.clientWidth;
        $rootScope.windowHeight = document.documentElement.clientHeight;
        angular.element($window).bind('resize', function () {
            $rootScope.windowWidth = document.documentElement.clientWidth;
            $rootScope.windowHeight = document.documentElement.clientHeight;
            $rootScope.$apply('windowWidth');
            $rootScope.$apply('windowHeight');
        });

        $rootScope.getMenuHeight = function () {
            return $rootScope.windowHeight - 194;
        };

        $rootScope.viewInstanceLog = function (params) {
            InstanceService.viewInstanceLog(params);
        }
    })
;
