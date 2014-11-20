'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.carUse', {
                abstract: true,
                template: '<div ui-view></div>',
                url: '/caruse'
            })
            .state('oa.carUse.list', {
                url: '/list',
                templateUrl: 'app/oa/caruse/car-use-list.html',
                controller: 'CarUseCtrl'
            })
            .state('oa.carUse.allot', {
                url: '/allot',
                templateUrl: 'app/oa/caruse/car-use-allot-list.html',
                controller: 'CarUseAllotListCtrl'
            })
        ;
    })

    .filter('caruseusername', function () {
        return function (input) {
            var username = [];
            _.forEach(input, function (carUseUser) {
                username.push(carUseUser.user.name);
            });
            return username.join('，');
        }
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
            queryForAllot: function (params) {
                return $http({
                    url: PageContext.path + '/oa/carUse/allot',
                    method: 'GET',
                    params: params
                });
            },
            allot: function (carUseInfo) {
                return Restangular.one('oa/carUse', carUseInfo.carUse.objectId).post('allot', carUseInfo);
            }
        }
    })

    .controller('CarUseCtrl', function ($scope, $q, $modal, SimpleTable, CarUseService, InstanceService) {

        $scope.grid = SimpleTable(CarUseService.query);

        $scope.createCarUse = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/caruse/car-use-form.html',
                controller: 'CarUseCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
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
                Toaster.success("保存成功！");
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
        $scope.getRegionText = function (region) {
            switch (region) {
                case 0:
                    return "辖区内";
                case 1:
                    return "市内";
                case 2:
                    return "市外";
            }
        }
    })

    .controller('CarUseAllotListCtrl', function ($scope, $q, $modal, SimpleTable, CarUseService) {

        $scope.grid = SimpleTable(CarUseService.queryForAllot);

        $scope.allotCarUse = function (carUse) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/caruse/car-use-allot-form.html',
                controller: 'CarUseAllotCtrl',
                resolve: {
                    objectId: function () {
                        return carUse.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("分配成功！");
            });
        };
        $scope.getRegionText = function (region) {
            switch (region) {
                case 0:
                    return "辖区内";
                case 1:
                    return "市内";
                case 2:
                    return "市外";
            }
        }
    })

    .controller('CarUseCreateCtrl', function ($scope, $modalInstance, CarUseService) {

        $scope.carUse = {
            region: 0,
            carUseUserList: []
        };
        $scope.userList = [];

        $scope.title = '新增用车申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            _.forEach($scope.userList, function (user) {
                $scope.carUse.carUseUserList.push({
                    user: {objectId: user.objectId}
                })
            });
            $scope.promise = CarUseService.create($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUseUpdateCtrl', function ($scope, $modalInstance, CarUseService, objectId) {

        $scope.promise = CarUseService.get(objectId);

        $scope.userList = [];
        $scope.promise.then(function (response) {
            $scope.carUse = response;
            _.forEach($scope.carUse.carUseUserList, function (carUseUser) {
                $scope.userList.push({
                    objectId: carUseUser.user.objectId,
                    name: carUseUser.user.name,
                    organization: {
                        objectId: carUseUser.user.organization.objectId
                    }
                })
            });
        });


        $scope.title = '修改用车申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            _.forEach($scope.userList, function (user) {
                $scope.carUse.carUseUserList.push({
                    user: {objectId: user.objectId}
                })
            });
            $scope.promise = CarUseService.update($scope.carUse).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('CarUseViewCtrl', function ($scope, CarUseService) {

        $scope.carUse = CarUseService.get($scope.objectId).$object;
        $scope.getRegionText = function (region) {
            switch (region) {
                case 0:
                    return "辖区内";
                case 1:
                    return "市内";
                case 2:
                    return "市外";
            }
        }
    })

    .controller('CarUseAllotCtrl', function ($scope, $modalInstance, CarUseService, DriverService, CarService, objectId) {

        $scope.title = '分配车辆';

        $scope.carUserInfo = {
            carUse: {
                objectId: objectId
            },
            driver: {objectId: ''},
            car: {objectId: ''}
        };

        CarService.query().then(function(response){
            $scope.carList = response.data.items;
        });
        DriverService.query().then(function(response){
            $scope.driverList = response.data.items;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = CarUseService.allot($scope.carUserInfo).then(function () {
                $modalInstance.close();
            });

        };
    })
;
