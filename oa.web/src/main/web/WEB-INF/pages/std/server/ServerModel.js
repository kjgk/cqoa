Ext.define('withub.ext.std.server.ServerModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'ip',
        'username',
        'password'
    ],
    idProperty: 'objectId'
});