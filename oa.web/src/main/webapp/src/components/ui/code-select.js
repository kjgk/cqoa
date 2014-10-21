'use strict';

angular.module('withub.ui')

    .directive('codeSelect', function ($http) {
        return {
            templateUrl: 'components/ui/template/code-select.html',
            restrict: 'EA',
            replace: true,
            scope: true,
            link: function (scope, element, attrs) {
                var config = attrs.codeSelect;
                var codeColumnTag = '';
                if (_.isString(config)) {
                    codeColumnTag = config;
                }
                $http({
                    url: PageContext.path + '/system/code/listCodeByCodeColumn',
                    params: {
                        codeColumnTag: codeColumnTag
                    },
                    method: 'GET'
                }).then(function (response) {
                    scope.items = response.data.items;
                });
            }
        }
    })

;