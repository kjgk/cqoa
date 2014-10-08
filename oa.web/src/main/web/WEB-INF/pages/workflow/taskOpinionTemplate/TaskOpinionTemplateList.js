Ext.define('withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplateList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplateModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplateModel',
            baseUrl: '/workflow/taskOpinionTemplate',
            enablePagginBar: true,
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'TaskOpinionTemplate',
            border: false,
            columns: [
                {xtype: 'rownumberer', width: 32},
                {text: '意见', minWidth: 600, flex: 1, dataIndex: 'opinion'},
                {text: '创建时间', width: 160, dataIndex: 'createTime', displayType: DisplayType.DateSecond}
            ],
            tbar: [
                '意见',
                {
                    xtype: 'textfield',
                    itemId: 'opinion',
                    width: 200,
                    emptyText: '请输入意见'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.query,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplate', {
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
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplate', {
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

        this.items = this.gridPanel;

        this.callParent();

        this.query();

    },


    query: function () {
        var opinion = this.gridPanel.down('#opinion');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            opinion: opinion.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});