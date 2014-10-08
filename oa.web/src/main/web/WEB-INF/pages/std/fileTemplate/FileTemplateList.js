Ext.define('withub.ext.std.fileTemplate.FileTemplateList', {
    extend: 'Ext.Panel',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.fileTemplate.FileTemplateModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.fileTemplate.FileTemplateModel',
            baseUrl: '/std/fileTemplate',
            enablePagginBar: true,
            enableDeleteItem: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '模板标识', width: 320, dataIndex: 'fileTemplateTag', sortable: false},
                {text: '模板名称', minWidth: 280, flex: 1, dataIndex: 'name'},
                {text: '最后更新时间', width: 150, dataIndex: 'lastUpdateTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}
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
                            width: 220,
                            emptyText: '请输入文件模板名称'
                        },
                        {
                            xtype: 'button',
                            text: '搜索',
                            iconCls: 'icon-query',
                            handler: this.queryFileTemplate,
                            scope: this
                        },
                        '-',
                        {
                            xtype: 'button',
                            text: '新增',
                            iconCls: 'icon-add',
                            handler: function () {
                                Ext.create('withub.ext.std.fileTemplate.FileTemplate', {
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
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.fileTemplate.FileTemplate', {
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

        this.queryFileTemplate();
    },

    queryFileTemplate: function () {
        var name = Ext.ComponentQuery.query('#name', this.gridPanel)[0];
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});