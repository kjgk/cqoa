Ext.define('withub.ext.std.law.LawIssueOrganizationManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.std.law.LawIssueOrganizationModel'],
    baseUrl: '/std/lawIssueOrganization',

    initComponent: function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '发布机构分类',
            split: true,
            width: 200,
            singleExpand: true,
            margins: '5 0 5 5',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: this.baseUrl
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '发布机构列表',
            enableOrderItem: false,
            enableDeleteItem: true,
            baseUrl: this.baseUrl,
            region: 'center',
            margins: '5 5 5 0',
            model: 'withub.ext.std.law.LawIssueOrganizationModel',
            extraParams: {
                objectId: this.objectId
            },
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '名称', width: 180, dataIndex: 'name'},
                {text: '描述', minWidth: 220, flex: 1, dataIndex: 'description'}

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
                    text: '添加发布机构',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.law.LawIssueOrganization', {
                            action: 'create',
                            parentId: objectId,
                            listeners: {
                                success: function () {
                                    if (record.get('leaf')) {
                                        store.load({
                                            node: store.getNodeById(record.get('parentId'))
                                        });
                                    } else {
                                        store.load({
                                            node: record
                                        });
                                    }
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
                        Ext.create('withub.ext.std.law.LawIssueOrganization', {
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
            var lawCategoryId = record.get('lawCategoryId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.law.lawIssueOrganization', {
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
                }
            );
        }, this);

        this.callParent();
    }
});
