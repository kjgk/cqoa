Ext.define('withub.ext.system.dataDeleted.DataDeletedList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.dataDeleted.DataDeletedModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.dataDeleted.DataDeletedModel',
            baseUrl: '/system/dataDeleted',
            enablePagginBar: true,
            border: false,
            columns: [
                {xtype: 'rownumberer', width: 32 },
                {text: '最后修改时间', width: 160, dataIndex: 'deletedTime', align: 'center'},
                {text: '记录标识', minWidth: 240, flex: 1, dataIndex: 'detail'},
                {text: '创建者', width: 100, dataIndex: 'creator'},
                {text: '最后修改者', width: 100, dataIndex: 'lastEditor'}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange',
                    width: 200
                },
                '实体',
                {
                    itemId: 'entity',
                    xtype: 'treecombo',
                    width: 240,
//                    enableClear: true,
                    url: '/system/metadata/loadTree',
                    selectType: 'Entity',
//                    showPathName: true,
                    params: {depth: 2},
                    emptyText: '请选择实体'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryEntityDeletedData,
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var me = this;
            var objectId = record.get('objectId');
            var entityId = record.get('entityId');
            items.push(
                {
                    text: '恢复记录',
                    handler: function () {
                        ExtUtil.Msg.confirm('确定恢复该条记录吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                method: 'GET',
                                url: PageContext.contextPath + "/system/dataDeleted/recover",
                                params: {
                                    objectId: objectId,
                                    entityId: entityId
                                },
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("恢复成功!", function () {
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

        this.queryEntityDeletedData();
    },

    queryEntityDeletedData: function () {
        var date = this.gridPanel.down("#date");
        var entity = this.gridPanel.down('#entity');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            entityId: entity.getObjectValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});