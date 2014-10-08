Ext.define('withub.ext.std.systemEvent.SystemEventModel', {
    extend:'Ext.data.Model',
    fields:[
        'objectId',
        'name',
        'className',
        'shouldSerializable',
        'entityProperty',
        'enableNotify',
        'priority',
        'delay',
        'intervalValue',
        'intervalTimeUnit',
        'retrySendCount',
        'accepterServiceMethod',
        'userProperty',
        'description',
        'orderNo'
    ]
});

