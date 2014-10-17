'use strict';

angular.module('withub.ui')
    .directive('userSelect', function ($http) {
        return {
            templateUrl: 'components/ui/template/user-select.html',
            restrict: 'EA',
            scope: {
                ngModel: '='
            },
            link: function (scope, element, attrs) {
                scope.multiple = attrs.multiple !== undefined;
                scope.placeholder = attrs.placeholder;
                scope.searchUser = function (field) {
                    scope.users = [];
                    $http({
                        url: PageContext.path + '/system/user/search',
                        params: {
                            keyword: field.search
                        }
                    }).then(function (response) {
                        var selected = [];
                        if (scope.ngModel !== undefined && _.isArray(scope.ngModel)) {
                            selected = _.pluck(scope.ngModel, 'objectId');
                        }
                        _.each(response.data.items, function (item) {
                            if (!_.contains(selected, item.objectId)) {
                                scope.users.push(item);
                            }
                        });
                    });
                };
            }
        };
    })
;
