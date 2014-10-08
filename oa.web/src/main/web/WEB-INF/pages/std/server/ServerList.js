Ext.define('withub.ext.std.server.ServerList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.server.ServerModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'Server',
            baseUrl: '/std/server',
            model: 'withub.ext.std.server.ServerModel',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', width: 180, dataIndex: 'name', sortable: true},
                {text: 'IP', width: 180, dataIndex: 'ip'},
                {text: '用户名', width: 180, dataIndex: 'username'},
                {text: '密码', width: 180, dataIndex: 'password'}
            ],
            tbar: [
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.server.Server', {
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
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.server.Server', {
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