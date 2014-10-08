Ext.define('withub.ext.std.blacklist.Blacklist', {
    extend: 'withub.ext.base.BaseWindow',
    title: '黑名单',
    baseUrl: '/std/blacklist',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'usersearchcombo',
                    fieldLabel: '姓名',
                    name: 'user.objectId',
                    allowBlank: false,
                    readOnly: this.action == 'update'
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '原因',
                    name: 'description',
                    height: 150,
                    allowBlank: false
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

