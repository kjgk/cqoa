Ext.define('withub.ext.system.userCluster.UserClusterManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '用户簇',
            split: true,
            width: 240,
            singleExpand: true,
            margins: '5 0 0 0',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/userCluster'
        });

        this.regulationPanel = Ext.create('withub.ext.base.Tree', {
            title: '用户簇规则模版',
            region: 'center',
            margins: '5 0 0 0',
            url: '/system/userClusterRegulation/loadTree'
        });

        this.items = [this.treePanel, this.regulationPanel];

        this.regulationPanel.on('itemcontextmenu', function (view, record, item, index, event, options) {
            var store = this.regulationPanel.getStore(),
                proxy = store.getProxy(), objectId = record.get('objectId');
            var items;
            if (record.get('depth') == 1) {
                items = [
                    {
                        text: '添加',
                        iconCls: 'icon-add',
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserClusterRegulation', {
                                action: 'create',
                                parentId: objectId,
                                userClusterId: proxy.extraParams['userClusterId'],
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
                ]
            }
            if (record.get('depth') == 2) {
                items = [
                    {
                        text: '编辑',
                        iconCls: 'icon-edit',
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserClusterRegulation', {
                                objectId: objectId,
                                action: 'update',
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
                        xtype: 'deleteitem',
                        text: '删除',
                        url: '/system/userClusterRegulation/delete/' + objectId,
                        onSuccess: function () {
                            store.load({
                                node: store.getNodeById(record.get('parentId'))
                            });
                        }
                    }
                ]
            }
            var menu = Ext.create('Ext.menu.Menu', {
                items: items
            });
            event.preventDefault();
            menu.showAt(event.getXY());
        }, this);

        this.treePanel.on('select', function (tree, record, index) {
            if (record.get('type') != 'UserCluster') {
                return;
            }
            var store = this.regulationPanel.getStore();
            store.getProxy().extraParams['userClusterId'] = record.get('objectId');
            store.setRootNode({
                id: 'Root'
            });
        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            if (record.get('type') == 'UserClusterCategory') {
                items.push(
                    {
                        text: '添加用户簇',
                        iconCls: 'icon-add',
                        hidden: record.get('depth') == 1,
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserCluster', {
                                action: 'create',
                                userClusterCategoryId: objectId,
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
                    },
                    {
                        text: '添加分类',
                        iconCls: 'icon-add',
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserClusterCategory', {
                                action: 'create',
                                parentId: objectId,
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
                    },
                    {
                        text: '编辑',
                        iconCls: 'icon-edit',
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserClusterCategory', {
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

            if (record.get('type') == 'UserCluster') {
                items.push(
                    {
                        text: '编辑',
                        iconCls: 'icon-edit',
                        handler: function () {
                            Ext.create('withub.ext.system.userCluster.UserCluster', {
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


        this.callParent();
    }
});