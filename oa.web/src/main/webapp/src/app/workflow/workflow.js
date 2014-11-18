'use strict';

angular.module('app.workflow', [])

    .factory('WorkflowService', function ($http) {
        return {

            listFlowType: function () {
                return $http({
                    url: PageContext.path + '/workflow/flowType/list',
                    method: 'GET'
                });
            }
        }
    })

    .directive('flowTypeSelect', function (WorkflowService) {
        return {
            templateUrl: 'app/workflow/common/flow-type-select.html',
            restrict: 'EA',
            replace: false,
            scope: true,
            compile: function (elenemt, attrs) {

                return function (scope, element, attrs) {
                    scope.placeholder = attrs.placeholder;
                    scope.showAll = attrs.showAll !== undefined && attrs.showAll !== false;
                    scope.showNone = attrs.showNone !== undefined && attrs.showNone !== false;
                    WorkflowService.listFlowType().then(function (response) {
                        scope.items = response.data.items;
                    });
                }
            }
        }
    })
;