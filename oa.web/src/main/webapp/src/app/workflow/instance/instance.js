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
            getInstance: function (objectId) {
                return $http({
                    url: PageContext.path + '/workflow/instance/view',
                    params: {
                        objectId: objectId
                    },
                    method: 'GET'
                });
            },
            queryInstanceTaskLog: function (instanceId) {
                return $http({
                    url: PageContext.path + '/workflow/instance/listInstanceTaskLog',
                    params: {
                        instanceId: instanceId
                    },
                    method: 'GET'
                });
            },
            viewInstance: function (objectId) {
                $modal.open({
                    templateUrl: 'app/workflow/instance/instance-view.html',
                    controller: 'InstanceViewCtrl',
                    size: 'lg',
                    resolve: {
                        objectId: function () {
                            return objectId;
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

    .controller('InstanceViewCtrl', function ($scope, $modalInstance, InstanceService, objectId) {

        InstanceService.getInstance(objectId).then(function (response) {
            $scope.instance = response.data.data;
        });
        InstanceService.queryInstanceTaskLog(objectId).then(function (response) {
            $scope.taskLogList = response.data.items;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

;
