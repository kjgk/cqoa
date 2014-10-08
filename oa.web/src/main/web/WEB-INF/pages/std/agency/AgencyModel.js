Ext.define('withub.ext.std.agency.AgencyModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'owner',
        'agent',
        'startTime',
        'endTime',
        'status',
        'statusTag',
        'creator'
    ]
});