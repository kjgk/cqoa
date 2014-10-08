Ext.define('withub.ext.system.metadata.Property', {
    extend: 'withub.ext.common.Window',
    title: '实体属性',
    width: 550,
    baseUrl: '/system/property',
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
                    fieldLabel: '实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 3},
                    name: 'entity.objectId',
                    value: this.action == 'create' ? "Entity" + ExtUtil.TREE_NODE_SPLIT + this.entityId : undefined,
                    selectType: 'Entity',
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '属性名',
                    name: 'propertyName',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '字段名',
                    name: 'columnName',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '属性类型',
                    name: 'propertyType.objectId',
                    codeColumnTag: 'EntityPropertyType',
                    emptyText: '请选择属性类型',
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '数据类型',
                    name: 'propertyDataType.objectId',
                    codeColumnTag: 'PropertyDataType',
                    emptyText: '请选择数据类型',
                    allowBlank: false
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '属性数据长度',
                    minValue: 1,
                    value: 36,
                    name: 'dataLength',
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体对象',
                    itemId: 'propertEntity',
                    url: '/system/metadata/loadTree',
                    params: {depth: 3},
                    enableClear: true,
                    name: 'propertEntity.objectId'
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '代码栏目',
                    itemId: 'propertCodeColumn',
                    editable: true,
                    url: '/system/code/loadTree',
                    params: {depth: 2},
                    selectType: 'CodeColumn',
                    enableClear: true,
                    name: 'propertCodeColumn.objectId'
                },
                {
                    fieldLabel: '全文检索别名',
                    name: 'fullTextSearchAlias',
                    maxLength: 100
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '语言的',
                    columns: 2,
                    items: [
                        { boxLabel: '启用', name: 'language', inputValue: 1 },
                        { boxLabel: '禁用', name: 'language', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '全文检索',
                    columns: 2,
                    items: [
                        { boxLabel: '启用', name: 'enableFullTextSearch', inputValue: 1 },
                        { boxLabel: '禁用', name: 'enableFullTextSearch', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '全文检索是否在文件域',
                    columns: 2,
                    items: [
                        { boxLabel: '是', name: 'fullTextSearchField', inputValue: 1 },
                        { boxLabel: '否', name: 'fullTextSearchField', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    itemId: 'description',
                    name: 'description',
                    height: 150,
                    maxLength: 1000,
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

