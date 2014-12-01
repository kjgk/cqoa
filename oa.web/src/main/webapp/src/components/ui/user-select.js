'use strict';

angular.module('withub.ui')

    .directive('userSelect', function ($http) {
        return {
            templateUrl: 'components/ui/template/user-select.html',
            restrict: 'EA',
            replace: false,
            scope: true,
            compile: function () {
                return function (scope, element, attrs) {
                    scope.placeholder = attrs.placeholder;
                    scope.showAll = attrs.showAll !== undefined && attrs.showAll !== false;
                    scope.showNone = attrs.showNone !== undefined && attrs.showNone !== false;

                    var config = attrs['userSelect'];
                    if (config == 'UserGroup') {
                        $http({
                            url: PageContext.path + '/oa/userGroup/users',
                            params: {
                                tag: attrs['userGroupTag']
                            },
                            method: 'GET'
                        }).then(function (response) {
                            scope.items = [];
                            _.forEach(response.data.data, function (item) {
                                scope.items.push({
                                    value: item.user.objectId,
                                    label: item.user.name
                                });
                            });
                        });

                    } else if (config == 'OrganizationManager') {   // 部门负责人
                        $http({
                            url: PageContext.path + '/workflow/task/fetchHander/organizationManager',
                            method: 'GET',
                            params: {organizationCode: attrs.organizationCode}
                        }).then(function (response) {
                            scope.items = [];
                            _.forEach(response.data.data, function (item) {
                                scope.items.push({
                                    value: item.objectId,
                                    label: item.name
                                });
                            });
                        });

                    } else if (config == 'Leader') {   // 分管领导
                        $http({
                            url: PageContext.path + '/workflow/task/fetchHander/leader',
                            method: 'GET'
                        }).then(function (response) {
                            scope.items = [];
                            _.forEach(response.data.data, function (item) {
                                scope.items.push({
                                    value: item.objectId,
                                    label: item.name
                                });
                            });
                        });
                    } else if (config == 'Boss') {   // 院长
                        $http({
                            url: PageContext.path + '/workflow/task/fetchHander/boss',
                            method: 'GET'
                        }).then(function (response) {
                            scope.items = [];
                            _.forEach(response.data.data, function (item) {
                                scope.items.push({
                                    value: item.objectId,
                                    label: item.name
                                });
                            });
                        });
                    }
                }
            }
        }
    })
;