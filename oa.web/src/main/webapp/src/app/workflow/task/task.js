'use strict';

angular.module('app.workflow')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.task', {
                url: '/task',
                abstract: true,
                template: '<div ui-view></div>'
            })
            .state('oa.task.pending', {
                url: '/pending',
                templateUrl: 'app/workflow/task/task-pending.html',
                controller: 'TaskPendingCtrl'
            })
            .state('oa.task.handled', {
                url: '/handled',
                templateUrl: 'app/workflow/task/task-handled.html',
                controller: 'TaskHandledCtrl'
            })
        ;
    })

    .factory('TaskService', function ($http) {
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/workflow/task/queryCurrentUserTask',
                    method: 'GET',
                    params: params
                });
            },
            getTaskFlowNode: function (taskId) {
                return $http({
                    url: PageContext.path + '/workflow/task/getTaskFlowNode/' + taskId,
                    method: 'GET'
                });
            },
            processTask: function (result, approveInfo) {
                return $http({
                    url: PageContext.path + '/workflow/task/' + result,
                    method: 'POST',
                    data: approveInfo
                });
            },
            rollbackTask: function (taskId) {
                return $http({
                    url: PageContext.path + '/workflow/task/rollback/' + taskId,
                    method: 'POST'
                });
            },
            transmitTask: function (taskId, handler) {
                return $http({
                    url: PageContext.path + '/workflow/task/transmit/' + taskId,
                    method: 'POST',
                    data: {
                        handler: handler
                    }
                });
            }
        }
    })

    .controller('TaskPendingCtrl', function ($scope, $modal, SimpleTable, TaskService, InstanceService) {

        $scope.grid = SimpleTable(TaskService.query, {params: {statusTag: 'Running'}});

        $scope.process = function (task) {
            var modalInstance, activity = task.activity.split('@');
            if (activity[1] == 'TaskProcessCtrl') {
                modalInstance = $modal.open({
                    templateUrl: 'app/workflow/task/task-process.html',
                    controller: activity[1],
                    resolve: {
                        task: function () {
                            return task;
                        },
                        viewTemplate: function () {
                            return activity[0];
                        }
                    }
                })
            } else {
                modalInstance = $modal.open({
                    templateUrl: activity[0],
                    controller: activity[1],
                    resolve: {
                        task: function () {
                            return task;
                        },
                        objectId: function () {
                            return task.relatedObjectId;
                        }
                    }
                })
            }
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.transmitTask = function (task) {
            var modalInstance = $modal.open({
                templateUrl: 'app/workflow/task/task-transmit.html',
                controller: 'InstanceTransmitCtrl',
                resolve: {
                    taskId: function () {
                        return task.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };

        $scope.viewInstance = function (task) {
            InstanceService.viewInstance(task.instanceId);
        };
    })

    .controller('TaskProcessCtrl', function ($scope, $timeout, $http, $modal, $modalInstance, TaskService, InstanceService, task, viewTemplate) {

        $scope.title = '处理';

        $scope.task = task;

        $scope.viewTemplate = viewTemplate;

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
            if (result == 'pass' && $scope.flowNode.manualSelectHandler == 1 && $scope.flowNode.nextFlowNodeType !== 'End'
                && (_.isEmpty($scope.approveInfo.approvers) || $scope.approveInfo.approvers[0] == null)) {
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
                approvers: $scope.approveInfo.approvers
            }).then(function () {
                $modalInstance.close();
                Toaster.info("审批成功！");
            });
        };

    })

    .controller('InstanceTransmitCtrl', function ($scope, $modalInstance, TaskService, taskId) {

        $scope.hander = {};

        $scope.submit = function () {
            $scope.promise = TaskService.transmitTask(taskId, $scope.hander.objectId).then(function () {
                $modalInstance.close();
                Toaster.info("任务转发成功！");
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

    .controller('TaskHandledCtrl', function ($scope, $modal, SimpleTable, TaskService, InstanceService) {

        $scope.grid = SimpleTable(TaskService.query, {params: {statusTag: 'Finish'}});

        $scope.rollbackTask = function (task) {
            Dialog.confirm('确认回滚？').then(function () {
                TaskService.rollbackTask(task.objectId).then(function () {
                    Toaster.info("任务回滚成功！");
                    $scope.grid.refresh();
                });
            });
        };

        $scope.viewInstance = function (task) {
            InstanceService.viewInstance(task.instanceId);
        };
    })
;
