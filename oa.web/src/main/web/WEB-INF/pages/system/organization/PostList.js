Ext.define('withub.ext.system.organization.PostList', {
    extend: 'withub.ext.common.ManagerGrid',
    title: '岗位列表',
    model: 'withub.ext.system.organization.PostModel',
    url: '/system/post/list',
    autoQuery: true,
    autoDimension: true,
    maxWidth: 640,
    enablePagginBar: false,
    enableDeleteItem: true,
    initComponent: function () {

        this.extraParams = {
            organizationId: this.organizationId
        };

        this.tbar = [
            {
                xtype: 'button',
                text: '新增',
                iconCls: 'icon-add',
                handler: function () {
                    Ext.create('withub.ext.system.organization.Post', {
                        action: 'create',
                        organizationId: this.organizationId,
                        listeners: {
                            success: function () {
                                this.getStore().load();
                            },
                            scope: this
                        }
                    }).show();
                },
                scope: this
            }
        ];
        this.columns = [
            Ext.create('Ext.grid.RowNumberer'),
            {text: '岗位名称', flex: 1, dataIndex: 'name'},
            {text: '创建时间', width: 120, dataIndex: 'createTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}
        ];

        this.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.organization.Post', {
                            action: 'update',
                            objectId: objectId,
                            organizationId: this.organizationId,
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

Ext.define('withub.ext.system.organization.PostModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'createTime'
    ]
});