'use strict';

angular.module('app.workflow')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.instance', {
                url: '/instance',
                abstract: true,
                template: '<div ui-view></div>'
            })
            .state('oa.instance.list', {
                url: '/list',
                templateUrl: 'app/workflow/instance/instance-list.html',
                controller: 'InstanceListCtrl'
            })
            .state('oa.instance.all', {
                url: '/all',
                templateUrl: 'app/workflow/instance/instance-all.html',
                controller: 'InstanceAllCtrl'
            })
        ;
    })

    .directive('instanceStatus', function () {
        return {
            template: '<label style="margin-bottom: 0; font-size: 100%; width: 54px; display: inline-block; padding: 4px 8px;" class="label {{labelClass}}">{{status.name}}</label>',
            restrict: 'A',
            replace: true,
            scope: {
                status: '=instanceStatus'
            },
            link: function (scope, element, attrs, ngModel) {

                console.log(scope.status.codeTag)
                scope.labelClass = {
                    Running: 'label-primary',
                    Stop: 'label-danger',
                    Finish: 'label-success'
                }[scope.status.codeTag]
            }
        };
    })

    .factory('InstanceService', function ($http, $modal) {
        return {
            queryAll: function (params) {
                return $http({
                    url: PageContext.path + '/workflow/instance/query',
                    method: 'GET',
                    params: params
                });
            },
            queryCurrent: function (params) {
                return $http({
                    url: PageContext.path + '/workflow/instance/queryCurrentUserInstance',
                    method: 'GET',
                    params: params
                });
            },
            getInstance: function (params) {
                return $http({
                    url: PageContext.path + '/workflow/instance/view',
                    params: params,
                    method: 'GET'
                });
            },
            queryInstanceTaskLog: function (params, onlyShowApproveLog) {
                return $http({
                    url: PageContext.path + '/workflow/instance/listInstanceTaskLog',
                    params: _.extend(params, {onlyShowApproveLog: onlyShowApproveLog}),
                    method: 'GET'
                });
            },
            // 流程查看
            viewInstance: function (params) {
                var _params = {};
                if (_.isString(params)) {
                    _params.objectId = params;
                } else {
                    _params = params;
                }
                $modal.open({
                    templateUrl: 'app/workflow/instance/instance-view.html',
                    controller: 'InstanceViewCtrl',
                    size: 'lg',
                    resolve: {
                        params: function () {
                            return _params;
                        }
                    }
                });
            },
            // 审批日志查看
            viewInstanceLog: function (params) {
                var _params = {};
                if (_.isString(params)) {
                    _params.objectId = params;
                } else {
                    _params = params;
                }
                $modal.open({
                    templateUrl: 'app/workflow/instance/instance-view-log.html',
                    controller: 'InstanceLogViewCtrl',
                    size: 'lg',
                    resolve: {
                        params: function () {
                            return _params;
                        }
                    }
                });
            }
        }
    })

    .controller('InstanceListCtrl', function ($scope, $q, $modal, SimpleTable, InstanceService) {

        $scope.grid = SimpleTable(InstanceService.queryCurrent);

        $scope.view = function (instance) {
            InstanceService.viewInstance(instance.objectId);
        };
    })

    .controller('InstanceAllCtrl', function ($scope, $q, $modal, SimpleTable, InstanceService) {

        $scope.grid = SimpleTable(InstanceService.queryAll);

        $scope.view = function (instance) {
            InstanceService.viewInstance(instance.objectId);
        };
    })

    .controller('InstanceViewCtrl', function ($scope, $modalInstance, InstanceService, params) {

        $scope.promise = InstanceService.getInstance(params).then(function (response) {
            $scope.instance = response.data.data;
            return InstanceService.queryInstanceTaskLog({instanceId: $scope.instance.objectId}, false).then(function (response) {
                $scope.taskLogList = response.data.items;
            });
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

    .controller('InstanceLogViewCtrl', function ($scope, $modalInstance, InstanceService, params) {

        $scope.promise = InstanceService.queryInstanceTaskLog(params, true).then(function (response) {
            $scope.taskLogList = response.data.items;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

;
