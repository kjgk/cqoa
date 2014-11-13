'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.car', {
                url: '/car',
                templateUrl: 'app/oa/car/car-list.html',
                controller: 'CarCtrl'
            })
        ;
    })

    .factory('CarService', function ($http, Restangular) {
        var api = Restangular.all('oa/car');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/car',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (car) {
                return api.doPOST(car);
            },
            update: function (car) {
                return api.doPUT(car);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('CarCtrl', function ($scope, $q, $modal, SimpleTable, CarService) {

        $scope.grid = SimpleTable(CarService.query);

        $scope.createCar = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/car/car-form.html',
                controller: 'CarCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.updateCar = function (car) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/car/car-form.html',
                controller: 'CarUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return car.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.deleteCar = function (car) {
            CarService.remove(car.objectId).then(function () {
                $scope.grid.refresh();
                Toaster.success("删除成功！");
            });
        };
    })

    .controller('CarCreateCtrl', function ($scope, $modalInstance, CarService) {

        $scope.car = {
            status: 1,
            purchaseDate: new Date()
        };
        $scope.title = '新增车辆';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = CarService.create($scope.car).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUpdateCtrl', function ($scope, $modalInstance, CarService, objectId) {

        $scope.promise = CarService.get(objectId);

        $scope.car = $scope.promise.$object;

        $scope.title = '修改车辆';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = CarService.update($scope.car).then(function () {
                $modalInstance.close();
            });
        };
    });
