Ext.define('withub.ext.std.entityCacheConfig.EntityCacheConfigModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'entity',
        'cacheKey',
        'cacheCount',
        'timestampProperty',
        'documentType',
        'enable'
    ]
});