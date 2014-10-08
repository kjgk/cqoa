Ext.define('withub.ext.system.permission.Permission', {
    extend: 'withub.ext.common.Window',
    title: '权限',
    width: 350,
    baseUrl: '/system/permission',
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
                    fieldLabel: '实体',
                    url: '/system/permission/loadTree',
                    params: {depth: 2},
                    name: 'entity.objectId',
                    value: this.action == 'create' ? "Entity" + "_" + this.entityId : undefined,
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
                    name: 'permissionTag',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    fieldLabel: '业务方法',
                    name: 'serviceMethod',
                    maxLength: 60,
                    allowBlank: true
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '菜单',
                    url: '/system/menu/loadTree',
                    name: 'menu.objectId',
                    enableClear: true,
                    listeners: {
                        beforeselect: function (nodeId, objectId, nodeType, record, index) {
                            return record.get('leaf');
                        }
                    }
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


