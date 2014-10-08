Ext.define('withub.ext.std.bulletin.DraftBulletinList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.bulletin.BulletinModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            entity: 'Bulletin',
            baseUrl: '/std/bulletin',
            enablePagginBar: true,
            enableDeleteItem: true,
            model: 'withub.ext.std.bulletin.BulletinModel',
            border: false,
            columns: [
                {xtype: 'rownumberer'},
                {text: '标题', minWidth: 460, flex: 1, dataIndex: 'title',
                    renderer: function (value, md, record) {
                        return '<a style="color: #0000FF;" target="_blank" href="' + PageContext.contextPath + '/std/bulletin/view.page?objectId='
                            + record.get('objectId') + '">' + value + '</a>'
                    }
                },
                {text: '类型', width: 100, dataIndex: 'bulletinType', sortable: false},
                {text: '有效期限', width: 140, dataIndex: 'effectiveTime', displayType: DisplayType.DateMinute}
            ],
            tbar: [
                '标题',
                {
                    itemId: 'title',
                    xtype: 'textfield',
                    width: 220,
                    emptyText: '请输入标题'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryDraftBulletin,
                    scope: this
                },
                {
                    xtype: 'button',
                    text: '新增',
                    menuTag: 'Add',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.bulletin.Bulletin', {
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
            items.push({
                text: '发布',
                iconCls: 'icon-view',
                handler: function () {
                    ExtUtil.Msg.confirm('确认发布当前公告通告吗?', function (select) {
                        if (select == 'no') {
                            return;
                        }
                        Ext.Ajax.request({
                            url: PageContext.contextPath + "/std/bulletin/issue/" + objectId,
                            success: function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    ExtUtil.Msg.info("发布成功!", function () {
                                        store.load();
                                    });
                                } else {
                                    ExtUtil.msg.error(result.message);
                                }
                            }
                        });
                    });
                },
                scope: this
            }, {
                text: '编辑',
                iconCls: 'icon-edit',
                handler: function () {
                    Ext.create('withub.ext.std.bulletin.Bulletin', {
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
            });
        }, this);

        this.items = this.gridPanel;

        this.callParent();
        this.queryDraftBulletin();
    },

    queryDraftBulletin: function () {
        var title = Ext.ComponentQuery.query('#title', this.gridPanel)[0];
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            title: title.getValue(),
            status: 'Draft',
            issuer: 'currentUser'
        });
        this.gridPanel.getStore().loadPage(1);
    }
});