Ext.define('withub.ext.std.bulletin.BulletinList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.bulletin.BulletinModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            entity: 'Bulletin',
            baseUrl: '/std/bulletin',
            enablePagginBar: true,
            userInterfaceTag: 'BulletinList',
            model: 'withub.ext.std.bulletin.BulletinModel',
            enableDeleteItem: true,
            border: false,
            columns: [
                {xtype: 'rownumberer'},
                {text: '标题', minWidth: 360, flex: 1, dataIndex: 'title',
                    renderer: function (value, md, record) {
                        return '<a style="color: #0000FF;" target="_blank" href="' + PageContext.contextPath + '/std/bulletin/view.page?objectId='
                            + record.get('objectId') + '">' + value + '</a>'
                    }
                },
                {text: '类型', width: 80, dataIndex: 'bulletinType'},
                {text: '有效期限', width: 140, dataIndex: 'effectiveTime', displayType: DisplayType.DateMinute},
                {text: '发布单位', width: 240, dataIndex: 'issueOrganization'},
                {text: '发布人', width: 60, dataIndex: 'issuer'},
                {text: '发布时间', width: 140, dataIndex: 'issueTime', displayType: DisplayType.DateMinute}
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    itemId: 'querybar',
                    items: [
                        '日期',
                        {
                            itemId: 'date',
                            xtype: 'daterange',
                            range: '-2m',
                            width: 200
                        },
                        '组织机构',
                        {
                            itemId: 'organization',
                            xtype: 'treecombo',
                            width: 220,
                            treeWidth: 360,
                            url: '/system/organization/loadTree',
                            showPathName: true,
                            pathNameDepth: 3,
                            emptyText: '请选择组织机构'
                        },
                        '类型',
                        {
                            itemId: 'bulletinType',
                            width: 100,
                            xtype: 'codecombo',
                            codeColumnTag: 'BulletinType',
                            showAll: true,
                            hidden: this.bulletinType != undefined
                        },
                        '标题',
                        {
                            itemId: 'title',
                            xtype: 'textfield',
                            width: 180,
                            emptyText: '请输入标题'
                        },
                        {
                            xtype: 'button',
                            text: '搜索',
                            iconCls: 'icon-query',
                            handler: this.queryBulletin,
                            scope: this
                        },
                        {
                            xtype: 'button',
                            text: '新增',
                            menuTag: 'Add',
                            iconCls: 'icon-add',
                            handler: function () {
                                Ext.create('withub.ext.std.bulletin.Bulletin', {
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
                    menuTag: 'Edit',
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
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();
        this.queryBulletin();
    },

    queryBulletin: function () {
        var date = this.gridPanel.down('#date');
        var organization = this.gridPanel.down('#organization');
        var bulletinType = this.gridPanel.down('#bulletinType');
        var title = this.gridPanel.down('#title');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            status: 'Issue',
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            organizationId: organization.getObjectValue(),
            bulletinType: bulletinType.getValue(),
            title: title.getValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});