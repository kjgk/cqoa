Ext.define('withub.ext.system.code.CodeColumn', {
    extend: 'withub.ext.common.Window',
    title: '代码栏目',
    baseUrl: '/system/codeColumn',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            trackResetOnLoad: true,
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '栏目分类',
                    url: '/system/code/loadTree',
                    params: {depth: 2},
                    name: 'codeColumnCategory.objectId',
                    value: this.action == 'create' ? 'CodeColumnCategory_' + this.codeColumnCategoryId : undefined
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'codeColumnTag',
                    vtype: 'unique',
                    vtypeEntity: 'CodeColumn',
                    vtypeProperty: 'codeColumnTag',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '选择模式',
                    name: 'selectMode',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '状态模式',
                    name: 'statusMode',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '显示规则',
                    name: 'displayRegulation',
                    height: 150,
                    maxLength: 500
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '备注',
                    name: 'description',
                    height: 100,
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

