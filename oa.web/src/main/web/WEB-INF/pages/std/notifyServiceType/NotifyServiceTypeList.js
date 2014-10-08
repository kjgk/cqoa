Ext.define('withub.ext.std.notifyServiceType.NotifyServiceTypeList', {
    extend:'Ext.Viewport',
    closable:true,
    layout:'fit',
    requires:['withub.ext.std.notifyServiceType.NotifyServiceTypeModel'],
    initComponent:function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model:'withub.ext.std.notifyServiceType.NotifyServiceTypeModel',
            baseUrl:'/std/notifyServiceType',
            enablePagginBar:true,
            enableDeleteItem:true,
            border:false,
            columns:[
                {xtype:'rownumberer', width:32},
                {text:'名称', width:150, dataIndex:'name' },
                {text:'标识', width:150, dataIndex:'notifyServiceTypeTag'},
                {text:'用户接收地址', width:200, dataIndex:'userPropertyAddress'},
                {text:'自定义通知时间', width:100,align:'center', dataIndex:'customiseNotifyTime',displayType:DisplayType.BooleanValue},
                {text:'启用', width:40 ,align:'center',dataIndex:'enable',displayType:DisplayType.BooleanValue}
            ],
            tbar:[
                '名称',
                {
                    xtype:'textfield',
                    itemId:'name',
                    width:100,
                    emptyText:'请选择名称'
                },
                {
                    xtype:'button',
                    text:'搜索',
                    iconCls:'icon-query',
                    handler:this.query,
                    scope:this
                },
                '-',
                {
                    xtype:'button',
                    text:'新增',
                    iconCls:'icon-add',
                    handler:function () {
                        Ext.create('withub.ext.std.notifyServiceType.NotifyServiceType', {
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
            items.push(
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function () {
                        Ext.create('withub.ext.std.notifyServiceType.NotifyServiceType', {
                            action:'update',
                            objectId:objectId,
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
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.query();
    },

    query:function () {
        var name = this.gridPanel.down('#name');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            name:name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});