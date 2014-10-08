Ext.define('withub.ext.system.userInterface.UserInterfaceMenu', {
    extend: 'withub.ext.common.Window',
    title: '用户接口菜单',
    baseUrl: '/system/userInterfaceMenu',
    initComponent: function() {
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
                    fieldLabel: '用户接口',
                    url: '/system/userInterface/loadTree',
                    params: {depth: 2},
                    selectType: 'UserInterface',
                    name: 'userInterface.objectId',
                    value:  'UserInterface_' + this.userInterfaceId,
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '权限',
                    url: '/system/permission/loadTree',
                    params: {depth: 3},
                    selectType: 'Permission',
                    name: 'permission.objectId',
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'menuTag',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '类型',
                    columns: 5,
                    items: [
                        {boxLabel: '上下文', name: 'menuType', inputValue: 1,checked: true},
                        {boxLabel: '工具栏', name: 'menuType', inputValue: 2}
                    ],
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

