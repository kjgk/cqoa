'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.training', {
                url: '/training',
                templateUrl: 'app/oa/training/training-list.html',
                controller: 'TrainingCtrl'
            })
        ;
    })

    .factory('TrainingService', function ($http, Restangular) {
        var api = Restangular.all('oa/training');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/training',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (training) {
                return api.doPOST(training);
            },
            update: function (training) {
                return api.doPUT(training);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('TrainingCtrl', function ($scope, $q, $modal, toaster, SimpleTable, TrainingService,InstanceService) {

        $scope.grid = SimpleTable(TrainingService.query);

        $scope.createTraining = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/training/training-form.html',
                controller: 'TrainingCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateTraining = function (training) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/training/training-form.html',
                controller: 'TrainingUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return training.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.viewTraining = function (training) {
            $modal.open({
                templateUrl: 'app/oa/training/training-view.html',
                controller: 'TrainingViewCtrl',
                resolve: {
                    objectId: function () {
                        return training.objectId;
                    }
                }
            });
        };
        $scope.viewTrainingInstance = function (training) {
            InstanceService.viewInstance({
                relatedObjectId: training.objectId
            });
        };
        $scope.deleteTraining = function (training) {
            TrainingService.remove(training.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('TrainingCreateCtrl', function ($scope, $modalInstance, TrainingService) {

        $scope.training = {
            beginDate: new Date(),
            endDate: new Date()
        };
        $scope.title = '新增培训申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            TrainingService.create($scope.training).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('TrainingUpdateCtrl', function ($scope, $modalInstance, TrainingService, objectId) {

        $scope.training = TrainingService.get(objectId).then(function (data) {
            $scope.training = data;
            // ng-quickDate 接收 Date 类型为参数
            $scope.training.beginDate = new Date(data.beginDate);
            $scope.training.endDate = new Date(data.endDate);
        });
        $scope.title = '修改培训申请';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            TrainingService.update($scope.training).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('TrainingViewCtrl', function ($scope, TrainingService) {

        $scope.training = TrainingService.get($scope.objectId).$object;
    })

;
