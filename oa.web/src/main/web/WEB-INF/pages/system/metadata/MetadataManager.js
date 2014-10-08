Ext.define('withub.ext.system.metadata.MetadataManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.system.metadata.PropertyModel'],
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '实体类型',
            split: true,
            width: 280,
            singleExpand: true,
            margins: '5 0 0 0',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/metadata'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '实体属性列表',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'Property',
            baseUrl: '/system/property',
            region: 'center',
            margins: '5 0 0 0',
            model: 'withub.ext.system.metadata.PropertyModel',
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '属性名称', width: 200, dataIndex: 'name'} ,
                {text: '属性名', width: 180, dataIndex: 'propertyName'},
                {text: '字段名', width: 180, dataIndex: 'columnName'} ,
                {text: '字段长度', width: 80, dataIndex: 'dataLength', align: 'right'} ,
                {text: '属性类型', width: 120, dataIndex: 'propertyType'},
                {text: '数据类型', width: 120, dataIndex: 'propertyDataType'}
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

            if (record.get('type') == 'EntityCategory') {
                items.push({
                    text: '添加实体',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.metadata.Entity', {
                            action: 'create',
                            entityCategoryId: objectId,
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
                    text: '添加分类',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.metadata.EntityCategory', {
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
                });
                items.push({
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.metadata.EntityCategory', {
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

            if (record.get('type') == 'Entity') {
                items.push(
                    {
                        text: '添加属性',
                        iconCls: 'icon-add',
                        handler: function () {
                            Ext.create('withub.ext.system.metadata.Property', {
                                action: 'create',
                                entityId: objectId,
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
                        handler: function () {
                            Ext.create('withub.ext.system.metadata.Entity', {
                                action: 'update',
                                objectId: objectId,
                                entityCategoryId: record.get('parentId'),
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
                items.push({
                    text: '重置排序号',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var me = this;
                        ExtUtil.Msg.confirm('确认重置排序号吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在重置排序号...'});
                            mask.show();
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/metadata/resetEntityOrderNo",
                                params: {
                                    entity: record.get("attributes").entityName
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("排序号完成重置!");
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                },
                                failure: function (response) {
                                    mask.hide();
                                    ExtUtil.Msg.error(response.responseText);
                                }
                            });
                        });
                    },
                    scope: this
                });
                items.push({
                    text: '添加根节点',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.metadata.RecursionEntityRootNode', {
                            action: 'create',
                            entityName: record.get("attributes").entityName,
                            listeners: {
                                failure: function () {
                                    //todo
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                });
                items.push({
                    text: '重置全文检索',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var me = this;
                        ExtUtil.Msg.confirm('确认重置全文检索吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在重置全文检索...'});
                            mask.show();
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/std/lucene/resetFullTextSearch",
                                params: {
                                    entity: record.get("attributes").entityName
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("全文检索完成重置!");
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                },
                                failure: function (response) {
                                    mask.hide();
                                    ExtUtil.Msg.error(response.responseText);
                                }
                            });
                        });
                    },
                    scope: this
                });
                items.push({
                    text: '重置拼音',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var me = this;
                        ExtUtil.Msg.confirm('确认重置拼音吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在重置拼音...'});
                            mask.show();
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/metadata/resetPinYin",
                                params: {
                                    entity: record.get("attributes").entityName
                                },
                                success: function (response) {
                                    mask.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("拼音完成重置!");
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                },
                                failure: function (response) {
                                    mask.hide();
                                    ExtUtil.Msg.error(response.responseText);
                                }
                            });
                        });
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
                    Ext.create('withub.ext.system.metadata.Property', {
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