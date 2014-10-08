Ext.define('withub.ext.workflow.instance.InstanceModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'flowType',
        'result',
        'status',
        'createTime',
        'finishTime',
        'flowNode',
        'handler',
        'organization',
        'creator'
    ],
    idProperty: 'objectId'
});