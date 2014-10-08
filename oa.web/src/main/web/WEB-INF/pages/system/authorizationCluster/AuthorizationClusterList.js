Ext.define('withub.ext.system.authorizationCluster.AuthorizationClusterList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.authorizationCluster.AuthorizationClusterModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.authorizationCluster.AuthorizationClusterModel',
            baseUrl: '/system/authorizationCluster',
            enablePagginBar: true,
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'com.withub.model.system.po.AuthorizationCluster',
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '授权对象', width: 180, dataIndex: 'name'},
                {text: '允许重复', width: 80, dataIndex: 'allowRepetition', displayType: DisplayType.BooleanValue, align: 'center'},
                {text: '优先级', width: 60, dataIndex: 'priority', align: 'center'},
                {text: '描述', flex: 1, width: 180, dataIndex: 'description', sortable: false},
                {text: '启用', width: 60, dataIndex: 'enable', displayType: DisplayType.BooleanValue, align: 'center'}
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        '名称',
                        {
                            itemId: 'name',
                            xtype: 'textfield',
                            width: 180,
                            emptyText: '请输入名称'
                        },
                        {
                            xtype: 'button',
                            text: '搜索',
                            iconCls: 'icon-query',
                            handler: this.queryAuthorizationCluster,
                            scope: this
                        },
                        '-',
                        {
                            xtype: 'button',
                            text: '新增',
                            iconCls: 'icon-add',
                            handler: function () {
                                Ext.create('withub.ext.system.authorizationCluster.AuthorizationCluster', {
                                    action: 'create',
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
                    ]
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '分配权限',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.authorizationCluster.PermissionAssign', {
                            authorizationClusterId: objectId
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.authorizationCluster.AuthorizationCluster', {
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

        this.items = this.gridPanel;

        this.callParent();

        this.queryAuthorizationCluster();
    },

    queryAuthorizationCluster: function () {
        var name = this.gridPanel.down("#name");
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});