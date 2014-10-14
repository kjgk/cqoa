'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.leave', {
                url: '/leave',
                templateUrl: 'app/oa/leave/leave-list.html',
                controller: 'LeaveCtrl'
            })
        ;
    })

    .factory('LeaveService', function ($http, Restangular) {
        var api = Restangular.all('oa/leave');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/leave',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (leave) {
                return api.doPOST(leave);
            },
            update: function (leave) {
                return api.doPUT(leave);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('LeaveCtrl', function ($scope, $q, $modal, toaster, SimpleTable, LeaveService) {

        $scope.grid = SimpleTable(LeaveService.query);

        $scope.createLeave = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/leave/leave-form.html',
                controller: 'LeaveCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateLeave = function (leave) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/leave/leave-form.html',
                controller: 'LeaveUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return leave.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.deleteLeave = function (leave) {
            LeaveService.remove(leave.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('LeaveCreateCtrl', function ($scope, $modalInstance, LeaveService) {

        $scope.leave = {
            localCity: 0
        };
        $scope.title = '新增请假';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            LeaveService.create($scope.leave).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('LeaveUpdateCtrl', function ($scope, $modalInstance, LeaveService, objectId) {

        $scope.leave = LeaveService.get(objectId).then(function (data) {
            $scope.leave = data;
            // ng-quickDate 接收 Date 类型为参数
            $scope.leave.beginDate = new Date(data.beginDate);
            $scope.leave.endDate = new Date(data.endDate);
        });
        $scope.title = '修改请假';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            LeaveService.update($scope.leave).then(function () {
                $modalInstance.close();
            });
        };
    });
