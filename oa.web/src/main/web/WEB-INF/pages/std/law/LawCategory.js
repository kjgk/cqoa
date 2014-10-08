Ext.define('withub.ext.std.law.LawCategory', {
    extend: 'withub.ext.common.Window',
    title: '法律法规分类',
    baseUrl: '/std/lawCategory',
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
                    xtype: 'treecombo',
                    fieldLabel: '上级分类',
                    url: '/std/law/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.action == 'create' ? 'LawCategory_' + this.parentId : undefined
                },
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
                    height: 150,
                    maxLength: 1000
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

