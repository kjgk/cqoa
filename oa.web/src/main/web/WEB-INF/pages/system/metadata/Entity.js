Ext.define('withub.ext.system.metadata.Entity', {
    extend: 'withub.ext.common.Window',
    title: '实体',
    width: 550,
    baseUrl: '/system/entity',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 105,
                anchor: '100%'
            },
            trackResetOnLoad: true,
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体分类',
                    url: '/system/metadata/loadTree',
                    params: {depth: 1},
                    name: 'entityCategory.objectId',
                    value: this.action == 'create' ? "EntityCategory" + ExtUtil.TREE_NODE_SPLIT + this.entityCategoryId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '实体名',
                    name: 'entityName',
                    vtype: 'unique',
                    vtypeEntity: 'Entity',
                    vtypeProperty: 'entityName',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '表名',
                    name: 'tableName',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '类名',
                    name: 'className',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '实体类型',
                    name: 'entityType.objectId',
                    codeColumnTag: 'EntityType',
                    allowBlank: false
                },
                {
                    fieldLabel: '记录标识',
                    name: 'identifierTemplate',
                    maxLength: 200,
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '允许授权',
                    columns: 4,
                    allowBlank: false,
                    items: [
                        {boxLabel: '是', name: 'allowAuthorization', inputValue: 1},
                        {boxLabel: '否', name: 'allowAuthorization', inputValue: 0, checked: true}
                    ]
                },
                {
                    fieldLabel: '默认排序属性',
                    name: 'defaultOrderProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '默认排序方式',
                    name: 'defaultOrderType',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '排序字段依赖属性',
                    name: 'orderNoDependProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '全文检索',
                    columns: 4,
                    items: [
                        { boxLabel: '启用', name: 'enableFullTextSearch', inputValue: 1 },
                        { boxLabel: '禁用', name: 'enableFullTextSearch', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '全文检索栏目',
                    itemId: 'fullTextSearchColumn',
                    name: 'fullTextSearchColumn.objectId',
                    codeColumnTag: 'fullTextSearchColumn'
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '全文检索权限',
                    columns: 4,
                    items: [
                        { boxLabel: '启用', name: 'fullTextSearchPermission', inputValue: 1 },
                        { boxLabel: '禁用', name: 'fullTextSearchPermission', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '扩展全文检索',
                    columns: 4,
                    items: [
                        { boxLabel: '启用', name: 'extendFullTextSearch', inputValue: 1 },
                        { boxLabel: '禁用', name: 'extendFullTextSearch', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '附件全文检索',
                    columns: 4,
                    items: [
                        { boxLabel: '启用', name: 'attachmentFullTextSearch', inputValue: 1 },
                        { boxLabel: '禁用', name: 'attachmentFullTextSearch', inputValue: 0, checked: true}
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

