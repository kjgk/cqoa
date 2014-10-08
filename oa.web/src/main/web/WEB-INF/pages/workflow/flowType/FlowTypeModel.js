Ext.define('withub.ext.workflow.flowType.FlowTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'flowTypeTag',
        'entityName',
        'enable'
    ],
    idProperty: 'objectId'
});