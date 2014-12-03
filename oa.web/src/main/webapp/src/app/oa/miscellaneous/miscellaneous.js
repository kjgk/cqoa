'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.miscellaneous', {
                url: '/miscellaneous',
                templateUrl: 'app/oa/miscellaneous/miscellaneous-list.html',
                controller: 'MiscellaneousCtrl'
            })
        ;
    })

    .factory('MiscellaneousService', function ($http, Restangular) {
        var api = Restangular.all('oa/miscellaneous');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/miscellaneous',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (miscellaneous) {
                return api.doPOST(miscellaneous);
            },
            update: function (miscellaneous) {
                return api.doPUT(miscellaneous);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('MiscellaneousCtrl', function ($scope, $q, $modal, SimpleTable, MiscellaneousService, OAService) {

        $scope.grid = SimpleTable(MiscellaneousService.query);

        $scope.createMiscellaneous = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/miscellaneous/miscellaneous-form.html',
                controller: 'MiscellaneousCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.updateMiscellaneous = function (miscellaneous) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/miscellaneous/miscellaneous-form.html',
                controller: 'MiscellaneousUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return miscellaneous.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.viewMiscellaneous = function (miscellaneous) {

            OAService.viewForm('查看综合事项申请', miscellaneous.objectId, 'app/oa/miscellaneous/miscellaneous-view.html')
        };
        $scope.deleteMiscellaneous = function (miscellaneous) {

            Dialog.confirmDelete().then(function () {
                MiscellaneousService.remove(miscellaneous.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('MiscellaneousCreateCtrl', function ($scope, $modalInstance, MiscellaneousService) {

        $scope.miscellaneous = {
            approver: {objectId: ''}
        };

        $scope.title = '新增综合事项申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = MiscellaneousService.create($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('MiscellaneousUpdateCtrl', function ($scope, $modalInstance, MiscellaneousService, objectId) {

        $scope.title = '修改综合事项申请';

        $scope.promise = MiscellaneousService.get(objectId);

        $scope.miscellaneous = $scope.promise.$object;

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = MiscellaneousService.update($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('MiscellaneousViewCtrl', function ($scope, MiscellaneousService) {

        $scope.miscellaneous = MiscellaneousService.get($scope.objectId).$object;
    })


    .controller('MiscellaneousTaskProcessCtrl', function ($scope, $timeout, $http, $modal, $modalInstance, TaskService, InstanceService, task) {

        $scope.title = '处理';

        $scope.task = task;

        $scope.viewTemplate = 'app/oa/miscellaneous/miscellaneous-view.html';

        $scope.nextFlowNode = 1;

        $http({
            url: PageContext.path + '/workflow/task/fetchHander/organizationManager',
            params: {organizationCode: 'all'},
            method: 'GET'
        }).then(function (response) {
            var items = [];
            _.forEach(response.data.data, function (item) {
                items.push({
                    objectId: item.objectId,
                    name: item.name
                });
            });
            $scope.handlerList1 = items;
        });

        $http({
            url: PageContext.path + '/workflow/task/fetchHander/leader',
            method: 'GET'
        }).then(function (response) {
            var items = [];
            _.forEach(response.data.data, function (item) {
                items.push({
                    objectId: item.objectId,
                    name: item.name
                });
            });
            $scope.handlerList2 = items;
        });

        $scope.approveInfo = {
            taskId: task.objectId,
            opinion: '',
            approvers: []
        };

        $scope.promise = TaskService.getTaskFlowNode(task.objectId).then(function (response) {
            $scope.flowNode = response.data.data;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.process = function (result) {
            if (result == 'pass' && (_.isEmpty($scope.approveInfo.approvers) || $scope.approveInfo.approvers[0] == null)) {
                Dialog.alert('请选择审批人！');
                return;
            }
            if ((result == 'return' || result == 'reject') && _.isEmpty($scope.approveInfo.opinion)) {
                Dialog.alert('请输入审批意见！');
                return;
            }
            $scope.promise = TaskService.processTask(result, {
                taskId: $scope.approveInfo.taskId,
                opinion: $scope.approveInfo.opinion,
                approvers: $scope.approveInfo.approvers,
                contextList: [
                    {contextKey: 'nextFlowNode', contextValue: $scope.nextFlowNode}
                ]
            }).then(function () {
                $modalInstance.close();
                Toaster.info("审批成功！");
            });
        };

    })
;
