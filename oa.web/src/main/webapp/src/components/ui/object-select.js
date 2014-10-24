'use strict';

angular.module('withub.ui')
    .directive('objectSelect', function ($http) {
        return {
            templateUrl: 'components/ui/template/object-select.html',
            restrict: 'EA',
            scope: {
                ngModel: '='
            },
            link: function (scope, element, attrs) {
                scope.multiple = attrs.multiple !== undefined;
                scope.placeholder = attrs.placeholder;
                var entity = attrs.entity;
                var queryProperty = attrs.queryProperty;
                scope.doSearch = function (field) {
                    scope.items = [];
                    $http({
                        url: PageContext.path + '/system/metadata/searchEntity',
                        params: {
                            entity: entity,
                            queryProperty: queryProperty || '',
                            keyword: field.search
                        }
                    }).then(function (response) {
                        var selected = [];
                        if (scope.ngModel !== undefined && _.isArray(scope.ngModel)) {
                            selected = _.pluck(scope.ngModel, 'value');
                        }
                        _.each(response.data.items, function (item) {
                            if (!_.contains(selected, item.value)) {
                                scope.items.push(item);
                            }
                        });
                    });
                };
            }
        };
    })
;
