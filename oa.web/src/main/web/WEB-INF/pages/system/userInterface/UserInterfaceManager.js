Ext.define('withub.ext.system.userInterface.UserInterfaceManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.system.userInterface.UserInterfaceMenuModel'],
    initComponent: function() {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '用户接口',
            split: true,
            width: 280,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/userInterface'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '接口菜单列表',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'UserInterface',
            baseUrl: '/system/userInterfaceMenu',
            region: 'center',
            margins: '5 5 5 0',
            model: 'withub.ext.system.userInterface.UserInterfaceMenuModel',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '菜单名称', flex:1, dataIndex: 'name', sortable: true},
                {text: '菜单标识', flex:1, dataIndex: 'menuTag', sortable: true},
                {text: '菜单类型', width: 100, dataIndex: 'menuType',sortable: false,align:'center',
                    renderer:function(value) {
                        return  value == 1 ? "上下文" : "工具栏"
                    }
                }
            ]
        });

        this.items = [this.treePanel, this.gridPanel];

        this.treePanel.on('select', function(tree, record, index) {
            if (record.get('type') != 'UserInterface') {
                return;
            }
            var store = this.gridPanel.getStore();
            store.getProxy().extraParams['id'] = record.get('objectId');
            store.load();
        }, this);

        this.treePanel.on('createcontextmenu', function(items, store, record, index, event) {
            var objectId = record.get('objectId');

            if (record.get('type') == 'UserInterfaceCategory') {
                items.push(
                    {
                        text: '添加用户接口',
                        iconCls: 'icon-add',
                        handler: function() {
                            Ext.create('withub.ext.system.userInterface.UserInterface', {
                                action: 'create',
                                userInterfaceCategoryId: objectId,
                                listeners: {
                                    success: function() {
                                        store.load({
                                            node: record
                                        });
                                    },
                                    scope: this
                                }
                            }).show();
                        },
                        scope: this
                    },
                    {
                        text: '添加分类',
                        iconCls: 'icon-add',
                        handler: function() {
                            Ext.create('withub.ext.system.userInterface.UserInterfaceCategory', {
                                action: 'create',
                                parentId: objectId,
                                listeners: {
                                    success: function() {
                                        store.load({
                                            node: record
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
                        handler: function() {
                            Ext.create('withub.ext.system.userInterface.UserInterfaceCategory', {
                                action: 'update',
                                objectId: objectId,
                                listeners: {
                                    success: function() {
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

            if (record.get('type') == 'UserInterface') {
                items.push(
                    {
                        text: '添加菜单',
                        iconCls: 'icon-add',
                        handler: function() {
                            Ext.create('withub.ext.system.userInterface.UserInterfaceMenu', {
                                action: 'create',
                                userInterfaceId: objectId,
                                listeners: {
                                    success: function() {
                                        this.gridPanel.getStore().load();
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
                        handler: function() {
                            Ext.create('withub.ext.system.userInterface.UserInterface', {
                                action: 'update',
                                objectId: objectId,
                                listeners: {
                                    success: function() {
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

        this.gridPanel.on('createcontextmenu', function(items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function() {
                    Ext.create('withub.ext.system.userInterface.UserInterfaceMenu', {
                        action: 'update',
                        objectId: objectId,
                        listeners: {
                            success: function() {
                                store.load();
                            },
                            scope: this
                        }
                    }).show();
                },
                scope: this
            });
        }, this);

        this.callParent();
    }
});