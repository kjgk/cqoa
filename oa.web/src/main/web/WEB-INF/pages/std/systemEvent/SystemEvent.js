Ext.define('withub.ext.std.systemEvent.SystemEvent', {
    extend: 'withub.ext.common.Window',
    title: '系统事件',
    width: 500,
    baseUrl: '/std/systemEvent',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 90,
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
                    fieldLabel: '类名',
                    name: 'className',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '是否序列化',
                    xtype: 'radiogroup',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'shouldSerializable', inputValue: 1},
                        {boxLabel: '否', name: 'shouldSerializable', inputValue: 0, checked: true}
                    ]

                },
                {
                    fieldLabel: '实体属性',
                    name: 'entityProperty',
                    maxLength: 100
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '启用通知',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'enableNotify', inputValue: 1},
                        {boxLabel: '否', name: 'enableNotify', inputValue: 0, checked: true}
                    ]
                },
                {

                    xtype: 'numberfield',
                    fieldLabel: '优先级',
                    name: 'priority',
                    value: 0,
                    minValue: 0,
                    allowDecimals: false,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '延迟',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'delay', inputValue: 1},
                        {boxLabel: '否', name: 'delay', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '重发周期',
                    name: 'intervalValue',
                    value: 0,
                    minValue: 0,
                    allowDecimals: false,
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '时间单位',
                    name: 'intervalTimeUnit.objectId',
                    codeColumnTag: 'TimeUnit',
                    codeTags: ['Day', 'Hour'],
                    allowBlank: false
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '尝试发送次数',
                    name: 'retrySendCount',
                    value: 0,
                    minValue: 0,
                    allowDecimals: false,
                    allowBlank: false
                },
                {
                    fieldLabel: '接受人方法',
                    name: 'accepterServiceMethod',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '用户属性',
                    name: 'userProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '描述',
                    xtype: 'textarea',
                    name: 'description',
                    maxLength: 240,
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

