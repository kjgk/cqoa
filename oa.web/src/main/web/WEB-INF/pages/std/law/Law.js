Ext.define('withub.ext.std.law.Law', {
    extend: 'withub.ext.common.Window',
    title: '法律法规',
    width: 760,
    baseUrl: '/std/law',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 90,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '法律法规分类',
                    url: '/std/law/loadTree',
                    name: 'lawCategory.objectId',
                    allowBlank: false,
                    value: this.lawCategoryId == undefined ? undefined : 'LawCategory_' + this.lawCategoryId
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '发布机构',
                    name: 'issueOrganization',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    fieldLabel: '发布时间',
                    name: 'issueDate'
                },
                {
                    xtype: 'htmleditor',
                    enableFont: false,
                    enableFontSize: false,
                    enableSourceEdit: false,
                    name: 'content',
                    emptyText: '请输入内容',
                    allowBlank: false,
                    height: 520,
                    flex: 1
                },
                {
                    fieldLabel: '附件',
                    xtype: 'swfuploadfield',
                    name: 'attachmentInfo',
                    listeners: {
                        filequeued: function (file) {
                            this.formPanel.getForm().findField('name').setValue(file.name.replace(file.type, ''));
                        },
                        scope: this
                    },
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

