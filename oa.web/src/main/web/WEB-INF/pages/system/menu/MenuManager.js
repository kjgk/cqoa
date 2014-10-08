Ext.define('withub.ext.system.menu.MenuManager', {
    extend:'Ext.Viewport',
    layout:'border',
    closable:true,
    requires:['withub.ext.system.menu.MenuModel'],
    initComponent:function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region:'west',
            title:'菜单',
            split:true,
            width:200,
            singleExpand:true,
            margins:'5 0 0 0',
            enableOrderItem:true,
            enableDeleteItem:true,
            baseUrl:'/system/menu'
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title:'菜单列表',
            enableOrderItem:true,
            enableDeleteItem:true,
            entity:'Menu',
            baseUrl:'/system/menu',
            region:'center',
            margins:'5 0 0 0',
            model:'withub.ext.system.menu.MenuModel',
            columns:[
                Ext.create('Ext.grid.RowNumberer'),
                {text:'菜单名称', width:210, dataIndex:'name', sortable:false},
                {text:'URL', flex:1, dataIndex:'url', sortable:false}
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
                        Ext.create('withub.ext.system.menu.Menu', {
                            action:'create',
                            parentId:objectId,
                            listeners:{
                                success:function () {
                                    if (record.get('leaf')) {
                                        store.load({
                                            node:store.getNodeById(record.get('parentId'))
                                        });
                                    } else {
                                        store.load({
                                            node:record
                                        });
                                    }
                                    this.gridPanel.getStore().load();
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                }
            );
            items.push(
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    hidden:record.get('depth') == 1,
                    handler:function () {
                        Ext.create('withub.ext.system.menu.Menu', {
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
                })
        }, this);

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push({
                text:'编辑',
                iconCls:'icon-edit',
                handler:function () {
                    Ext.create('withub.ext.system.menu.Menu', {
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