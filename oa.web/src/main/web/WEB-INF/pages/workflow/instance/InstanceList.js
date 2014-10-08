Ext.define('withub.ext.workflow.instance.InstanceList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    defaultQueryStatusTag: 'Running',
    requires: ['withub.ext.workflow.instance.InstanceModel'],
    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.workflow.instance.InstanceModel',
            baseUrl: '/workflow/instance',
            enablePagginBar: true,
            userInterfaceTag: 'InstanceList',
            enableDeleteItem: true,
            border: false,
            columns: [
                {xtype: 'rownumberer', locked: true},
                {text: '流程名称', width: 520, dataIndex: 'name', locked: true, renderer: ExtUtil.linkRenderer('withub.ext.workflow.instance.InstanceView')},
                {text: '当前流程节点', width: 130, dataIndex: 'flowNode', locked: true},
                {text: '流程类型', width: 120, dataIndex: 'flowType'},
                {text: '当前处理人', width: 150, dataIndex: 'handler'},
                {text: '发起单位', width: 240, dataIndex: 'organization'},
                {text: '发起人', width: 150, dataIndex: 'creator'},
                {text: '状态', width: 60, dataIndex: 'status'},
                {text: '开始时间', width: 120, dataIndex: 'createTime', sortable: false, displayType: DisplayType.DateMinute},
                {text: '完成时间', width: 120, dataIndex: 'finishTime', sortable: false, displayType: DisplayType.DateMinute}
            ],
            tbar: [
                '日期',
                {
                    itemId: 'date',
                    xtype: 'daterange',
                    width: 200
                },
                '流程类型',
                {
                    itemId: 'flowType',
                    xtype: 'simplecombo',
                    width: 150,
                    entity: 'FlowType',
                    order: 'orderNo',
                    showAll: true,
                    emptyText: '请选择流程类型'
                },
                '组织机构',
                {
                    itemId: 'organization',
                    xtype: 'treecombo',
                    width: 240,
                    url: '/system/organization/loadTree',
                    showPathName: true,
                    pathNameDepth: 3,
                    emptyText: '请选择组织机构'
                },
                '状态',
                {
                    itemId: 'status',
                    width: 100,
                    xtype: 'codecombo',
                    codeColumnTag: 'InstanceStatus',
                    defaultCodeTag: 'Running',
                    showAll: true
                },
                {
                    itemId: 'user',
                    xtype: 'textfield',
                    width: 120,
                    emptyText: '请输入发起人姓名'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryInstance,
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            var taskStatus = record.get('taskStatus');
            var activity = record.get('activity');

            items.push(
                {
                    text: '查看',
                    iconCls: 'icon-view',
                    handler: function () {
                        ExtUtil.showWindow('withub.ext.workflow.instance.InstanceView', {
                            objectId: objectId
                        }, {border: true});
                    },
                    scope: this
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryInstance();
    },

    queryInstance: function () {
        var date = this.gridPanel.down("#date");
        var flowType = this.gridPanel.down("#flowType");
        var organization = this.gridPanel.down('#organization');
        var status = this.gridPanel.down('#status');
        var user = this.gridPanel.down('#user');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            flowType: flowType.getValue(),
            statusId: status.getValue(),
            organizationId: organization.getObjectValue(),
            statusTag: this.defaultQueryStatusTag,
            user: user.getValue()
        });
        this.defaultQueryStatusTag = '';
        this.gridPanel.getStore().loadPage(1);
    }
});