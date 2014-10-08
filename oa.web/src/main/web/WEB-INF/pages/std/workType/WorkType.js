Ext.define('withub.ext.std.workType.WorkType', {
    extend: 'withub.ext.base.BaseWindow',
    title: '工种类型',
    baseUrl: '/std/workType',
    initComponent: function() {
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
                    fieldLabel: '工种分类',
                    url: '/std/workType/loadTree',
                    params: {depth: 1},
                    name: 'workTypeCategory.objectId',
                    value: this.action == 'create' ? 'WorkTypeCategory_' + this.workTypeCategoryId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'code',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 190,
                    maxLength: 500
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

