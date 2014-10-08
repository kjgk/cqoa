Ext.define('withub.ext.system.userInterface.UserInterface', {
    extend: 'withub.ext.common.Window',
    title: '用户接口',
    baseUrl: '/system/userInterface',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 100,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '用户接口分类',
                    url: '/system/userInterface/loadTree',
                    params: {depth: 1},
                    name: 'userInterfaceCategory.objectId',
                    value: this.userInterfaceCategoryId ? 'UserInterfaceCategory_' + this.userInterfaceCategoryId : undefined,
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
                    name: 'userInterfaceTag',
                    maxLength: 30,
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

