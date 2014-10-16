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
            }
        }
    })

    .controller('TaskPendingCtrl', function ($scope, $q, $modal, toaster, SimpleTable, TaskService) {

        $scope.grid = SimpleTable(TaskService.query);

        $scope.process = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/task/task-process.html',
                controller: 'TaskProcessCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
            });
        };
    })

    .controller('TaskProcessCtrl', function ($scope, $modalInstance, TaskService, toaster) {

        $scope.title = '处理';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
//            TaskService.create($scope.training).then(function () {
                $modalInstance.close();
//            });

            toaster.pop('info', "信息", "xxxxxxxxx！");
        };
    })


;
