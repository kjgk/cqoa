'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.miscellaneous', {
                url: '/miscellaneous',
                templateUrl: 'app/oa/miscellaneous/miscellaneous-list.html',
                controller: 'miscellaneousCtrl'
            })
        ;
    })

    .factory('miscellaneousService', function ($http, Restangular) {
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

    .controller('miscellaneousCtrl', function ($scope, $q, $modal, toaster, SimpleTable, miscellaneousService) {

        $scope.grid = SimpleTable(miscellaneousService.query);

        $scope.createMiscellaneous = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/miscellaneous/miscellaneous-form.html',
                controller: 'miscellaneousCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateMiscellaneous = function (miscellaneous) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/miscellaneous/miscellaneous-form.html',
                controller: 'miscellaneousUpdateCtrl',
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
        $scope.deleteMiscellaneous = function (miscellaneous) {
            miscellaneousService.remove(miscellaneous.objectId).then(function () {
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

    .controller('miscellaneousCreateCtrl', function ($scope, $modalInstance, miscellaneousService) {

        $scope.miscellaneous = {
        };
        $scope.title = '新增综合事项申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            miscellaneousService.create($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('miscellaneousUpdateCtrl', function ($scope, $modalInstance, miscellaneousService, objectId) {


        $scope.miscellaneous = miscellaneousService.get(objectId).$object;
        $scope.title = '修改综合事项申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            miscellaneousService.update($scope.miscellaneous).then(function () {
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

;
