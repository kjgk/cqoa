Ext.define('withub.ext.system.code.Code', {
    extend: 'withub.ext.common.Window',
    title: '代码',
    baseUrl: '/system/code',

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
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'codeTag',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '默认值',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'defaultValue', inputValue: 1},
                        {boxLabel: '否', name: 'defaultValue', inputValue: 0, checked: true}
                    ]
                },
                {
                    fieldLabel: '状态值',
                    name: 'statusValue',
                    maxLength: 30
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 100,
                    maxLength: 500
                },
                {
                    xtype: 'hidden',
                    name: 'codeColumn.objectId',
                    value: this.codeColumnId
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

