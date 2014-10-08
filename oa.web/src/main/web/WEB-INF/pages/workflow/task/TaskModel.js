Ext.define('withub.ext.workflow.task.TaskModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'instanceName',
        'flowType',
        'flowNodeType',
        'flowNodeName',
        'result',
        'taskStatusName',
        'taskCreateTime',
        'taskFinishTime',
        'creatorName',
        'organizationName',
        'activity',
        'relatedObjectId',
        'taskStatus',
        'taskArriveTime',
        'taskLocked',
        'taskExpiration',
        'taskSourceType'
    ],
    idProperty: 'objectId'
});