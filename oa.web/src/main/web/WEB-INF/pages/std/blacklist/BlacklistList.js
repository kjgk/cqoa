Ext.define('withub.ext.std.blacklist.BlacklistList', {
    extend: 'Ext.Panel',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.blacklist.BlacklistModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.blacklist.BlacklistModel',
            baseUrl: '/std/blacklist',
            enablePagginBar: true,
            enableDeleteItem: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '姓名', width: 100, dataIndex: 'user', sortable: false},
                {text: '身份证', width: 150, dataIndex: 'idCard', sortable: false},
                {text: '原因', width: 450, dataIndex: 'description'},
                {text: '列入时间', width: 120, dataIndex: 'enterTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange'
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
                        Ext.create('withub.ext.std.blacklist.Blacklist', {
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
            var userId = record.get('userId');
            items.push([
                {
                    text: '解除',
                    iconCls: 'icon-edit',
                    handler: function () {
                        ExtUtil.confirm('确认解除黑名单吗 ?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在解除黑名单...'});
                            mask.show();
                            Ext.Ajax.request({
                                url: Global.contextPath + "/std/blacklist/removeFromBlacklist/" + userId,
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.showSuccessMsgBox("黑名单解除成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.showErrorMsgBox(result.message);
                                    }
                                },
                                failure: function (response) {
                                    mask.hide();
                                    ExtUtil.showErrorMsgBox(response.responseText);
                                }
                            });
                        });
                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.blacklist.Blacklist', {
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
            ]);
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.query();
    },

    query: function () {
        var date = this.gridPanel.down("#date");
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});