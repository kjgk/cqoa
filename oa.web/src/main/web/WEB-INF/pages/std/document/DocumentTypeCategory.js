Ext.define('withub.ext.std.document.DocumentTypeCategory', {
    extend: 'withub.ext.common.Window',
    title: '文档分类',
    baseUrl: '/std/documentTypeCategory',
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
                    url: '/std/documentType/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.action == 'create' ? 'DocumentTypeCategory_' + this.parentId : undefined
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'nodeTag',
                    maxLength: 30
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
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

