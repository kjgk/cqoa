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

    .controller('LeaveCtrl', function ($scope, $q, $modal, SimpleTable, LeaveService, OAService) {

        $scope.grid = SimpleTable(LeaveService.query);

        $scope.createLeave = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/leave/leave-form.html',
                controller: 'LeaveCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
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
                Toaster.success("保存成功！");
            });
        };
        $scope.viewLeave = function (leave) {

            OAService.viewForm('查看请假申请', leave.objectId, 'app/oa/leave/leave-view.html');
        };
        $scope.deleteLeave = function (leave) {

            Dialog.confirmDelete().then(function () {
                LeaveService.remove(leave.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('LeaveCreateCtrl', function ($scope, $modalInstance, LeaveService) {

        $scope.leave = {
            approver: {objectId: ''}
        };
        $scope.title = '新增请假申请';


        $scope.userSelectConfig = {
            'Personnel': 'OrganizationManager',
            'DeputyManager': 'OrganizationManager',
            'Manager': 'Leader',
            'Leader': 'Leader',
            'Boss': 'Leader'
        }[PageContext.currentUser.role.tag];

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = LeaveService.create($scope.leave).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('LeaveUpdateCtrl', function ($scope, $modalInstance, LeaveService, objectId) {

        $scope.title = '修改请假申请';

        $scope.promise = LeaveService.get(objectId);

        $scope.leave = $scope.promise.$object;

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = LeaveService.update($scope.leave).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('LeaveViewCtrl', function ($scope, LeaveService) {

        $scope.leave = LeaveService.get($scope.objectId).$object;
    })
;
