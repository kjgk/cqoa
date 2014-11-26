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

                    if (attrs['userGroupTag']) {
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
                    }
                }
            }
        }
    })

;