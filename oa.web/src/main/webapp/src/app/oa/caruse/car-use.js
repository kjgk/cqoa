'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.carUse', {
                url: '/caruse',
                templateUrl: 'app/oa/caruse/car-use-list.html',
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
            },
            allot: function (carUseInfo) {
                return Restangular.one('oa/carUse', carUseInfo.carUse.objectId).post('allot', carUseInfo);
            }
        }
    })

    .controller('CarUseCtrl', function ($scope, $q, $modal, toaster, SimpleTable, CarUseService, InstanceService) {

        $scope.grid = SimpleTable(CarUseService.query);

        $scope.createCarUse = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/caruse/car-use-form.html',
                controller: 'CarUseCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateCarUse = function (carUse) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/caruse/car-use-form.html',
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
        $scope.viewCarUseInstance = function (carUse) {
            InstanceService.viewInstance({
                relatedObjectId: carUse.objectId
            });
        };
        $scope.deleteCarUse = function (carUse) {
            Dialog.confirmDelete().then(function () {
                CarUseService.remove(carUse.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success('删除成功！');
                });
            });
        };
        $scope.allotCarUse = function (carUse) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/caruse/car-use-allot.html',
                controller: 'CarUseAllotCtrl',
                resolve: {
                    objectId: function () {
                        return carUse.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "分配成功！");
            });
        };
    })

    .controller('CarUseCreateCtrl', function ($scope, $modalInstance, CarUseService) {

        $scope.carUse = {
//            beginTime: new Date(),
//            endTime: new Date(),
            localCity: 0
        };
        $scope.title = '新增用车申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = CarUseService.create($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUseUpdateCtrl', function ($scope, $modalInstance, CarUseService, objectId) {

        $scope.promise = CarUseService.get(objectId);

        $scope.carUse = $scope.promise.$object;

        $scope.title = '修改用车申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = CarUseService.update($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUseViewCtrl', function ($scope, CarUseService) {

        $scope.carUse = CarUseService.get($scope.objectId).$object;
    })

    .controller('CarUseAllotCtrl', function ($scope, $modalInstance, CarUseService, objectId) {

        $scope.title = '分配车辆';

        $scope.carUserInfo = {
            carUse: {
                objectId: objectId
            },
            driver: {},
            car: {}
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.carUserInfo.driver.objectId = $scope.driverSelect.value;
            $scope.carUserInfo.car.objectId = $scope.carSelect.value;
            $scope.promise = CarUseService.allot($scope.carUserInfo).then(function () {
                $modalInstance.close();
            });

        };
    })
;
