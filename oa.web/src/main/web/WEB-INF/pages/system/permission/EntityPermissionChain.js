Ext.define('withub.ext.system.permission.EntityPermissionChain', {
    extend: 'withub.ext.common.Window',
    title: '实体权限控制链',
    baseUrl: '/system/entityPermissionChain',
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
                    xtype: 'treecombo',
                    fieldLabel: '上级链',
                    url: '/system/entityPermissionChain/loadTree',
                    name: 'parent.objectId',
                    value: this.action == 'create' ? this.parentId : undefined,
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    selectType: 'Entity',
                    name: 'entity.objectId',
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '依赖属性',
                    name: 'dependProperty',
                    maxLength: 100,
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

