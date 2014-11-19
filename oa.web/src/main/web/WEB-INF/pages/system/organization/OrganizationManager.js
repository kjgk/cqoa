Ext.define('withub.ext.system.organization.OrganizationManager', {
    extend: 'Ext.Viewport',
    layout: 'border',
    closable: true,
    requires: ['withub.ext.system.organization.OrganizationModel'],
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '组织机构',
            userInterfaceTag: 'OrganizationManager',
            split: true,
            width: 260,
            singleExpand: true,
            margins: '5 0 0 0',
            enableOrderItem: true,
            enableDeleteItem: true,
            baseUrl: '/system/organization'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title: '组织机构列表',
            userInterfaceTag: 'OrganizationList',
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'Organization',
            region: 'center',
            margins: '5 0 0 0',
            baseUrl: '/system/organization',
            model: 'withub.ext.system.organization.OrganizationModel',
            treePanel: this.treePanel,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '组织机构名称', minWidth: 320, flex: 1, dataIndex: 'name', sortable: false},
                {text: '联系人', width: 160, dataIndex: 'contact', sortable: false},
                {text: '联系电话', width: 100, dataIndex: 'phone', sortable: false}
            ]
        });

        this.items = [this.treePanel, this.gridPanel];

        this.treePanel.on('select', function (tree, record, index) {
            var store = this.gridPanel.getStore();
            store.getProxy().extraParams['id'] = record.get('objectId');
            store.load();
        }, this);

        this.treePanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId') , organizationTag = record.get('attributes')['organizationTag']
                , organizationTypeId = record.get('attributes')['organizationTypeId'];
            items.push({
                text: '添加',
                iconCls: 'icon-add',
                menuTag: 'Add',
                handler: function () {
                    Ext.create('withub.ext.system.organization.Organization', {
                        action: 'create',
                        parentId: objectId,
                        organizationTag: organizationTag,
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
                                this.gridPanel.getStore().load();
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
                menuTag: 'Edit',
                handler: function () {
                    Ext.create('withub.ext.system.organization.Organization', {
                        action: 'update',
                        objectId: objectId,
                        depth: record.get('depth'),
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
                text: '同步组织机构',
                iconCls: 'icon-refresh',
                hidden: record.get('depth') > 1,
                handler: function () {
                    Ext.Ajax.request({
                        url: PageContext.contextPath + "/oa/dataImport/organization",
                        success: function (response) {
                            var result = Ext.decode(response.responseText);
                            if (result.success) {
                                ExtUtil.Msg.info("同步成功!", function () {
                                    store.load({
                                        node: record
                                    });
                                });
                            } else {
                                ExtUtil.Msg.error(result.message);
                            }
                        }
                    });
                },
                scope: this
            });
        }, this);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var me = this;
            var objectId = record.get('objectId');
            var name = record.get('name');
            var accountId = record.get('accountId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    menuTag: 'Edit',
                    handler: function () {
                        Ext.create('withub.ext.system.organization.Organization', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
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