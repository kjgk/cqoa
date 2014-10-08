Ext.define('withub.ext.std.customPage.CustomPageList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            fields: ['id', 'name'],
            baseUrl: '/ea/customPage',
            enableDeleteItem: true,
            entity: 'CustomPage',
            autoQuery: true,
            identityField: 'id',
            tbar: [
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.customPage.CustomPage', {
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
            ],
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', width: 160, dataIndex: 'name'}
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('id');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.customPage.CustomPage', {
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