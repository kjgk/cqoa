'use strict';

angular.module('app.oa', ['base'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('oa', {
                url: '/oa',
                templateUrl: 'app/oa/oa-menu.html',
                controller: 'OaCtrl'
            });

        $urlRouterProvider.when('/oa', '/oa/meetingroom');
    })

    .factory('OAService', function ($modal) {
        return {
            viewForm: function (title, objectId, viewTemplate) {
                var modalInstance = $modal.open({
                    templateUrl: 'app/oa/form-view.html',
                    controller: ['$scope', '$modalInstance', 'InstanceService', function (scope, $modalInstance, InstanceService) {
                        scope.cancel = function () {
                            $modalInstance.dismiss();
                        };
                        scope.title = title;
                        scope.objectId = objectId;
                        scope.viewTemplate = viewTemplate;
                        scope.viewInstance = function () {
                            InstanceService.viewInstance({
                                relatedObjectId: objectId
                            })
                        };
                    }]
                });
                modalInstance.result.then(function (result) {
                });
            }
        }
    })

    .controller('OaCtrl', function ($scope, $http, $modal, toaster) {

        // 加载菜单
        var loadMenu = function (menuId) {
            return $http({
                url: PageContext.path + '/security/loadMenu',
                params: {
                    node: menuId
                },
                method: 'GET'
            });
        };

        $scope.menus = [];
        loadMenu('Root').then(function (response) {
            _.each(response.data.nodes, function (node) {
                if (node.text == '业务管理') {
                    loadMenu(node.id).then(function (response) {
                        _.each(response.data.nodes, function (node) {
                            $scope.menus.push({
                                text: node.text,
                                url: node.attributes.url
                            });
                        })
                    });
                }
            })
        });

        $scope.modifyPassword = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/modify-password-form.html',
                controller: ['$scope', '$modalInstance', function (scope, $modalInstance) {

                    scope.cancel = function () {
                        $modalInstance.dismiss();
                    };

                    scope.submit = function () {
                        scope.promise = $http({
                            url: PageContext.path + '/security/modifyPassword',
                            method: 'POST',
                            data: {
                                oldPassword: scope.oldPassword,
                                newPassword: scope.newPassword
                            }
                        }).then(function (response) {
                            $modalInstance.close();
                        })
                    };
                }]
            });
            modalInstance.result.then(function (result) {
                Toaster.success("密码修改成功！");
            });
        };

    })
;
