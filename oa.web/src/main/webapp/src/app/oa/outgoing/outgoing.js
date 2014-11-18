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

    .controller('OutgoingCtrl', function ($scope, $q, $modal, SimpleTable, OutgoingService, InstanceService) {

        $scope.grid = SimpleTable(OutgoingService.query);

        $scope.createOutgoing = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/outgoing/outgoing-form.html',
                controller: 'OutgoingCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
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
                Toaster.success("保存成功！");
            });
        };
        $scope.viewOutgoing = function (outgoing) {
            $modal.open({
                templateUrl: 'app/oa/outgoing/outgoing-view.html',
                controller: 'OutgoingViewCtrl',
                resolve: {
                    objectId: function () {
                        return outgoing.objectId;
                    }
                }
            });
        };
        $scope.viewOutgoingInstance = function (outgoing) {
            InstanceService.viewInstance({
                relatedObjectId: outgoing.objectId
            });
        };
        $scope.deleteOutgoing = function (outgoing) {

            Dialog.confirmDelete().then(function () {
                OutgoingService.remove(outgoing.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('OutgoingCreateCtrl', function ($scope, $modalInstance, OutgoingService) {

        $scope.outgoing = {
            localCity: 0,
            requiredCar: 0,
            transportation: {
                objectId: ''
            }
        };
        $scope.title = '新增出差申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = OutgoingService.create($scope.outgoing).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('OutgoingUpdateCtrl', function ($scope, $modalInstance, OutgoingService, objectId) {

        $scope.title = '修改出差申请';

        $scope.promise = OutgoingService.get(objectId);

        $scope.outgoing = $scope.promise.$object;

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = OutgoingService.update($scope.outgoing).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('OutgoingViewCtrl', function ($scope, OutgoingService) {

        $scope.outgoing = OutgoingService.get($scope.objectId).$object;
    })

;
