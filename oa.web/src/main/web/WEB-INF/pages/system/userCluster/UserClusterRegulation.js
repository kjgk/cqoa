Ext.define('withub.ext.system.userCluster.UserClusterRegulation', {
    extend: 'withub.ext.common.Window',
    title: '用户簇规则模版',
    baseUrl: '/system/userClusterRegulation',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 140,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
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
                    fieldLabel: '代码栏目标识',
                    name: 'codeColumnTag',
                    maxLength: 100
                },
                {
                    fieldLabel: '用户属性',
                    name: 'userProperty',
                    maxLength: 100
                },
                {
                    fieldLabel: '扩展角色属性',
                    name: 'extandedRoleProperty',
                    maxLength: 100
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '扩展属性实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    selectType: 'Entity',
                    enableClear: true,
                    name: 'extandedPropertyEntity.objectId'
                },
                {
                    fieldLabel: '扩展属性代码栏目标识',
                    name: 'extandedPropertyCodeColumnTag',
                    maxLength: 100
                },
                {
                    xtype: 'hidden',
                    name: 'parent.objectId',
                    value: this.parentId
                },
                {
                    xtype: 'hidden',
                    name: 'userCluster.objectId',
                    value: this.userClusterId
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

