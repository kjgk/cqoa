Ext.define('withub.ext.system.menu.Menu', {
    extend: 'withub.ext.common.Window',
    title: '菜单',
    width: 720,
    baseUrl: '/system/menu',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 95,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '上级菜单',
                    url: '/system/menu/loadTree',
                    name: 'parent.objectId',
                    value: this.action == 'create' ? this.parentId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '权限等级',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '超级管理员', name: 'permission', inputValue: 1},
                        {boxLabel: '管理员', name: 'permission', inputValue: 2},
                        {boxLabel: '授权', name: 'permission', inputValue: 3, checked: true},
                        {boxLabel: '登录', name: 'permission', inputValue: 4},
                        {boxLabel: '公开', name: 'permission', inputValue: 5}
                    ]
                },

                {
                    fieldLabel: 'URL',
                    name: 'url',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '子菜单回调方法',
                    name: 'subMenuMethod',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '图标',
                    name: 'image',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '展开',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'expand', inputValue: 1, checked: true},
                        {boxLabel: '否', name: 'expand', inputValue: 0}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '显示',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'visible', inputValue: 1, checked: true},
                        {boxLabel: '否', name: 'visible', inputValue: 0}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '显示模式',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '导航', name: 'openMode', inputValue: 1, checked: true},
                        {boxLabel: '弹出', name: 'openMode', inputValue: 0},
                        {boxLabel: '新窗口', name: 'openMode', inputValue: 2}
                    ]
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

