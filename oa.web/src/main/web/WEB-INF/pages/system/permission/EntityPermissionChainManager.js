Ext.define('withub.ext.system.permission.EntityPermissionChainManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'center',
            title: '实体权限控制链',
            split: true,
            width: 200,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/entityPermissionChain'
        });

        this.items = [this.treePanel];

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');

            items.push(
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.permission.EntityPermissionChain', {
                            action: 'create',
                            parentId: objectId,
                            listeners: {
                                success: function () {
                                    if (record.get('leaf')) {
                                        store.load({
                                            node: store.getNodeById(record.get("parentId"))
                                        });
                                    } else {
                                        store.load({ node: record });
                                    }
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.system.permission.EntityPermissionChain', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    store.load({
                                        node: store.getNodeById(record.get("parentId"))
                                    });
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                }
            );
        }, this);

        this.callParent();
    }
});