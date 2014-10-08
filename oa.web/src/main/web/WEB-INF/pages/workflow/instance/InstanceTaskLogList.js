Ext.define('withub.ext.workflow.instance.InstanceTaskLogList', {
    extend: 'withub.ext.common.ManagerGrid',
    requires: ['withub.ext.workflow.instance.InstanceTaskLogModel'],
    url: '/workflow/instance/listInstanceTaskLog',
    //userInterfaceTag: 'TaskList',
    autoQuery: true,
    onlyShowApproveLog: false,
    height: 480,
    width: 720,
    title: '审批记录查看',

    initComponent: function () {

        this.model = 'withub.ext.workflow.instance.InstanceTaskLogModel';
        this.extraParams = {
            onlyShowApproveLog: this.onlyShowApproveLog,
            instanceId: this.instanceId,
            taskId: this.taskId,
            relatedObjectId: this.relatedObjectId
        };

        this.columns = [
            Ext.create('Ext.grid.RowNumberer'),
            {text: '流程节点', width: 220, dataIndex: 'flowNodeName'},
            {text: '处理人', width: 80, dataIndex: 'handler'},
            {text: '处理结果', width: 60, dataIndex: 'result'},
            {text: '处理意见', flex: 1, dataIndex: 'opinion'},
            {text: '完成时间', width: 120, dataIndex: 'finishTime', renderer: ExtUtil.dateRenderer(DateFormat.MINUTE)}
        ];

        this.on('createcontextmenu', function (items, store, record, index, event) {
            var taskId = record.get('taskId');
            var finishTime = record.get('finishTime');
            items.push(
                {
                    text: '转发',
                    iconCls: 'icon-edit',
                    disabled: finishTime != "",
                    menuTag: 'Transmit',
                    handler: function () {
                        Ext.create('withub.ext.workflow.task.TaskTransmit', {
                            taskId: taskId,
                            listeners: {
                                success: function () {
                                    this.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                }
            );
        }, this);

        this.callParent();
    }
});