Ext.define('withub.ext.std.sms.SmsTest', {
    extend: 'withub.ext.common.Window',
    title: '短信测试',
    width: 530,
    button1Text: '发送',
    baseUrl: '/std/smsTest',

    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 70,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '手机号码',
                    maxLength: 11,
                    name: 'recipient',
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '短信内容',
                    height: 150,
                    name: 'text',
                    maxLength: 1000,
                    allowBlank: false
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});