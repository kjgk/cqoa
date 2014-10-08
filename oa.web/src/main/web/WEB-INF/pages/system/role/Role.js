Ext.define('withub.ext.system.role.Role', {
    extend: 'withub.ext.common.Window',
    title: '角色',
    baseUrl: '/system/role',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 50,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'roleTag',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    maxLength: 1000,
                    allowBlank: true,
                    height: 150
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

