Ext.define('withub.ext.system.permission.PermissionManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '权限管理',
            split: true,
            width: 280,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/permission'
        });

        this.treeRegulationPanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'center',
            title: '权限规则',
            split: true,
            rootVisible: true,
            root: {
                id: 'Root',
                text: '权限规则',
                expanded: true
            },
            rootDepth: 0,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/permissionRegulation'
        });

        this.items = [this.treePanel, this.treeRegulationPanel];

        this.treePanel.on('select', function (tree, record, index) {
            if (record.get('type') != 'Permission') {
                return;
            }
            this.treeRegulationPanel.permissionId = record.get("objectId");
            this.treeRegulationPanel.setRootNode({
                id: 'Permission_' + record.get("objectId"),
                text: '权限规则',
                expanded: true
            });

        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');

            if (record.get('type') == 'Entity') {
                items.push(
                    {
                        text: '添加权限',
                        iconCls: 'icon-add',
                        handler: function () {

                            Ext.create('withub.ext.system.permission.Permission', {
                                action: 'create',
                                entityId: objectId,
                                listeners: {
                                    success: function () {
                                        store.load({
                                            node: record
                                        });
                                    },
                                    scope: this
                                }
                            }).show();
                        },
                        scope: this
                    }
                );
            }

            if (record.get('type') == 'Permission') {
                items.push(
                    {
                        text: '编辑',
                        iconCls: 'icon-edit',
                        handler: function () {
                            Ext.create('withub.ext.system.permission.Permission', {
                                action: 'update',
                                objectId: objectId,
                                listeners: {
                                    success: function () {
                                        store.load({
                                            node: store.getNodeById(record.get('parentId'))
                                        });
                                    },
                                    scope: this
                                }
                            }).show();
                        },
                        scope: this
                    }
                );
            }
        }, this);

        this.treeRegulationPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {

                        Ext.create('withub.ext.system.permission.PermissionRegulation', {

                            action: 'create',
                            parentId: objectId,
                            permissionId: this.treeRegulationPanel.permissionId,
                            listeners: {
                                success: function () {
                                    store.load({
                                        node: store.getNodeById(record.get('parentId'))
                                    });
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
                    hidden: record.get('depth') == 0,
                    handler: function () {
                        Ext.create('withub.ext.system.permission.PermissionRegulation', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    store.load({
                                        node: store.getNodeById(record.get('parentId'))
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
