Ext.define('withub.ext.workflow.flowType.FlowTypeList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.workflow.flowType.FlowTypeModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.workflow.flowType.FlowTypeModel',
            baseUrl: '/workflow/flowType',
            enablePagginBar: true,
            enableOrderItem: true,
            enableDeleteItem: true,
            entity: 'FlowType',
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '流程标识', width: 180, dataIndex: 'flowTypeTag', sortable: true},
                {text: '流程名称', minWidth: 400, flex: 1, dataIndex: 'name', sortable: false},
                {text: '对应的实体名称', width: 200, dataIndex: 'entityName'},
                {text: '启用', width: 60, dataIndex: 'enable', align: 'center', displayType: DisplayType.BooleanValue}
            ],
            tbar: [
                '流程名称',
                {
                    itemId: 'name',
                    xtype: 'textfield',
                    width: 180,
                    emptyText: '请输入流程名称'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryFlowType,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.workflow.flowType.FlowType', {
                            action: 'create',
                            flowTypeTag: this.flowTypeTag,
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
                    text: '编辑流程图',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var id = 'diagram-' + record.get('objectId');
                        var tabPanel = window.parent.Ext.getCmp('tab-panel');
                        var frame = tabPanel.getComponent(id);
                        if (!frame) {
                            frame = new Ext.ux.IFrame({
                                id: id,
                                closable: true,
                                title: record.get('name'),
                                src: "/diagram.html#/" + record.get('objectId')
//                                src: "../dist/diagram.html#/" + record.get('objectId')
                            });
                            frame.on('afterrender', function () {
                                frame.el.mask('界面加载中...');
                            });
                            tabPanel.add(frame);
                        }
                        tabPanel.setActiveTab(frame);
                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.workflow.flowType.FlowType', {
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

        this.queryFlowType();
    },

    queryFlowType: function () {
        var name = this.gridPanel.down("#name");
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});