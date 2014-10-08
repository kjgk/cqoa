Ext.define('withub.ext.std.workCalendar.WorkCalendarHolidayList', {
    extend: 'Ext.Panel',
    height: 580,
    width: 400,
    layout: 'fit',
    autoQuery: true,
    requires: ['withub.ext.std.workCalendar.WorkCalendarHolidayModel'],
    initComponent: function () {

        this.extraParams = {
            workCalendarId: this.workCalendarId
        };

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableDeleteItem: true,
            entity: 'WorkCalendarHoliday',
            baseUrl: '/std/workCalendarHoliday',
            model: 'withub.ext.std.workCalendar.WorkCalendarHolidayModel',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '日期', width: 120, dataIndex: 'day', renderer: ExtUtil.dateRenderer(DateFormat.DAY)},
                {text: '节假日', width: 100, dataIndex: 'holiday', align: 'center', renderer: ExtUtil.booleanValueRenderer}
            ],
            tbar: [
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.workCalendar.WorkCalendarHoliday', {
                            action: 'create',
                            workCalendarId: this.workCalendarId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    xtype: 'button',
                    text: '测试',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.workCalendar.WorkCalendarTest', {
                            listeners: {
                                success: function () {

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
        }, this);

        this.items = this.gridPanel;

        this.callParent();
    }
});