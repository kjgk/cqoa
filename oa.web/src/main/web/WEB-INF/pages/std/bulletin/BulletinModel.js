Ext.define('withub.ext.std.bulletin.BulletinModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'title',
        'bulletinType',
        'issuer',
        'issueTime',
        'content',
        'organization',
        'issueOrganization',
        'effectiveTime'
    ]
});