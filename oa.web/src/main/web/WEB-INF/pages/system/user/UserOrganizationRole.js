Ext.define('withub.ext.system.user.UserOrganizationRole', {
    extend: 'Ext.Window',
    width: 480,
    resizable: false,
    modal: true,
    title: '用户角色',
    initComponent: function () {
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
                    itemId: 'organization',
                    fieldLabel: '组织机构',
                    xtype: 'treecombo',
                    url: '/system/organization/loadTree',
                    showPathName: true,
                    pathNameDepth: 3,
                    emptyText: '请选择组织机构'
                },
                {
                    itemId: 'role',
                    fieldLabel: '角色',
                    xtype: 'simplecombo',
                    entity: 'Role',
                    order: 'orderNo',
                    emptyText: '请选择角色',
                    allowBlank: false
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.buttons = [
            {
                text: '确定',
                handler: function () {
                    var form = this.formPanel.getForm();
                    var organization = this.down('#organization');
                    var role = this.down('#role');
                    if (!form.isValid()) {
                        return;
                    }
                    this.fireEvent('input', organization.getValue(), organization.getRawValue(), role.getValue(), role.getRawValue());
                    this.close();
                },
                scope: this
            },
            {
                text: '取消',
                handler: this.close,
                scope: this
            }
        ];

        this.callParent();
    },

    setData: function (record) {
        this.down('#organization').setValue(record.get('organizationId'));
        this.down('#role').setValue(record.get('roleId'));
    }
});