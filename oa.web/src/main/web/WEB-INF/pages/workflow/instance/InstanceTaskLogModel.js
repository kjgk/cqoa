Ext.define('withub.ext.workflow.instance.InstanceTaskLogModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'taskId',
        'flowNodeName',
        'handler',
        'result',
        'opinion',
        'finishTime'
    ],
    idProperty: 'objectId'
});