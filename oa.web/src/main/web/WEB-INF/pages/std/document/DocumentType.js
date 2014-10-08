Ext.define('withub.ext.std.document.DocumentType', {
    extend: 'withub.ext.base.BaseWindow',
    title: '文档类型',
    baseUrl: '/std/documentType',
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
                    fieldLabel: '文档分类',
                    url: '/std/documentType/loadTree',
                    params: {depth: 1},
                    name: 'documentTypeCategory.objectId',
                    value: this.action == 'create' ? 'DocumentTypeCategory_' + this.documentTypeCategoryId : undefined
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'documentTypeTag',
                    maxLength: 30
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 180,
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

