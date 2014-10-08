Ext.define('withub.ext.std.workCalendar.WorkCalendarModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'defaultCalendar',
        'weekendHoliday',
        'forenoonStartTime',
        'forenoonEndTime',
        'afternoonStartTime',
        'afternoonEndTime',
        'dayHours'
    ]
});