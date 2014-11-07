'use strict';

angular.module('withub.ui')

    .directive('datePicker', function (uuid4) {
        return {
            templateUrl: 'components/ui/template/date-picker.html',
            restrict: 'EA',
            replace: true,
            scope: {
                ngModel: '='
            },
            compile: function (elenemt, attrs) {

                var gid = uuid4.generate();
                var datetimePickerConfig = {
                    dropdownSelector: '#' + gid,
                    minView: 'day'
                };
                var format = 'YYYY-MM-DD';

                elenemt.find('a.dropdown-toggle').attr('id', gid);
                elenemt.find('input[date-time-input]').attr('date-time-input', format).attr('placeholder', attrs.placeholder);
                elenemt.find('datetimepicker').attr('datetimepicker-config', angular.toJson(datetimePickerConfig));

                return function (scope, $element, attrs) {
                }

            }
        }
    })

;