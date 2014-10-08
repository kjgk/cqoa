Ext.define('withub.ext.system.user.SecurityLogModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'account',
        'organization',
        'loginTime',
        'clientIdentityCode',
        'logoutTime',
        'loginType'
    ]
});