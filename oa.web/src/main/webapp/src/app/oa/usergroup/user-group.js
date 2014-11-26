'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.userGroup', {
                url: '/userGroup',
                templateUrl: 'app/oa/usergroup/user-group-list.html',
                controller: 'UserGroupCtrl'
            })
        ;
    })

    .factory('UserGroupService', function ($http, Restangular) {
        var api = Restangular.all('oa/userGroup');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/userGroup',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (userGroup) {
                return api.doPOST(userGroup);
            },
            update: function (userGroup) {
                return api.doPUT(userGroup);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('UserGroupCtrl', function ($scope, $q, $modal, SimpleTable, UserGroupService, InstanceService) {

        $scope.grid = SimpleTable(UserGroupService.query);

        $scope.createUserGroup = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/usergroup/user-group-form.html',
                controller: 'UserGroupCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.updateUserGroup = function (userGroup) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/usergroup/user-group-form.html',
                controller: 'UserGroupUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return userGroup.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };

        $scope.deleteUserGroup = function (userGroup) {

            Dialog.confirmDelete().then(function () {
                UserGroupService.remove(userGroup.objectId).then(function () {
                    $scope.grid.refresh();
                    Toaster.success("删除成功！");
                });
            });
        };
    })

    .controller('UserGroupCreateCtrl', function ($scope, $modalInstance, UserGroupService) {

        $scope.userGroup = {
            userGroupDetailList: []
        };
        $scope.userList = [];

        $scope.title = '新增人员配置';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            _.forEach($scope.userList, function (user) {
                $scope.userGroup.userGroupDetailList.push({
                    user: {objectId: user.objectId}
                })
            });

            $scope.promise = UserGroupService.create($scope.userGroup).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('UserGroupUpdateCtrl', function ($scope, $timeout, $modalInstance, UserGroupService, objectId) {

        $scope.title = '修改人员配置';

        $scope.promise = UserGroupService.get(objectId);

        $scope.userList = [];
        $scope.promise.then(function (response) {
            $scope.userGroup = response;
            _.forEach($scope.userGroup.userGroupDetailList, function (userGroupDetail) {
                $scope.userList.push({
                    objectId: userGroupDetail.user.objectId,
                    name: userGroupDetail.user.name,
                    organization: {
                        objectId: userGroupDetail.user.organization.objectId
                    }
                })
            });

        });

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            _.forEach($scope.userList, function (user) {
                $scope.userGroup.userGroupDetailList.push({
                    user: {objectId: user.objectId}
                })
            });

            $scope.promise = UserGroupService.update($scope.userGroup).then(function () {
                $modalInstance.close();
            });
        };
    })

;
