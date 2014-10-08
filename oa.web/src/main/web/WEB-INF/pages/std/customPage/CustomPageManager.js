Ext.define('withub.ext.std.customPage.CustomPageManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    initComponent: function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '界面分类',
            split: true,
            width: 200,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/std/customPage'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '自定义界面',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'CustomPage',
            baseUrl: '/std/customPage',
            region: 'center',
            margins: '5 5 5 0',
            fields: [
                'objectId',
                'name',
                'code'
            ],
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', minWidth: 320, flex: 1, dataIndex: 'name', sortable: false,
                    renderer: function (value, md, record) {
                        return '<a style="color: #0000FF;" target="_blank" href="' + PageContext.contextPath + '/loadPage/withub.ext.std.customPage.CustomPageDesign?customPageId='
                            + record.get('objectId') + '">' + value + '</a>'
                    }
                },
                {text: '标识', flex: 1, dataIndex: 'code'}
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
            items.push(
                {
                    text: '添加自定义界面',
                    iconCls: 'icon-add',
                    hidden: record.get('depth') == 1,
                    handler: function () {
                        Ext.create('withub.ext.std.customPage.CustomPage', {
                            action: 'create',
                            customPageCategoryId: objectId,
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
                        Ext.create('withub.ext.std.customPage.CustomPageCategory', {
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
                        Ext.create('withub.ext.std.customPage.CustomPageCategory', {
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
                    Ext.create('withub.ext.std.customPage.CustomPage', {
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