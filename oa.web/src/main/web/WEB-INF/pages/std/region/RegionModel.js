Ext.define('withub.ext.std.region.RegionModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        {name: 'regionType.objectId', mapping: 'regionTypeId'},
        {name: 'regionType.name', mapping: 'regionTypeName'},
        'description',
        'orderNo'
    ]
});