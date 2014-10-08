Ext.define('withub.ext.std.server.Server', {
    extend: 'withub.ext.common.Window',
    title: '服务器',
    baseUrl: '/std/server',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: 'IP',
                    name: 'ip',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '用户名',
                    name: 'username',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '密码',
                    name: 'password',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '本机',
                    allowBlank: false,
                    items: [
                        {boxLabel: '是', name: 'localhost', inputValue: 1},
                        {boxLabel: '否', name: 'localhost', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

