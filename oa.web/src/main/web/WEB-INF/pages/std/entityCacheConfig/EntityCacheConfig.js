Ext.define('withub.ext.std.entityCacheConfig.EntityCacheConfig', {
    extend: 'withub.ext.base.BaseWindow',
    title: '缓存配置',
    width: 600,
    baseUrl: '/std/entityCacheConfig',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            trackResetOnLoad: true,
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
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
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '缓存关键字',
                    name: 'cacheKey',
                    vtype: 'unique',
                    vtypeEntity: 'EntityCacheConfig',
                    vtypeProperty: 'cacheKey',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '时间戳属性',
                    name: 'timestampProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '缓存数量',
                    name: 'cacheCount',
                    allowDecimals: false,
                    minValue: 0,
                    value: 6,
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '文档类型',
                    url: '/std/documentType/loadTree',
                    params: {depth: 2},
                    selectType: 'DocumentType',
                    name: 'documentType.objectId',
                    allowBlank: true
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '附加条件',
                    name: 'addition',
                    allowBlank: true,
                    height: 150
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '启用',
                    allowBlank: false,
                    items: [
                        {boxLabel: '是', name: 'enable', inputValue: 1},
                        {boxLabel: '否', name: 'enable', inputValue: 0, checked: true}
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
})
;

