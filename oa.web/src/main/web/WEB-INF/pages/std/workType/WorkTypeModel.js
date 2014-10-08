Ext.define('withub.ext.std.workType.WorkTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'code',
        'orderNo',
        {name: 'workTypeCategory.objectId', mapping: 'workTypeCategoryId'}
    ]
});