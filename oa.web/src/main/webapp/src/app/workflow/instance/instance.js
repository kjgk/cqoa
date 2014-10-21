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
        ;
    })

    .factory('InstanceService', function ($http, $modal) {
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/workflow/instance/query',
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
            queryInstanceTaskLog: function (instanceId, onlyShowApproveLog) {
                return $http({
                    url: PageContext.path + '/workflow/instance/listInstanceTaskLog',
                    params: {
                        instanceId: instanceId,
                        onlyShowApproveLog: onlyShowApproveLog
                    },
                    method: 'GET'
                });
            },
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
            }
        }
    })

    .controller('InstanceListCtrl', function ($scope, $q, $modal, toaster, SimpleTable, InstanceService) {

        $scope.grid = SimpleTable(InstanceService.query);

        $scope.view = function (instance) {
            InstanceService.viewInstance(instance.objectId);
        };
    })

    .controller('InstanceViewCtrl', function ($scope, $modalInstance, InstanceService, params) {

        InstanceService.getInstance(params).then(function (response) {
            $scope.instance = response.data.data;
            InstanceService.queryInstanceTaskLog($scope.instance.objectId).then(function (response) {
                $scope.taskLogList = response.data.items;
            });

        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

;
