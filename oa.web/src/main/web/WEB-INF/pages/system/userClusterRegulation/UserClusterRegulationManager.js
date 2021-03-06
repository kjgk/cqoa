Ext.define('withub.ext.system.userClusterRegulation.UserClusterRegulationManager', {
    extend: 'Ext.Viewport',
    layout: 'fit',
    closable: true,
    initComponent: function() {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            border: false,
            singleExpand: true,
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/userClusterRegulation'
        });

        this.items = [this.treePanel];

        this.treePanel.on('createcontextmenu', function(items, store, record, index, event) {
            var objectId = record.get('objectId');
            console.log(record.data)
            items.push(
                {
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function() {
                        Ext.create('withub.ext.system.userCluster.UserClusterRegulation', {
                            action: 'create',
                            parentId: objectId,
                            userClusterId: '94E292F3-6B08-49A1-982B-49D760258BAC',
                            listeners: {
                                success: function() {
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
                    handler: function() {
                        Ext.create('withub.ext.system.userCluster.UserClusterRegulation', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function() {
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