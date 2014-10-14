'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.carUse', {
                url: '/carUse',
                templateUrl: 'app/oa/carUse/carUse-list.html',
                controller: 'CarUseCtrl'
            })
        ;
    })

    .factory('CarUseService', function ($http, Restangular) {
        var api = Restangular.all('oa/carUse');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/carUse',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (carUse) {
                return api.doPOST(carUse);
            },
            update: function (carUse) {
                return api.doPUT(carUse);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('CarUseCtrl', function ($scope, $q, $modal, toaster, SimpleTable, CarUseService) {

        $scope.grid = SimpleTable(CarUseService.query);

        $scope.createCarUse = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/carUse/carUse-form.html',
                controller: 'CarUseCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateCarUse = function (carUse) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/carUse/carUse-form.html',
                controller: 'CarUseUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return carUse.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.deleteCarUse = function (carUse) {
            CarUseService.remove(carUse.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('CarUseCreateCtrl', function ($scope, $modalInstance, CarUseService) {

        $scope.carUse = {
            startTime: new Date(),
            endTime: new Date()
        };
        $scope.title = '新增用车';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            CarUseService.create($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUseUpdateCtrl', function ($scope, $modalInstance, CarUseService, objectId) {


        $scope.carUse = CarUseService.get(objectId).then(function (data) {
            $scope.carUse = data;
            $scope.carUse.startTime = new Date(data.startTime);
            $scope.carUse.endTime = new Date(data.endTime);
        });
        $scope.title = '修改用车';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            CarUseService.update($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    });
