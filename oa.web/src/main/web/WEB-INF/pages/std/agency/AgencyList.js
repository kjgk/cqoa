Ext.define('withub.ext.std.agency.AgencyList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.agency.AgencyModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.agency.AgencyModel',
            baseUrl: '/std/agency',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '委托人', width: 100, dataIndex: 'owner', sortable: false},
                {text: '代理人', width: 100, dataIndex: 'agent', sortable: false},
                {text: '状态', width: 100, dataIndex: 'status'},
                {text: '开始时间', width: 120, dataIndex: 'startTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)},
                {text: '结束时间', width: 120, dataIndex: 'endTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}
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
                    handler: this.queryAgency,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.agency.Agency', {
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
            var statusTag = record.get('statusTag');
            items.push([
                {
                    text: '结束',
                    iconCls: 'icon-edit',
                    disabled: statusTag == 'Finish',
                    handler: function () {
                        ExtUtil.confirm('确认结束当前代理吗 ?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在结束当前代理...'});
                            mask.show();
                            Ext.Ajax.request({
                                url: Global.contextPath + "/std/agency/finish/" + objectId,
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.showSuccessMsgBox("代理结束成功!", function () {
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
                }
            ]);
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryAgency();
    },

    queryAgency: function () {
        var date = this.gridPanel.down("#date");
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});