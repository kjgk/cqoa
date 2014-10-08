Ext.define('withub.ext.std.entitySequence.EntitySequenceList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.entitySequence.EntitySequenceModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.entitySequence.EntitySequenceModel',
            baseUrl: '/std/entitySequence',
            enablePagginBar: true,
            enableDeleteItem: true,
            border: false,
            columns: [
                {xtype: 'rownumberer', width: 32},
                {text: '名称', width: 100, dataIndex: 'name' },
                {text: '实体', width: 100, dataIndex: 'entity'},
                {text: '固定长度', width: 80, dataIndex: 'fixedLength', align: 'right'},
                {text: '序号属性', width: 120, dataIndex: 'sequenceProperty'},
                {text: '按年份循环序号', width: 100, dataIndex: 'circleSequenceByYear', align: 'center', renderer: function (v) {
                    if (v == '0') {
                        return "否";
                    }
                    else {
                        return "是";
                    }
                }},
                {text: '年份属性', width: 120, dataIndex: 'yearProperty'},
                {text: '描述', flex: 1, minidth: 200, dataIndex: 'description'}
            ],
            tbar: [
                '实体',
                {
                    itemId: 'entity',
                    xtype: 'treecombo',
                    width: 220,
                    treeWidth: 360,
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    emptyText: '请选择实体分类'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryEntitySequence,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.entitySequence.EntitySequence', {
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
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.std.entitySequence.EntitySequence', {
                            action: 'update',
                            objectId: objectId,
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
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryEntitySequence();
    },

    queryEntitySequence: function () {
        var entity = this.gridPanel.down('#entity');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            entityCategoryId: entity.getObjectValue()
        });
        this.gridPanel.getStore().loadPage(1);
    }
});