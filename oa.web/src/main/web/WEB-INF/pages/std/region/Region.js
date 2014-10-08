Ext.define('withub.ext.std.region.Region', {
    extend: 'withub.ext.common.Window',
    title: '地理区域',
    baseUrl: '/std/region',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            trackResetOnLoad: true,
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '上级区域',
                    url: '/std/region/loadTree',
                    name: 'parent.objectId',
                    value: this.action == 'create' ? this.parentId : undefined
                },
                {
                    fieldLabel: '区域名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '区域标识',
                    name: 'regionTag',
                    vtype: 'unique',
                    vtypeEntity: 'Region',
                    vtypeProperty: 'nodeTag',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '区域类型',
                    name: 'regionType.objectId',
                    codeColumnTag: 'RegionType',
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
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

