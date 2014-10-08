Ext.define('withub.ext.system.authorizationCluster.AuthorizationClusterModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'allowRepetition',
        'enable',
        'priority',
        'description'
    ],
    idProperty: 'objectId'
});