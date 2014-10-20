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

    .controller('MiscellaneousCtrl', function ($scope, $q, $modal, toaster, SimpleTable, MiscellaneousService, InstanceService) {

        $scope.grid = SimpleTable(MiscellaneousService.query);

        $scope.createMiscellaneous = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/miscellaneous/miscellaneous-form.html',
                controller: 'MiscellaneousCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
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
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.viewMiscellaneousInstance = function (miscellaneous) {
            InstanceService.viewInstance({
                relatedObjectId: miscellaneous.objectId
            });
        };
        $scope.deleteMiscellaneous = function (miscellaneous) {
            MiscellaneousService.remove(miscellaneous.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
        $scope.viewMiscellaneousWorkFlow = function () {
            $modal.open({
                templateUrl: 'app/oa/miscellaneous/view-miscellaneous-work-flow.html',
                controller: 'ViewMiscellaneousWorkFlowCtrl'
            });
        };
    })

    .controller('MiscellaneousCreateCtrl', function ($scope, $modalInstance, MiscellaneousService) {

        $scope.miscellaneous = {
        };
        $scope.title = '新增综合事项申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            MiscellaneousService.create($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('MiscellaneousUpdateCtrl', function ($scope, $modalInstance, MiscellaneousService, objectId) {


        $scope.miscellaneous = MiscellaneousService.get(objectId).$object;
        $scope.title = '修改综合事项申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            MiscellaneousService.update($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('ViewMiscellaneousWorkFlowCtrl', function ($scope, $modalInstance) {

        $scope.title = '综合事项申请流程';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })

    .controller('MiscellaneousViewCtrl', function ($scope, MiscellaneousService) {

        $scope.miscellaneous = MiscellaneousService.get($scope.objectId).$object;
    })

;
