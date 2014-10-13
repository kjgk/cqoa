'use strict';

angular.module('withub.filter', [])
    .filter('$date', function ($filter) {
        return function (input, format) {
            if (input) {
                format = format || 'yyyy-MM-dd HH:mm';
                if (_.isNumber(input)) {
                    return $filter('date')(input, format);
                } else {
                    return $filter('date')(input.time, format);
                }
            }
        };
    })
;