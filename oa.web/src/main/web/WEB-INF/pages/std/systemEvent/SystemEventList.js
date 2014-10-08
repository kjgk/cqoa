Ext.define('withub.ext.std.systemEvent.SystemEventList', {
    extend:'Ext.Viewport',
    closable:true,
    layout:'fit',
    requires:['withub.ext.std.systemEvent.SystemEventModel'],
    initComponent:function () {
        var menu = [];
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            enableOrderItem:true,
            enableDeleteItem:true,
            entity:'SystemEvent',
            baseUrl:'/std/systemEvent',
            model:'withub.ext.std.systemEvent.SystemEventModel',
            enablePagginBar:true,
            border:false,
            columns:[
                Ext.create('Ext.grid.RowNumberer'),
                {text:'事件名称', width:180, dataIndex:'name'},
                {text:'优先级', dataIndex:'priority', width:60},
                {text:'事件类名', minWidth:400, flex:1, dataIndex:'className'},
                {text:'延迟', width:40, dataIndex:'delay', align:'center', renderer:RendererUtil.booleanValueRenderer()},
                {text:'对应的实体属性', width:140, dataIndex:'entityProperty'},
                {text:'对应的用户属性', width:140, dataIndex:'userProperty'},
                {text:'启用', width:60, dataIndex:'enableNotify', align:'center',
                    renderer:RendererUtil.booleanValueRenderer()
                }
            ],
            tbar:[
                '事件',
                {
                    itemId:'name',
                    xtype:'textfield',
                    width:180,
                    emptyText:'请输入事件名称'
                },
                {
                    xtype:'button',
                    text:'搜索',
                    iconCls:'icon-query',
                    handler:this.querySystemEvent,
                    scope:this
                },
                '-',
                {
                    xtype:'button',
                    text:'新增',
                    iconCls:'icon-add',
                    handler:function () {
                        Ext.create('withub.ext.std.systemEvent.SystemEvent', {
                            action:'create',
                            listeners:{
                                success:function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            var name = record.get('name');
            items.push(
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function () {
                        Ext.create('withub.ext.std.systemEvent.SystemEvent', {
                            action:'update',
                            objectId:objectId,
                            listeners:{
                                success:function () {
                                    store.load();
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                },
                {
                    text:'配置通知模板',
                    iconCls:'icon-edit',
                    handler:function () {
                        Ext.create('withub.ext.std.systemEvent.EventNotifyServiceTypeTree', {
                            systemEventId:objectId,
                            listeners:{
                                success:function () {
                                    this.fireEvent('success');
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.querySystemEvent();
    },

    querySystemEvent:function () {
        var name = Ext.ComponentQuery.query('#name', this.gridPanel)[0];
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name:name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});