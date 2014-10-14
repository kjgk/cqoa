'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.outgoing', {
                url: '/outgoing',
                templateUrl: 'app/oa/outgoing/outgoing-list.html',
                controller: 'OutgoingCtrl'
            })
        ;
    })

    .factory('OutgoingService', function ($http, Restangular) {
        var api = Restangular.all('oa/outgoing');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/outgoing',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (outgoing) {
                return api.doPOST(outgoing);
            },
            update: function (outgoing) {
                return api.doPUT(outgoing);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('OutgoingCtrl', function ($scope, $q, $modal, toaster, SimpleTable, OutgoingService) {

        $scope.grid = SimpleTable(OutgoingService.query);

        $scope.createOutgoing = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/outgoing/outgoing-form.html',
                controller: 'OutgoingCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateOutgoing = function (outgoing) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/outgoing/outgoing-form.html',
                controller: 'OutgoingUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return outgoing.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.deleteOutgoing = function (outgoing) {
            OutgoingService.remove(outgoing.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('OutgoingCreateCtrl', function ($scope, $modalInstance, OutgoingService) {

        $scope.outgoing = {
            localCity: 0,
            requiredCar: 0,
            beginDate:new Date,
            endDate:new Date
        };
        $scope.title = '新增出差';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            OutgoingService.create($scope.outgoing).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('OutgoingUpdateCtrl', function ($scope, $modalInstance, OutgoingService, objectId) {

        $scope.outgoing = OutgoingService.get(objectId).then(function (data) {
            $scope.outgoing = data;
            $scope.outgoing.beginDate = new Date(data.beginDate);
            $scope.outgoing.endDate = new Date(data.endDate);
        });
        $scope.title = '修改出差';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            OutgoingService.update($scope.outgoing).then(function () {
                $modalInstance.close();
            });
        };
    });
