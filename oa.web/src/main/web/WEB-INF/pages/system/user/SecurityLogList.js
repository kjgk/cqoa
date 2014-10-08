Ext.define('withub.ext.system.user.SecurityLogList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.user.SecurityLogModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.user.SecurityLogModel',
            baseUrl: '/security/securityLog',
            enablePagginBar: true,
            enableDeleteItem: false,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '姓名', width: 160, dataIndex: 'name'},
                {text: '账号', width: 100, dataIndex: 'account', align: 'center'},
                {text: '组织机构', minWidth: 220, flex: 1, dataIndex: 'organization'},
                {text: '客户端地址', width: 180, dataIndex: 'clientIdentityCode'},
                {text: '登录类型', width: 180, dataIndex: 'loginType'},
                {text: '登录时间', width: 150, dataIndex: 'loginTime', sortable: false, displayType: DisplayType.DateSecond},
                {text: '退出时间', width: 150, dataIndex: 'logoutTime', sortable: false, displayType: DisplayType.DateSecond}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange',
                    width: 200
                },
                '组织机构',
                {
                    itemId: 'organization',
                    xtype: 'treecombo',
                    width: 240,
                    enableClear: true,
                    url: '/system/organization/loadTree',
                    showPathName: true,
                    pathNameDepth: 3,
                    emptyText: '请选择组织机构'
                },
                '姓名',
                {
                    itemId: 'name',
                    xtype: 'textfield',
                    width: 160,
                    emptyText: '请输入姓名'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.querySecurityLog,
                    scope: this
                }
            ]
        });

        this.items = this.gridPanel;

        this.callParent();

        this.querySecurityLog();
    },

    querySecurityLog: function () {
        var date = this.gridPanel.down("#date");
        var name = this.gridPanel.down('#name');
        var organization = this.gridPanel.down('#organization');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            organizationId: organization.getObjectValue(),
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});