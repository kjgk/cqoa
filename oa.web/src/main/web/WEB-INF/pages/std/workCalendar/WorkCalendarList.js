Ext.define('withub.ext.std.workCalendar.WorkCalendarList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.workCalendar.WorkCalendarModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'WorkCalendar',
            baseUrl: '/std/workCalendar',
            model: 'withub.ext.std.workCalendar.WorkCalendarModel',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '日历名称', minWidth: 160, flex: 1, dataIndex: 'name'},
                {text: '默认日历', width: 80, dataIndex: 'defaultCalendar', displayType: DisplayType.BooleanValue, align: 'center'},
                {text: '周末节假日', width: 80, dataIndex: 'weekendHoliday', displayType: DisplayType.BooleanValue, align: 'center'},
                {text: '天工作小时', width: 100, dataIndex: 'dayHours', align: 'center'},
                {text: '上午开始时间', width: 100, dataIndex: 'forenoonStartTime', align: 'center'},
                {text: '上午结束时间', width: 100, dataIndex: 'forenoonEndTime', align: 'center'},
                {text: '下午开始时间', width: 100, dataIndex: 'afternoonStartTime', align: 'center'},
                {text: '下午结束时间', width: 100, dataIndex: 'afternoonEndTime', align: 'center'}

            ],
            tbar: [
                '日历名称',
                {
                    xtype:'textfield',
                    itemId:'name',
                    width:100,
                    emptyText:'请选择日历名称'
                },
                {
                    xtype:'button',
                    text:'搜索',
                    iconCls:'icon-query',
                    handler:this.query,
                    scope:this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.workCalendar.WorkCalendar', {
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

        this.gridPanel.getStore().loadPage(1);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            var weekendHoliday = record.get('weekendHoliday');
            items.push(
                {
                    text: '设置节假日',
                    iconCls: 'icon-calendar ',
                    handler: function () {

                        Ext.create('withub.ext.std.workCalendar.WorkCalendarHolidayManager', {
                            workCalendarId:objectId,
                            weekendHoliday:weekendHoliday,
                            title:'设置节假日'
                        }).show();

//                        ExtUtil.showWindow('withub.ext.std.workCalendar.WorkCalendarHolidayManager', {
//                            workCalendarId: objectId,
//                            title: '节假日列表'
//                        });
                    },
                    scope: this
                },
                {
                    text: '工作日历测试',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.workCalendar.WorkCalendarTest', {
                            workCalendarId: objectId
                        }).show();

                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.workCalendar.WorkCalendar', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    store.load();
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
    } ,

    query:function () {
        var name = this.gridPanel.down('#name');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name:name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});