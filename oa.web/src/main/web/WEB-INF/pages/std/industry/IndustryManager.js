Ext.define('withub.ext.std.industry.IndustryManager', {
    extend:'Ext.Viewport',
    layout:'border',
    closable:true,
    requires:['withub.ext.std.industry.IndustryModel'],
    initComponent:function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region:'west',
            title:'行业分类',
            split:true,
            width:320,
            singleExpand:true,
            margins:'5 0 5 5',
            enableOrderItem:true,
            enableDeleteItem:true,
            baseUrl:'/std/industry'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title:'行业分类列表',
            enableOrderItem:true,
            enableDeleteItem:true,
            entity:'Industry',
            baseUrl:'/std/industry',
            region:'center',
            margins:'5 5 5 0',
            model:'withub.ext.std.industry.IndustryModel',
            columns:[
                Ext.create('Ext.grid.RowNumberer'),
                {text:'行业名称', flex:1, dataIndex:'name', sortable:false}
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
                    text:'添加',
                    iconCls:'icon-add',
                    handler:function () {
                        Ext.create('withub.ext.std.industry.Industry', {
                            action:'create',
                            parentId:objectId,
                            listeners:{
                                success:function () {
                                    store.load({
                                        node:record
                                    });
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                },
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function () {
                        Ext.create('withub.ext.std.industry.Industry', {
                            action:'update',
                            objectId:objectId,
                            listeners:{
                                success:function () {
                                    store.load({
                                        node:store.getNodeById(record.get('parentId'))
                                    });
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                }
            );
        }, this);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text:'编辑',
                iconCls:'icon-edit',
                handler:function () {
                    Ext.create('withub.ext.std.industry.Industry', {
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
            });
        }, this);
        this.callParent();
    }
});