Ext.define('withub.ext.std.file.FileConfigList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.file.FileConfigModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'FileConfig',
            baseUrl: '/std/fileConfig',
            model: 'withub.ext.std.file.FileConfigModel',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '实体', width: 180, dataIndex: 'entity.name', sortable: true},
                {text: '服务器', width: 180, dataIndex: 'server.name'},
                {text: '路径', flex: 1, dataIndex: 'serverPath'}
            ],
            tbar: [
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.file.FileConfig', {
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
                    Ext.create('withub.ext.std.file.FileConfig', {
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