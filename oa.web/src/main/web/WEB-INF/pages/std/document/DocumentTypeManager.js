Ext.define('withub.ext.std.document.DocumentTypeManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.std.document.DocumentTypeModel'],
    initComponent: function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '文档分类',
            split: true,
            width: 200,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/std/documentType'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '文档类型列表',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'DocumentType',
            baseUrl: '/std/documentType',
            region: 'center',
            margins: '5 5 5 0',
            model: 'withub.ext.std.documentType.DocumentTypeModel',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', width: 180, dataIndex: 'name', sortable: true},
                {text: '标识', flex: 1, dataIndex: 'documentTypeTag'}
            ]
        });

        this.items = [this.treePanel, this.gridPanel];

        this.treePanel.on('select', function (tree, record, index) {
            var store = this.gridPanel.getStore();
            store.getProxy().extraParams['id'] = record.get('objectId');
            store.load();
        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push([
                {
                    text: '添加文档类型',
                    iconCls: 'icon-add',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.std.documentType.DocumentType', {
                            action: 'create',
                            documentTypeCategoryId: objectId,
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
                        Ext.create('withub.ext.std.documentType.DocumentTypeCategory', {
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
                        Ext.create('withub.ext.std.documentType.DocumentTypeCategory', {
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
            ]);
        }, this);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.documentType.DocumentType', {
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