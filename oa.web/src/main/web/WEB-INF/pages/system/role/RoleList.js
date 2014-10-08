Ext.define('withub.ext.system.role.RoleList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.role.RoleModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.role.RoleModel',
            baseUrl: '/system/role',
            enablePagginBar: true,
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'Role',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '角色名称', width: 220, dataIndex: 'name', renderer: Ext.util.Format.htmlEncode},
                {text: '角色标识', width: 80, dataIndex: 'roleTag', renderer: Ext.util.Format.htmlEncode},
                {text: '描述', minWidth: 280, flex: 1, dataIndex: 'description', sortable: false, renderer: Ext.util.Format.htmlEncode}
            ],
            tbar: [
                '名称',
                {
                    itemId: 'name',
                    xtype: 'textfield',
                    width: 180,
                    emptyText: '请输入角色名称'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryRole,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.role.Role', {
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
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.system.role.Role', {
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
        this.queryRole();
    },

    queryRole: function () {
        var name = this.gridPanel.down("#name");
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});