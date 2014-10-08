Ext.define('withub.ext.std.industry.Industry', {
    extend: 'withub.ext.common.Window',
    title: '行业分类',
    baseUrl: '/std/industry',
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
            trackResetOnLoad: true,
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '上级行业',
                    url: '/std/industry/loadTree',
                    name: 'parent.objectId',
                    value: this.action == 'create' ? this.parentId : undefined
                },
                {
                    fieldLabel: '行业名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 150,
                    maxLength: 1000,
                    allowBlank: true
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

