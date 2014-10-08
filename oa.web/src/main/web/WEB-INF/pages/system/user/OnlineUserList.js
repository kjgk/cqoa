Ext.define('withub.ext.system.user.OnlineUserList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.user.OnlineUserModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.user.OnlineUserModel',
            baseUrl: '/security/onlineUser',
            enablePagginBar: true,
            border: false,

            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '姓名', width: 100, dataIndex: 'name'},
                {text: '账户', width: 100, dataIndex: 'account'},
                {text: '组织机构', minWidth: 180, flex: 1, dataIndex: 'organization'},
                {text: '客户端地址', width: 180, dataIndex: 'clientIdentityCode'},
                {text: '登录类型', width: 180, dataIndex: 'loginType'},
                {text: '登录时间', width: 150, dataIndex: 'loginTime', sortable: false, displayType: DisplayType.DateSecond},
                {text: '在线时间(分)', width: 120, dataIndex: 'onlineTime', align: 'right', displayType: DisplayType.Number}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange',
                    width: 230
                },
                '组织机构',
                {
                    itemId: 'organization',
                    xtype: 'treecombo',
                    width: 200,
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
                    width: 90,
                    emptyText: '请输入姓名'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryOnlineUser,
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var me = this;
            var objectId = record.get('objectId');
            var userObjectId = record.get('userObjectId');
            var accountId = record.get('accountId');
            items.push(
                {
                    text: '踢出在线用户',
                    handler: function () {
                        ExtUtil.Msg.confirm('确定将 ' + record.get('name') + ' 踢出系统吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                method: 'GET',
                                url: PageContext.contextPath + "/security/kickoutOnlineUser",
                                params: {
                                    userObjectId: userObjectId,
                                    objectId: objectId,
                                    accountId: accountId
                                },
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("踢出成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryOnlineUser();
    },

    queryOnlineUser: function () {
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