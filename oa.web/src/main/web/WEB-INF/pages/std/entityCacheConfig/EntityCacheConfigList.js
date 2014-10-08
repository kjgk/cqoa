Ext.define('withub.ext.std.entityCacheConfig.EntityCacheConfigList', {
    extend: 'Ext.Panel',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.entityCacheConfig.EntityCacheConfigModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'EntityCacheConfig',
            baseUrl: '/std/entityCacheConfig',
            model: 'withub.ext.std.entityCacheConfig.EntityCacheConfigModel',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '缓存名称', flex: 1, minWidth: 240, dataIndex: 'name'},
                {text: '缓存关键字', width: 200, dataIndex: 'cacheKey'},
                {text: '文档类型', width: 120, dataIndex: 'documentType', sortable: false},
                {text: '实体名称', width: 120, dataIndex: 'entity', sortable: false},
                {text: '时间戳属性', width: 140, dataIndex: 'timestampProperty'},
                {text: '缓存数量', width: 80, dataIndex: 'cacheCount', align: 'right'},
                {text: '启用', width: 60, dataIndex: 'enable', align: 'center', sortable: false, renderer: ExtUtil.booleanValueRenderer}
            ],
            tbar: [
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.entityCacheConfig.EntityCacheConfig', {
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
                },
                {
                    xtype: 'button',
                    text: '刷新',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.Ajax.request({
                            url: Global.contextPath + "/std//entityCacheConfig/refreshCache",
                            success: function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    ExtUtil.showSuccessMsgBox("缓存刷新成功!", function () {
                                        store.load();
                                    });
                                } else {
                                    ExtUtil.showErrorMsgBox(result.message);
                                }
                            }
                        });
                    },
                    scope: this
                }
            ]
        });

        this.gridPanel.getStore().loadPage(1);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.entityCacheConfig.EntityCacheConfig', {
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
            });
        }, this);

        this.items = this.gridPanel;

        this.callParent();
    }
});