'use strict';

angular.module('withub.common', [])
    .constant('DateFormat', {
        DAY: 'YYYY-MM-DD',
        HOUR: 'YYYY-MM-DD HH',
        MINUTE: 'YYYY-MM-DD HH:mm',
        SECOND: 'YYYY-MM-DD HH:mm:ss',
        TIMESTAMP: 'YYYY-MM-DD HH:mm:ss.SSS'
    })
;