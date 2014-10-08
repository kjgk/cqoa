Ext.define('withub.ext.system.code.CodeManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.system.code.CodeModel'],
    initComponent: function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '代码分类',
            split: true,
            width: 200,
            singleExpand: true,
            margins: '5 0 0 0',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/code'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '代码列表',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'Code',
            region: 'center',
            margins: '5 0 0 0',
            baseUrl: '/system/code',
            model: 'withub.ext.system.code.CodeModel',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', width: 180, dataIndex: 'name', sortable: false},
                {text: '标识', width: 180, dataIndex: 'codeTag', sortable: false},
                {text: '默认值', width: 100, dataIndex: 'defaultValue', sortable: false, align: 'center',
                    renderer: function (value) {
                        return  value == 1 ? "是" : "否"
                    }
                }
            ]
        });

        this.items = [this.treePanel, this.gridPanel];

        this.treePanel.on('select', function (tree, record, index) {
            var store = this.gridPanel.getStore(),
                proxy = store.getProxy();
            if (record.get('type') != 'CodeColumn') {
                return;
            }
            proxy.extraParams['id'] = record.get('objectId');
            store.load();
        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            if (record.get('type') == 'CodeColumnCategory') {
                items.push({
                    text: '添加代码栏目',
                    iconCls: 'icon-add',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.system.code.CodeColumn', {
                            action: 'create',
                            codeColumnCategoryId: objectId,
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
                });
                items.push({
                    text: '添加代码栏目分类',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.code.CodeColumnCategory', {
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
                });
                items.push({
                    text: '编辑代码栏目分类',
                    iconCls: 'icon-edit',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.system.code.CodeColumnCategory', {
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
                });
            }
            if (record.get('type') == 'CodeColumn') {
                items.push({
                    text: '添加代码',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.code.Code', {
                            action: 'create',
                            codeColumnId: objectId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                });

                items.push({
                    text: '编辑代码栏目',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.code.CodeColumn', {
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
                });
            }
        }, this);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.system.code.Code', {
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