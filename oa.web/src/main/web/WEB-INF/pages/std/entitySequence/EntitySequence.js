Ext.define('withub.ext.std.entitySequence.EntitySequence', {
    extend: 'withub.ext.common.Window',
    title: '实体序号',
    width: 720,
    baseUrl: '/std/entitySequence',
    initComponent: function () {

        this.regulationList = Ext.create('withub.ext.base.Grid', {
            height: 150,
            store: Ext.create('Ext.data.ArrayStore', {
                fields: ['objectId', 'name', 'regulationExpression']
            }),
            columns: [
                {xtype: 'rownumberer', width: 32, locked: true},
                {text: '名称', width: 100, dataIndex: 'name'},
                {text: '规则表达式', minWidth: 240, flex: 1, dataIndex: 'regulationExpression'}
            ],
            tbar: [
                {
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.entitySequence.EntitySequenceRegulation', {
                            listeners: {
                                success: function (data) {
                                    this.regulationList.getStore().loadData([data], this);
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var records = this.regulationList.getSelectionModel().getSelection();
                        if (records.length > 0) {
                            var record = records[0];
                            Ext.create('withub.ext.std.entitySequence.EntitySequenceRegulation', {
                                record: record,
                                listeners: {
                                    success: function (data) {
                                        record.set(data);
                                        record.commit();
                                        this.regulationList.getView().refresh();
                                    },
                                    scope: this
                                }
                            }).show();
                        }
                    },
                    scope: this
                },
                {
                    text: '删除',
                    iconCls: 'icon-delete',
                    handler: function () {
                        this.regulationList.getStore().remove(this.regulationList.getSelectionModel().getSelection());
                        this.regulationList.getView().refresh();
                    },
                    scope: this
                }
            ]
        });

        this.formPanel = Ext.create('Ext.form.Panel', {
            border: false,
            bodyPadding: 5,
            baseCls: 'x-plain',
            items: [
                {
                    xtype: 'hidden',
                    name: 'objectId'
                },
                {
                    xtype: 'fieldset',
                    title: '基本设置',
                    layout: 'column',
                    defaultType: 'textfield',
                    defaults: {
                        columnWidth: .5,
                        labelWidth: 80,
                        labelAlign: 'right',
                        style: 'margin: 3px;'
                    },
                    items: [
                        {
                            fieldLabel: '名称',
                            name: 'name',
                            allowBlank: false
                        },
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
                            xtype: 'numberfield',
                            fieldLabel: '固定长度',
                            name: 'fixedLength',
                            minValue: 5,
                            value: 10,
                            allowBlank: false
                        },
                        {
                            fieldLabel: '序号属性',
                            name: 'sequenceProperty',
                            allowBlank: false
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '按年份循环',
                            allowBlank: false,
                            columns: 6,
                            items: [
                                {boxLabel: '是', name: 'circleSequenceByYear', inputValue: 1},
                                {boxLabel: '否', name: 'circleSequenceByYear', inputValue: 0, checked: true}
                            ]
                        },
                        {
                            fieldLabel: '年份属性',
                            name: 'yearProperty'
                        },
                        {
                            fieldLabel: '备注',
                            name: 'description',
                            xtype: 'textarea',
                            height: 100,
                            columnWidth: 1,
                            maxLength: 1000
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: '规则设置',
                    layout: 'fit',
                    items: [this.regulationList]
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();

        this.on('load', function (form, action) {
            var result = Ext.decode(action.response.responseText);
            var regulationList = [];
            Ext.each(result['regulationList'], function (item) {
                regulationList.push({
                    objectId: item['objectId'],
                    name: item['name'],
                    regulationExpression: item['regulationExpression']
                })
            }, this);
            this.regulationList.getStore().loadData(regulationList);
        }, this);

        this.on('beforesave', function (form, params) {
            this.regulationList.getStore().each(function (record, index) {
                params['regulationList[' + index + '].objectId'] = record.get('objectId');
                params['regulationList[' + index + '].name'] = record.get('name');
                params['regulationList[' + index + '].regulationExpression'] = record.get('regulationExpression');
            }, this);
        }, this);
    }
});

Ext.define('withub.ext.std.entitySequence.EntitySequenceRegulation', {
    extend: 'Ext.Window',
    width: 480,
    layout: 'fit',
    resizable: false,
    modal: true,
    title: '规则',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            border: false,
            bodyPadding: 10,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'hidden',
                    name: 'objectId'
                },
                ,
                {
                    fieldLabel: '名称',
                    name: 'name',
                    allowBlank: false
                },
                {
                    fieldLabel: '规则表达式',
                    name: 'regulationExpression',
                    allowBlank: false
                }
            ]
        });

        this.items = [this.formPanel];

        this.buttons = [
            {
                text: '保存',
                handler: function () {
                    this.fireEvent('success', Ext.apply(this.formPanel.getForm().getValues()));
                    this.close();
                },
                scope: this
            },
            {
                text: '关闭',
                handler: this.close,
                scope: this
            }
        ];

        this.callParent();

        if (this.record != undefined) {
            this.formPanel.getForm().loadRecord(this.record);
        }
    }
});

