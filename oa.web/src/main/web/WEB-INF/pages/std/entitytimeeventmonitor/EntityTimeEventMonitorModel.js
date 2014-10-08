Ext.define('withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitorModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'entity',
        'expiredEvent',
        'entityTimeProperty',
        'entityTimePropertyIsExpired',
        'expiredTimeValue',
        'expiredTimeValueTimeUnit',
        'eventClassName',
        'useWorkCalendar',
        'startTime',
        'endTime',
        'intervalValue',
        'priority',
        'enable'
    ]
});