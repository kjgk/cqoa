Ext.define('withub.ext.std.law.LawIssueOrganization', {
    extend: 'withub.ext.common.Window',
    title: '发布机构',
    baseUrl: '/std/lawIssueOrganization',
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
                    url: '/std/lawIssueOrganization/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.parentId
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

