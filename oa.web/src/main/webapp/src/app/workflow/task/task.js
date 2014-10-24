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
            getFlowNodeByTaskId: function (taskId) {
                return $http({
                    url: PageContext.path + '/workflow/task/getFlowNodeByTaskId/' + taskId,
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

    .controller('TaskPendingCtrl', function ($scope, $modal, toaster, SimpleTable, TaskService, InstanceService) {

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

    .controller('TaskProcessCtrl', function ($scope, $timeout, $http, $modal, $modalInstance, TaskService, InstanceService, task, viewTemplate, toaster) {

        $scope.title = '处理';

        $scope.task = task;

        $scope.viewTemplate = viewTemplate;

        $scope.approveInfo = {
            taskId: task.objectId,
            opinion: '',
            approvers: []
        };

        $scope.promise = TaskService.getFlowNodeByTaskId(task.objectId).then(function (response) {
            $scope.flowNode = response.data.data;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.process = function (result) {
            if (result == 'pass' && $scope.flowNode.manualSelectHandler == 1 && _.isEmpty($scope.approveInfo.approvers)) {
                toaster.pop('warning', "警告", "请选择审批人！");
                return;
            }
            $scope.promise = TaskService.processTask(result, {
                taskId: $scope.approveInfo.taskId,
                opinion: $scope.approveInfo.opinion,
                approvers: _.pluck($scope.approveInfo.approvers, 'objectId')
            }).then(function () {
                $modalInstance.close();
                toaster.pop('info', "信息", "审批成功！");
            });
        };

        $scope.viewInstanceLog = function () {
            $modal.open({
                templateUrl: 'app/workflow/instance/instance-view-log.html',
                size: 'lg',
                controller: function ($scope, $modalInstance) {
                    $scope.promise = InstanceService.queryInstanceTaskLog(task.instanceId, true).then(function (response) {
                        $scope.taskLogList = response.data.items;
                        $scope.cancel = function () {
                            $modalInstance.dismiss();
                        };
                    });
                }
            });
        }
    })

    .controller('InstanceTransmitCtrl', function ($scope, $modalInstance, toaster, TaskService, taskId) {

        $scope.hander = {};

        $scope.submit = function () {
            $scope.promise = TaskService.transmitTask(taskId, $scope.hander.objectId).then(function () {
                $modalInstance.close();
                toaster.pop('info', "信息", "任务转发成功！");
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

    .controller('TaskHandledCtrl', function ($scope, $modal, toaster, SimpleTable, TaskService, InstanceService) {

        $scope.grid = SimpleTable(TaskService.query, {params: {statusTag: 'Finish'}});

        $scope.rollbackTask = function (task) {
            TaskService.rollbackTask(task.objectId).then(function () {
                toaster.pop('info', "信息", "任务回滚成功！");
                $scope.grid.refresh();
            });
        };

        $scope.viewInstance = function (task) {
            InstanceService.viewInstance(task.instanceId);
        };
    })
;
