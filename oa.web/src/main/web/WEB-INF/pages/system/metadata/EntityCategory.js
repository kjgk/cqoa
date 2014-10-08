Ext.define('withub.ext.system.metadata.EntityCategory', {
    extend: 'withub.ext.common.Window',
    title: '实体分类',
    baseUrl: '/system/entityCategory',
    initComponent: function() {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '上级分类',
                    url: '/system/metadata/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.action == 'create' ? 'EntityCategory_' + this.parentId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height:150,
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

