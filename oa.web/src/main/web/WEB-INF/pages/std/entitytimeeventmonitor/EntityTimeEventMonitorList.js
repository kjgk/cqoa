Ext.define('withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitorList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitorModel'],

    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitorModel',
            baseUrl: '/std/entityTimeEventMonitor',
            enablePagginBar: true,
            enableDeleteItem: true,
            border: false,
            columns: [
                {xtype: 'rownumberer', width: 32, locked: true},
                {text: '名称', width: 200, dataIndex: 'name'},
                {text: '实体', width: 100, dataIndex: 'entity'},
                {text: '过期事件', width: 80, dataIndex: 'expiredEvent', align: 'center', renderer: function (v) {
                    if (v == '0') {
                        return "否";
                    }
                    else {
                        return "是";
                    }
                } },
                {text: '启用', width: 80, dataIndex: 'enable', align: 'center', renderer: function (v) {
                    if (v == '0') {
                        return "否";
                    }
                    else {
                        return "是";
                    }
                } },
                {text: '工作日历', width: 80, dataIndex: 'useworkCalendar', align: 'center', renderer: function (v) {
                    if (v == '0') {
                        return "否";
                    }
                    else {
                        return "是";
                    }
                } },
                {text: '提醒时间', width: 80, dataIndex: 'remindTimeValue', align: 'right' },
                {text: '过期时间', width: 80, dataIndex: 'expiredTimeValue', align: 'right'},
                {text: '监控时间(H)', width: 80, dataIndex: 'monitorHour', align: 'right'},
                {text: '间隔时间(H)', width: 80, dataIndex: 'intervalHour', align: 'right'},
                {text: '优先级', width: 80, dataIndex: 'priority', align: 'right'},
                {text: '记录大小', width: 80, dataIndex: 'recordSetSize', align: 'right'},
                {text: '实体时间属性', width: 100, dataIndex: 'entityTimeProperty'},
                {text: '实体时间类型', width: 80, dataIndex: 'entityTimePropertyType', align: 'center', renderer: function (v) {
                    if (v == '0') {
                        return "类型0";
                    }
                    else {
                        return "类型1";
                    }
                }},
                {text: '事件类名', width: 240, dataIndex: 'eventClassName'},
                {text: '附加限制', minWidth: 200, flex: 1, dataIndex: 'additionalCondition'}
            ],
            tbar: [
                '实体',
                {
                    itemId: 'entityName',
                    xtype: 'textfield',
                    width: 180,
                    emptyText: '请输入实体名称'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.query,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitor', {
                            action: 'create',
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.entitytimeeventmonitor.EntityTimeEventMonitor', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.query();
    },

    query: function () {
        var entityName = this.gridPanel.down('#entityName ');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            entityName: entityName.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});