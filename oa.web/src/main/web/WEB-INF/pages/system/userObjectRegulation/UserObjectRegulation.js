Ext.define('withub.ext.system.userObjectRegulation.UserObjectRegulation', {
    extend: 'withub.ext.common.Window',
    title: '用户对象规则',
    baseUrl: '/system/userObjectRegulation',
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
                    fieldLabel: '上级规则',
                    url: '/system/userObjectRegulation/loadTree',
                    name: 'parent.objectId',
                    value: this.action == 'create' ? this.parentId : undefined,
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
                    name: 'regulationTag',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    fieldLabel: '用户属性',
                    name: 'userProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    name: 'entity.objectId',
                    allowBlank: true
                },
                {
                    fieldLabel: '代码栏目标识',
                    name: 'codeColumnTag',
                    xtype: 'textfield',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '依赖实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    name: 'dependEntity.objectId',
                    allowBlank: true
                },
                {
                    fieldLabel: '依赖属性',
                    name: 'dependProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '上级依赖属性',
                    name: 'parentDependProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '要求上级模板节点',
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'requiredParentNode', inputValue: 1},
                        {boxLabel: '否', name: 'requiredParentNode', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '自定义规则',
                    name: 'regulation',
                    height: 150
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

