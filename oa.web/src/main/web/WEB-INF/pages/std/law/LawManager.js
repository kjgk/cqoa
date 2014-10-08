Ext.define('withub.ext.std.law.LawManager', {
    extend:'Ext.Viewport',
    layout:'border',
    closable:true,
    requires:['withub.ext.std.law.LawModel'],
    baseUrl:'/std/law',

    initComponent:function () {
        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region:'west',
            title:'法律法规分类',
            split:true,
            width:360,
            singleExpand:true,
            margins:'5 0 5 5',
            enableOrderItem:true,
            enableDeleteItem:true,
            baseUrl:this.baseUrl
        });

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            title:'法律法规列表',
            enableOrderItem:true,
            enableDeleteItem:true,
            entity:'Law',
            baseUrl:this.baseUrl,
            region:'center',
            margins:'5 5 5 0',
            model:'withub.ext.std.law.LawModel',
            columns:[
                Ext.create('Ext.grid.RowNumberer'),
                {text:'法律法规', flex:1, dataIndex:'name', sortable:false, renderer:function (value, metaData, record) {
                    var html = '<a href="' + Global.contextPath + '/std/law/view/'
                        + record.get('objectId') + '.page" target="_blank">'
                        + Ext.util.Format.htmlEncode(record.get('name')) + '</a>'
                    return html;
                }}
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
                    text:'添加法律法规',
                    iconCls:'icon-add',
                    hidden:record.get('depth') == 1,
                    handler:function () {
                        Ext.create('withub.ext.std.law.Law', {
                            action:'create',
                            lawCategoryId:objectId,
                            listeners:{
                                success:function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope:this
                            }
                        }).show();
                    },
                    scope:this
                },
                {
                    text:'添加分类',
                    iconCls:'icon-add',
                    handler:function () {
                        Ext.create('withub.ext.std.law.LawCategory', {
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
                    }
                },
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    hidden:record.get('depth') == 1,
                    handler:function () {
                        Ext.create('withub.ext.std.law.LawCategory', {
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
            var lawCategoryId = record.get('lawCategoryId');
            items.push(
                {
                    text:'编辑',
                    iconCls:'icon-edit',
                    handler:function () {
                        Ext.create('withub.ext.std.law.Law', {
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
                }
            );
        }, this);

        this.callParent();
    }
});