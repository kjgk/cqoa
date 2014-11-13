'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.driver', {
                url: '/driver',
                templateUrl: 'app/oa/driver/driver-list.html',
                controller: 'DriverCtrl'
            })
        ;
    })

    .factory('DriverService', function ($http, Restangular) {
        var api = Restangular.all('oa/driver');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/driver',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (driver) {
                return api.doPOST(driver);
            },
            update: function (driver) {
                return api.doPUT(driver);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('DriverCtrl', function ($scope, $q, $modal, SimpleTable, DriverService) {

        $scope.grid = SimpleTable(DriverService.query);

        $scope.createDriver = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/driver/driver-form.html',
                controller: 'DriverCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.updateDriver = function (driver) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/driver/driver-form.html',
                controller: 'DriverUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return driver.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.deleteDriver = function (driver) {
            Dialog.confirmDelete().then(function () {
                DriverService.remove(driver.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('DriverCreateCtrl', function ($scope, $modalInstance, DriverService) {

        $scope.driver = {
            gender: 1,
            status: 1,
            birthday: new Date(),
            licenseDate: new Date
        };
        $scope.title = '新增司机';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = DriverService.create($scope.driver).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('DriverUpdateCtrl', function ($scope, $modalInstance, DriverService, objectId) {

        $scope.promise = DriverService.get(objectId);

        $scope.driver = $scope.promise.$object;

        $scope.title = '修改司机';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = DriverService.update($scope.driver).then(function () {
                $modalInstance.close();
            });
        };
    });
