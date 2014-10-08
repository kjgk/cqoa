Ext.define('withub.ext.std.systemEvent.EventNotifyServiceTypeModel', {
    extend:'Ext.data.Model',
    fields:[
        'objectId',
        'systemEvent',
        'notifyServiceType',
        'titleTemplate',
        'contentTemplate',
        'defaultTemplate',
        'orderNo'
    ],
    idProperty:'objectId'
});