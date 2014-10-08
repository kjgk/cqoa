Ext.define('withub.ext.std.sms.FailedSendSmsList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.sms.SmsModel'],

    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.sms.SmsModel',
            baseUrl: '/std/failedSms',
            enablePagginBar: true,
            columns: [
                {xtype: 'rownumberer', width: 32},
                {text: '创建时间', width: 140, dataIndex: 'createDate', renderer: ExtUtil.dateRenderer('Y-m-d H:i:s')},
                {text: '发送时间', minWidth: 140, dataIndex: 'sendDate', renderer: ExtUtil.dateRenderer('Y-m-d H:i:s')},
                {text: '接收者', width: 80, dataIndex: 'name'},
                {text: '电话号码', width: 100, dataIndex: 'recipient'},
                {text: '短信内容', width: 800, dataIndex: 'text'},
                {text: '错误信息', width: 80, dataIndex: 'errors'}
            ],
            tbar: [
                '创建日期',
                {
                    itemId: 'date',
                    xtype: 'daterange',
                    range: '-30d'
                },
                '接收者',
                {
                    itemId: 'name',
                    width: 100,
                    xtype: 'textfield'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.querySms,
                    scope: this
                }
            ]
        });

        this.items = this.gridPanel;

        this.callParent();

        this.querySms();
    },

    querySms: function () {

        var date = this.gridPanel.down('#date');
        if (date.getEndDate() < date.getBeginDate()) {
            ExtUtil.Msg.error('请选择正确的日期范围');
            return false;
        }

        var name = this.gridPanel.down('#name');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});