Ext.define('withub.ext.std.systemEvent.NotifyQueueModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'systemEvent',
        'entityInstanceId',
        'notifyServiceType',
        'enterTime',
        'title',
        'lastSendTime',
        'sendCount',
        'result',
        'status'
    ]
});