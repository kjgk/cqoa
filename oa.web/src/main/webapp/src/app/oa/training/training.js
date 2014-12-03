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

    .controller('TrainingCtrl', function ($scope, $q, $modal, SimpleTable, TrainingService, OAService) {

        $scope.grid = SimpleTable(TrainingService.query);

        $scope.createTraining = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/training/training-form.html',
                controller: 'TrainingCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
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
                Toaster.success("保存成功！");
            });
        };
        $scope.viewTraining = function (training) {
            OAService.viewForm('查看培训申请', training.objectId, 'app/oa/training/training-view.html')
        };
        $scope.deleteTraining = function (training) {
            Dialog.confirmDelete().then(function () {
                TrainingService.remove(training.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('TrainingCreateCtrl', function ($scope, $modalInstance, TrainingService) {

        $scope.training = {
            approver: {objectId: ''}
        };

        $scope.title = '新增培训申请';

        // 政治部可以选择培训类型
        $scope.organizationCode = '100000000010000001';
        if (PageContext.currentUser.organization.code == $scope.organizationCode) {
            $scope.showTrainingType = true;
            $scope.training.trainingType = 1;
        } else {
            $scope.showTrainingType = false;
            $scope.training.trainingType = 2;
        }

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = TrainingService.create($scope.training).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('TrainingUpdateCtrl', function ($scope, $modalInstance, TrainingService, objectId) {

        $scope.promise = TrainingService.get(objectId);

        $scope.training = $scope.promise.$object;

        $scope.title = '修改培训申请';

        $scope.showTrainingType = false;

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = TrainingService.update($scope.training).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('TrainingViewCtrl', function ($scope, TrainingService) {

        $scope.training = TrainingService.get($scope.objectId).$object;
    })

;
