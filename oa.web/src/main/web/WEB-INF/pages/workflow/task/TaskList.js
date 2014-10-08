Ext.define('withub.ext.workflow.task.TaskList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.workflow.task.TaskModel'],
    defaultQueryStatusTag: 'Running',
    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.workflow.task.TaskModel',
            url: '/workflow/task/queryCurrentUserTask',
            enablePagginBar: true,
            border: false,
            columns: [
                {xtype: 'rownumberer', locked: true},
                {text: '流程名称', width: 420, dataIndex: 'instanceName', locked: true},
                {text: '流程类型', width: 130, dataIndex: 'flowType'},
                {text: '流程节点', width: 160, dataIndex: 'flowNodeName'},
                {text: '状态', width: 60, dataIndex: 'taskStatusName'},
                {text: '期限(工作小时)', width: 100, dataIndex: 'taskExpiration', align: 'right', hidden: true},
                {text: '发起单位', width: 260, dataIndex: 'organizationName'},
                {text: '发起人', width: 150, dataIndex: 'creatorName'},
                {text: '处理结果', width: 80, dataIndex: 'result'},
                {text: '开始时间', width: 120, dataIndex: 'taskArriveTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)},
                {text: '完成时间', width: 120, dataIndex: 'taskFinishTime', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}

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
                '状态',
                {
                    itemId: 'status',
                    width: 100,
                    xtype: 'codecombo',
                    codeColumnTag: 'TaskStatus',
                    defaultCodeTag: 'Running',
                    showAll: true
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryTask,
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var objectId = record.get('objectId');
            var taskStatus = record.get('taskStatus');
            var activity = record.get('activity');
            var relatedObjectId = record.get('relatedObjectId');
            var flowNodeType = record.get('flowNodeType');
            var taskLocked = record.get('taskLocked');
            items.push(
                {
                    text: '处理',
                    iconCls: 'icon-process',
                    hidden: taskStatus != 'Running',
                    handler: function () {
                        // 审批界面
                        if (activity.indexOf('Approve') != -1) {
                            Ext.create(activity, {
                                objectId: relatedObjectId,
                                taskId: objectId,
                                listeners: {
                                    success: function () {
                                        this.gridPanel.getStore().load();
                                    },
                                    scope: this
                                }
                            }).show();
                        }
                        // 退回修改界面
                        else {
                            Ext.create(activity, {
                                objectId: relatedObjectId,
                                action: 'submit',
                                taskId: objectId,
                                listeners: {
                                    success: function () {
                                        this.gridPanel.getStore().load();
                                    },
                                    scope: this
                                }
                            }).show();
                        }
                    },
                    scope: this
                },
                {
                    text: '查看',
                    iconCls: 'icon-view',
                    hidden: taskStatus != 'Finish',
                    handler: function () {
                        // 审批界面
                        if (activity.indexOf('Approve') != -1) {
                            Ext.create(activity, {
                                objectId: relatedObjectId,
                                taskId: objectId,
                                viewOnly: true,
                                listeners: {
                                    success: function () {
                                        this.gridPanel.getStore().load();
                                    },
                                    scope: this
                                }
                            }).show();
                        }
                        // 退回修改界面
                        else {
                            Ext.create(activity, {
                                objectId: relatedObjectId,
                                action: 'submit',
                                viewOnly: true,
                                taskId: objectId,
                                listeners: {
                                    success: function () {
                                        this.gridPanel.getStore().load();
                                    },
                                    scope: this
                                }
                            }).show();
                        }
                    },
                    scope: this
                },
                {
                    text: '查看审批流程',
                    iconCls: 'icon-view',
                    handler: function () {
                        ExtUtil.showWindow('withub.ext.workflow.instance.InstanceView', {
                            taskId: objectId
                        }, {border: true});
                    },
                    scope: this
                },
                {
                    text: '回滚',
                    iconCls: 'icon-process',
                    hidden: !(flowNodeType == 'AndSign' && taskStatus == 'Finish' && taskLocked == 0),
                    handler: function () {
                        ExtUtil.confirm('确认回滚当前任务吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.MessageBox.wait('正在将任务回滚 ...', 'ETS');
                            Ext.Ajax.request({
                                url: Global.contextPath + "/workflow/task/rollback/" + objectId,
                                success: function (response) {
                                    Ext.MessageBox.updateProgress(1);
                                    Ext.MessageBox.hide();
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.showSuccessMsgBox("任务回滚成功!", function () {
                                            this.gridPanel.getStore().load();
                                        }, this);
                                    } else {
                                        ExtUtil.showErrorMsgBox(result.message);
                                    }
                                },
                                scope: this
                            });
                        }, this);
                    },
                    scope: this
                },
                {
                    text: '转发',
                    iconCls: 'icon-edit',
                    hidden: taskStatus != 'Running',
                    menuTag: 'Transmit',
                    handler: function () {
                        Ext.create('withub.ext.workflow.task.TaskTransmit', {
                            taskId: objectId,
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

        this.queryTask();
    },

    queryTask: function () {
        var date = this.gridPanel.down("#date");
        var flowType = this.gridPanel.down("#flowType");
        var status = this.gridPanel.down('#status');

        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            beginDate: date.getBeginDate(),
            endDate: date.getEndDate(),
            flowType: flowType.getValue(),
            statusId: status.getValue(),
            statusTag: this.defaultQueryStatusTag
        });
        this.defaultQueryStatusTag = '';
        this.gridPanel.getStore().loadPage(1);
    }
});