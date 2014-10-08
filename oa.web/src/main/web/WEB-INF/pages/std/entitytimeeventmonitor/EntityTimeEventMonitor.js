Ext.define('withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitor', {
    extend: 'withub.ext.common.Window',
    title: '实体时间事件监控',
    baseUrl: '/std/entityTimeEventMonitor',

    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            border: false,
            baseCls: 'x-plain',
            items: [
                {
                    xtype: 'hidden',
                    name: 'objectId'
                },
                {
                    xtype: 'fieldset',
                    layout: 'column',
                    defaultType: 'textfield',
                    defaults: {
                        columnWidth: 1,
                        labelWidth: 100,
                        labelAlign: 'right',
                        style: 'margin: 3px;'
                    },
                    items: [
                        {
                            fieldLabel: '名称',
                            name: 'name',
                            maxLength: 100,
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
                            fieldLabel: '实体时间属性',
                            name: 'entityTimeProperty',
                            maxLength: 100,
                            columnWidth: .5,
                            allowBlank: false
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '类型',
                            columnWidth: .5,
                            allowBlank: false,
                            items: [
                                {boxLabel: '类型1', name: 'entityTimePropertyType', inputValue: 1},
                                {boxLabel: '类型0', name: 'entityTimePropertyType', inputValue: 0, checked: true}
                            ]
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '提醒时间',
                            columnWidth: .5,
                            name: 'remindTimeValue',
                            minValue: 0,
                            value: 1,
                            allowBlank: false
                        },
                        {
                            xtype: 'codecombo',
                            fieldLabel: '单位',
                            columnWidth: .5,
                            name: 'remindTimeValueTimeUnit.objectId',
                            codeColumnTag: 'TimeUnit',
                            codeTags: ['Day', 'Hour', 'Minute'],
                            allowBlank: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '过期时间',
                            columnWidth: .5,
                            name: 'expiredTimeValue',
                            minValue: 0,
                            value: 1,
                            allowBlank: false
                        },
                        {
                            xtype: 'codecombo',
                            fieldLabel: '单位',
                            columnWidth: .5,
                            name: 'expiredTimeValueTimeUnit.objectId',
                            codeColumnTag: 'TimeUnit',
                            codeTags: ['Day', 'Hour', 'Minute'],
                            allowBlank: false
                        },
                        {
                            fieldLabel: '事件类名',
                            name: 'eventClassName',
                            maxLength: 100,
                            allowBlank: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '监控时间(小时)',
                            name: 'monitorHour',
                            minValue: 0,
                            value: 1,
                            allowBlank: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '间隔时间(小时)',
                            name: 'intervalHour',
                            minValue: 1,
                            value: 1,
                            allowBlank: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '优先级',
                            name: 'priority',
                            minValue: 1,
                            maxValue: 3,
                            value: 3,
                            allowBlank: false
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: '记录大小',
                            minValue: 1,
                            value: 10,
                            name: 'recordSetSize',
                            allowBlank: false
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '过期事件',
                            allowBlank: false,
                            items: [
                                {boxLabel: '是', name: 'expiredEvent', inputValue: 1},
                                {boxLabel: '否', name: 'expiredEvent', inputValue: 0, checked: true}
                            ]
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '工作日历',
                            allowBlank: false,
                            items: [
                                {boxLabel: '是', name: 'useworkCalendar', inputValue: 1, checked: true},
                                {boxLabel: '否', name: 'useworkCalendar', inputValue: 0}
                            ]
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '启用',
                            allowBlank: false,
                            items: [
                                {boxLabel: '是', name: 'enable', inputValue: 1, checked: true},
                                {boxLabel: '否', name: 'enable', inputValue: 0}
                            ]
                        },
                        {
                            xtype: 'textarea',
                            fieldLabel: '附加限制',
                            name: 'additionalCondition',
                            maxLength: 200
                        }
                    ]
                }
//                {
//                    xtype: 'hidden',
//                    name: 'creator.objectId',
//                    value: this.action =='create'? PageContext.currentUser.objectId:undefined
//                },
//                {
//                    xtype: 'hidden',
//                    name: 'lastEditor.objectId',
//                    value: PageContext.currentUser.objectId
//                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

