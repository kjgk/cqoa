Ext.define('withub.ext.std.fileTemplate.FileTemplate', {
    extend: 'withub.ext.base.BaseWindow',
    title: '文件模板',
    width: 780,
    baseUrl: '/std/fileTemplate',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '模板文件',
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
                    fieldLabel: '标识',
                    name: 'fileTemplateTag',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '配置',
                    name: 'configuration',
                    height: 460,
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

