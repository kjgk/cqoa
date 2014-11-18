'use strict';

angular.module('withub.ui')

    .directive('codeSelect', function ($http) {
        return {
            templateUrl: 'components/ui/template/code-select.html',
            restrict: 'EA',
            replace: false,
            scope: true,
            compile: function (elenemt, attrs) {

                return function (scope, element, attrs, ngModel) {
                    var config = attrs.codeSelect;
                    scope.placeholder = attrs.placeholder;
                    scope.showAll = attrs.showAll !== undefined && attrs.showAll !== false;
                    scope.showNone = attrs.showNone !== undefined && attrs.showNone !== false;
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
        }
    })

;