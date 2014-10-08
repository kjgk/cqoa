Ext.define('withub.ext.std.sms.SmsModel', {
    extend: 'Ext.data.Model',
    fields: [
        'recipient',
        'text',
        'createDate',
        'originator',
        'name',
        'statusReport',
        'sendDate',
        'refNo',
        'status',
        'errors'
    ]
});