'use strict';

angular.module('withub.common', [])
    .constant('DateFormat', {
        DAY: 'YYYY-MM-DD',
        HOUR: 'YYYY-MM-DD HH',
        MINUTE: 'YYYY-MM-DD HH:MM',
        SECOND: 'YYYY-MM-DD HH:MM:ss',
        TIMESTAMP: 'YYYY-MM-DD HH:MM:ss.SSS'
    })
;