Ext.define('withub.ext.system.code.CodeColumnCategory', {
    extend: 'withub.ext.common.Window',
    title: '代码栏目分类',
    baseUrl: '/system/codeColumnCategory',
    initComponent: function() {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
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
                    url: '/system/code/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.action == 'create' ? 'CodeColumnCategory_' + this.parentId : undefined
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '备注',
                    name: 'description',
                    height: 190,
                    maxLength: 500
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

