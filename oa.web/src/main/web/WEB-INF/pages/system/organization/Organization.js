Ext.define('withub.ext.system.organization.Organization', {
    extend: 'withub.ext.common.Window',
    title: '组织机构',
    baseUrl: '/system/organization',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 85,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '上级组织',
                    url: '/system/organization/loadTree',
                    name: 'parent.objectId',
                    value: this.parentId,
                    allowBlank: this.depth == 1,
                    hidden: this.depth == 1
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '类型',
                    name: 'organizationType.objectId',
                    codeColumnTag: 'OrganizationType',
                    defaultCodeTag: 'Department',
                    emptyText: '请选择类型',
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'nodeTag',
                    maxLength: 30
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '备注',
                    name: 'description',
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

