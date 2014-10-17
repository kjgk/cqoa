'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.task', {
                url: '/task',
                abstract: true,
                template: '<div ui-view></div>'
            })
            .state('oa.task.pending', {
                url: '/pending',
                templateUrl: 'app/oa/task/task-pending.html',
                controller: 'TaskPendingCtrl'
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
            }
        }
    })

    .controller('TaskPendingCtrl', function ($scope, $q, $modal, toaster, SimpleTable, TaskService) {

        $scope.grid = SimpleTable(TaskService.query);

        $scope.process = function (task) {
            var modalInstance, activity = task.activity.split('@');
            if (activity[1] == 'TaskProcessCtrl') {
                modalInstance = $modal.open({
                    templateUrl: 'app/oa/task/task-process.html',
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
    })

    .controller('TaskProcessCtrl', function ($scope, $timeout, $http, $modalInstance, TaskService, task, viewTemplate, toaster) {

        $scope.title = '处理';

        $scope.task = task;

        $scope.viewTemplate = viewTemplate;

        $scope.approveInfo = {
            taskId: task.objectId,
            opinion: '',
            approvers: []
        };

        TaskService.getFlowNodeByTaskId(task.objectId).then(function (response) {
            $scope.flowNode = response.data.data;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.process = function (result) {
            TaskService.processTask(result, {
                taskId: $scope.approveInfo.taskId,
                opinion: $scope.approveInfo.opinion,
                approvers: _.pluck($scope.approveInfo.approvers, 'objectId')
            }).then(function () {
                $modalInstance.close();
                toaster.pop('info', "信息", "审批成功！");
            });
        };
    })

;
