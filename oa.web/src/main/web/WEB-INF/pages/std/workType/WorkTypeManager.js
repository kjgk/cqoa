Ext.define('withub.ext.std.workType.WorkTypeManager', {
    extend: 'Ext.Panel',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.std.workType.WorkTypeModel'],
    initComponent: function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '工种分类',
            split: true,
            width: 320,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/std/workType'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '工种类型列表',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'WorkType',
            baseUrl: '/std/workType',
            region: 'center',
            margins: '5 5 5 0',
            model: 'withub.ext.std.workType.WorkTypeModel',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '工种名称', flex: 1, dataIndex: 'name', sortable: false}
            ]
        });

        this.items = [this.treePanel, this.gridPanel];

        this.treePanel.on('select', function (tree, record, index) {
            var store = this.gridPanel.getStore(), proxy = store.getProxy();
            proxy.extraParams['id'] = record.get('objectId');
            store.load();
        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '添加工种类型',
                    iconCls: 'icon-add',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.std.workType.WorkType', {
                            action: 'create',
                            workTypeCategoryId: objectId,
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
                    text: '添加分类',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.workType.WorkTypeCategory', {
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
                    }
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.std.workType.WorkTypeCategory', {
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

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.workType.WorkType', {
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

        this.callParent();
    }
});