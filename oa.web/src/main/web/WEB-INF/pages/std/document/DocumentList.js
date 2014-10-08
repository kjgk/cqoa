Ext.define('withub.ext.std.document.DocumentList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.document.DocumentModel'],
    initComponent: function () {

        var tbar = [
            '日期',
            {
                itemId: 'date',
                xtype: 'daterange',
                range: '-12m'
            },
            '名称',
            {
                itemId: 'name',
                xtype: 'textfield',
                emptyText: '请输入文档名称'
            },
            {
                xtype: 'button',
                text: '搜索',
                iconCls: 'icon-query',
                handler: this.queryDocument,
                scope: this
            },
            '-',
            {
                xtype: 'button',
                text: '新增',
                iconCls: 'icon-add',
                handler: function () {
                    Ext.create('withub.ext.std.document.Document', {
                        action: 'create',
                        documentTypeId: this.documentTypeId,
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
        ];

        if (Ext.isEmpty(this.documentTypeId) && Ext.isEmpty(this.documentTypeCategoryId)) {
            Ext.Array.insert(tbar, 2, ['类型', {
                itemId: 'documentType',
                xtype: 'treecombo',
                width: 200,
                url: '/std/documentType/loadTree',
                params: {depth: 2},
                enableClear: true,
                emptyText: '请选择文档类型'
            }])
        }


        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.document.DocumentModel',
            baseUrl: '/std/document',
            enablePagginBar: true,
            enableDeleteItem: true,
            enableHseColumnItem: true,
            entity: 'Document',
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {width: 30, renderer: function (value, metaData, record) {
                    var html = '<a href="{0}" target="_blank" class="file-download" style="margin-left: 3px;"><img style="height: 14px;" src="{1}"/></a>';
                    return Ext.String.format(html, Global.contextPath + '/std/file/download?fileInfoId=' + record.get('fileInfoId')
                        , Global.contextPath + '/images/icon/attach.png');
                }},
                {text: '文档名称', flex: 1, dataIndex: 'name', renderer: ExtUtil.linkRenderer('withub.ext.std.document.DocumentView')},
                {text: '版本', width: 50, dataIndex: 'version'},
                {text: '文档类型', width: 130, dataIndex: 'documentType.name', sortable: false},
                {text: '大小', width: 80, dataIndex: 'documentSize', sortable: false, renderer: SWFUpload.speed.formatBytes},
                {text: '上传者', width: 80, dataIndex: 'creator', sortable: false},
                {text: '编写日期', width: 100, dataIndex: 'writeDate', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.DAY)}
            ],
            tbar: tbar
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId'), documentTypeId = record.get('documentType.objectId');
            items.push([
                {
                    text: '查看',
                    iconCls: 'icon-view',
                    handler: function () {
                        ExtUtil.showWindow('withub.ext.std.document.DocumentView', {
                            objectId: objectId
                        });
                    },
                    scope: this
                },
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.document.Document', {
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
                },
                {
                    text: '上传新版本',
                    iconCls: 'icon-version-new',
                    handler: function () {
                        Ext.create('withub.ext.std.document.Document', {
                            action: 'createNewVersion',
                            documentId: objectId,
                            documentTypeId: documentTypeId,
                            listeners: {
                                success: function () {
                                    store.load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '查看历史版本',
                    iconCls: 'icon-document-history',
                    handler: function () {
                        ExtUtil.showWindow('withub.ext.std.document.DocumentHistoryList', {
                            documentId: objectId
                        });
                    },
                    scope: this
                }
            ]);
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryDocument();
    },

    queryDocument: function () {
        var date = this.gridPanel.down('#date');
        var name = this.gridPanel.down('#name');
        var documentType, typeId;
        if (!Ext.isEmpty(this.documentTypeId)) {
            documentType = 'DocumentType';
            typeId = this.documentTypeId;
        } else if (!Ext.isEmpty(this.documentTypeCategoryId)) {
            documentType = 'DocumentTypeCategory';
            typeId = this.documentTypeCategoryId;
        } else {
            documentType = this.gridPanel.down('#documentType').getObjectType();
            typeId = this.gridPanel.down('#documentType').getObjectValue();
        }
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            name: name.getValue(),
            documentType: documentType,
            typeId: typeId
        });
        this.gridPanel.getStore().loadPage(1);
    }
});