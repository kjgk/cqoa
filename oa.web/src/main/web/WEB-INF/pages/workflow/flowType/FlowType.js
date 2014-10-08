Ext.define('withub.ext.workflow.flowType.FlowType', {
    extend: 'withub.ext.common.Window',
    title: '流程类型',
    width: 650,
    baseUrl: '/workflow/flowType',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 100,
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
                    name: 'flowTypeTag',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    selectType: 'Entity',
                    name: 'entity.objectId',
                    emptyText: '请选择实体',
                    allowBlank: false
                },
                {
                    fieldLabel: '子流程表达式规则',
                    xtype: 'textarea',
                    name: 'subFlowTypeExpression',
                    maxLength: 1000,
                    height: 120,
                    allowBlank: true
                },
                {
                    fieldLabel: '编号',
                    name: 'code',
                    maxLength: 30,
                    allowBlank: true
                },
                {
                    fieldLabel: '流程名',
                    name: 'instanceName',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '状态属性',
                    name: 'statusProperty',
                    value: 'status',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '表单页面',
                    name: 'url',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '启用',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'enable', inputValue: 1, checked: true},
                        {boxLabel: '否', name: 'enable', inputValue: 0}
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '跳过上次执行通过的处理人',
                    columns: 6,
                    items: [
                        {boxLabel: '否', name: 'skipHandler', inputValue: 0, checked: true},
                        {boxLabel: '全部', name: 'skipHandler', inputValue: 1},
                        {boxLabel: '部分', name: 'skipHandler', inputValue: 2}
                    ]
                },
                {
                    fieldLabel: '启动入口',
                    name: 'entranceMethod',
                    maxLength: 60,
                    allowBlank: false
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

