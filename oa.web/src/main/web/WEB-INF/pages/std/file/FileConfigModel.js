Ext.define('withub.ext.std.file.FileConfigModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        {name: 'entity.name', mapping: 'entityName'},
        {name: 'server.name', mapping: 'serverName'},
        'serverPath'
    ],
    idProperty: 'objectId'
});