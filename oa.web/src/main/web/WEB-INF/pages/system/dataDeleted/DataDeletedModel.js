Ext.define('withub.ext.system.dataDeleted.DataDeletedModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'entityId',
        'creator',
        'lastEditor',
        'detail',
        'deletedTime'
    ]
});