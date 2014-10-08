Ext.define('withub.ext.std.law.LawList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.law.LawModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            entity: 'Law',
            url: '/std/law/queryLaw',
            enablePagginBar: true,
            model: 'withub.ext.std.law.LawModel',
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '法律法规名称', flex: 1, dataIndex: 'name', sortable: false, renderer: function (value, metaData, record) {
                    var html = '<a href="' + Global.contextPath + '/std/law/view/'
                        + record.get('objectId') + '.page" target="_blank">'
                        + record.get('name') + '</a>'
                    return html;
                }},
                {text: '发布机构', width: 160, dataIndex: 'organization', sortable: false},
                {text: '发布时间', width: 100, dataIndex: 'issueTime', renderer: ExtUtil.dateRenderer(DateFormat.DAY)}
            ],
            tbar: [
                '法律法规分类',
                {
                    itemId: 'lawCategory',
                    xtype: 'treecombo',
                    width: 240,
                    treeWidth: 300,
                    url: '/std/law/loadTree',
                    params: {depth: 2},
                    emptyText: '请选择法律法规分类'
                },
                '名称',
                {
                    itemId: 'name',
                    xtype: 'textfield',
                    width: 250,
                    emptyText: '请输入法律法规名称'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryLaw,
                    scope: this
                }
            ]
        });

        this.items = this.gridPanel;

        this.callParent();
        this.queryLaw();
    },

    queryLaw: function () {
        var lawCategory = this.gridPanel.down('#lawCategory');
        var name = this.gridPanel.down('#name');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            lawCategoryId: lawCategory.getObjectValue(),
            name: name.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});