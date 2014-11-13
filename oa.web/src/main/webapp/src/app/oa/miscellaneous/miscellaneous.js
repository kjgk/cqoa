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
        $scope.viewMiscellaneousInstance = function (miscellaneous) {
            InstanceService.viewInstance({
                relatedObjectId: miscellaneous.objectId
            });
        };
        $scope.deleteMiscellaneous = function (miscellaneous) {
            MiscellaneousService.remove(miscellaneous.objectId).then(function () {
                $scope.grid.refresh();
                Toaster.success("删除成功！");
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
            $scope.promise =  MiscellaneousService.update($scope.miscellaneous).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('MiscellaneousViewCtrl', function ($scope, MiscellaneousService) {

        $scope.miscellaneous = MiscellaneousService.get($scope.objectId).$object;
    })

;
