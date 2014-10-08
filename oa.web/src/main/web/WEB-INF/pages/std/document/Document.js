Ext.define('withub.ext.std.document.Document', {
    extend: 'withub.ext.common.Window',
    title: '文档',
    baseUrl: '/std/document',
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
                    fieldLabel: '文档类型',
                    url: '/std/documentType/loadTree',
                    params: {depth: 2},
                    selectType: 'DocumentType',
                    name: 'document.documentType.objectId',
                    allowBlank: false,
                    value: this.documentTypeId == undefined ? undefined : 'DocumentType_' + this.documentTypeId
                },
                {
                    fieldLabel: '文档',
                    xtype: 'swfuploadfield',
                    name: 'attachmentInfo',
                    listeners: {
                        filequeued: function (file) {
                            this.formPanel.getForm().findField('name').setValue(file.name.replace(file.type, ''));
                        },
                        scope: this
                    },
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '关键字',
                    name: 'keyword',
                    maxLength: 100
                },
                {
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    fieldLabel: '编写日期',
                    value: new Date(),
                    name: 'writeDate'
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 150,
                    maxLength: 500
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                },
                {
                    xtype: 'hidden',
                    name: 'document.objectId',
                    value: this.documentId
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

