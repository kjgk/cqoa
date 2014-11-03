angular.module('app.mobile')

    .config(function ($stateProvider) {
        $stateProvider
            .state('mobile.appversion', {
                url: '/appversion',
                templateUrl: 'app/mobile/appversion/appversion-list.html',
                controller: 'AppVersionCtrl'
            })
        ;
    })

    .factory('AppVersionService', function ($http, Restangular) {
        var api = Restangular.all('oa/appVersion');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/appVersion',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (appVersion) {
                return api.doPOST(appVersion);
            },
            update: function (appVersion) {
                return api.doPUT(appVersion);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('AppVersionCtrl', function ($scope, $q, $modal, toaster, SimpleTable, AppVersionService) {

        $scope.grid = SimpleTable(AppVersionService.query);

        $scope.createAppVersion = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/mobile/appVersion/appVersion-form.html',
                controller: 'AppVersionCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateAppVersion = function (appVersion) {
            var modalInstance = $modal.open({
                templateUrl: 'app/mobile/appVersion/appVersion-form.html',
                controller: 'AppVersionUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return appVersion.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.deleteAppVersion = function (appVersion) {
            AppVersionService.remove(appVersion.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('AppVersionCreateCtrl', function ($scope, $modalInstance, AppVersionService) {

        $scope.appVersion = {
                status:1
        };

        $scope.title = '新增App';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = AppVersionService.create($scope.appVersion).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('AppVersionUpdateCtrl', function ($scope, $modalInstance, AppVersionService, objectId) {

        $scope.promise = AppVersionService.get(objectId);

        $scope.appVersion = $scope.promise.$object;

        $scope.title = '修改App';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = AppVersionService.update($scope.appVersion).then(function () {
                $modalInstance.close();
            });
        };
    });

;