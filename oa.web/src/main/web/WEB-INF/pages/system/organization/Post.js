Ext.define('withub.ext.system.organization.Post', {
    extend: 'withub.ext.common.Window',
    title: '岗位',
    baseUrl: '/system/post',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 60,
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
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 190,
                    maxLength: 500
                },
                {
                    xtype: 'hidden',
                    name: 'organization.objectId',
                    value: this.organizationId
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

