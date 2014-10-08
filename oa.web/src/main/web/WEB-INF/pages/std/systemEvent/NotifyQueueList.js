Ext.define('withub.ext.std.systemEvent.NotifyQueueList', {
    extend: 'Ext.Panel',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.systemEvent.NotifyQueueModel'],
    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.systemEvent.NotifyQueueModel',
            baseUrl: '/std/notifyQueue',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '事件名称', width: 140, dataIndex: 'systemEvent'},
                {text: '发送服务', width: 160, dataIndex: 'notifyServiceType'},
                {text: '标题', width: 280, dataIndex: 'title'},
                {text: '进入时间', width: 130, dataIndex: 'enterTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.SECOND)},
                {text: '最后发送时间', width: 130, dataIndex: 'lastSendTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.SECOND)},
                {text: '发送次数', width: 60, dataIndex: 'sendCount', align: 'right'},
                {text: '返回值', width: 60, dataIndex: 'result'},
                {text: '状态', width: 60, dataIndex: 'status'}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange'
                },
                '发送服务',
                {
                    itemId: 'notifyServiceType',
                    width: 160,
                    xtype: 'codecombo',
                    codeColumnTag: 'NotifyServiceType',
                    showAll: true
                },
                '状态',
                {
                    itemId: 'status',
                    width: 100,
                    xtype: 'codecombo',
                    codeColumnTag: 'NotifyQueueStatus',
                    showAll: true
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryNotifyQueue,
                    scope: this
                },
                {
                    xtype: 'button',
                    text: 'SMS测试',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.systemEvent.SmsSend', {
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

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '查看',
                iconCls: 'icon-view',
                handler: function () {
                    ExtUtil.showWindow('Ext.Panel', {
                        width: 902,
                        height: 560,
                        title: '通知队列查看',
                        html: ExtUtil.buildIframe(Global.contextPath + '/std/notifyQueue/view/' + objectId + '.page', 900, 560)
                    });
                },
                scope: this
            });

        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryNotifyQueue();
    },

    queryNotifyQueue: function () {
        var date = this.gridPanel.down("#date");
        var notifyServiceType = this.gridPanel.down("#notifyServiceType");
        var status = this.gridPanel.down('#status');

        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            notifyServiceType: notifyServiceType.getValue(),
            status: status.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});