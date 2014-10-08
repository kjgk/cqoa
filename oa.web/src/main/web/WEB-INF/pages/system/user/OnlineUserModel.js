Ext.define('withub.ext.system.user.OnlineUserModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'userObjectId',
        'name',
        'account',
        'code',
        'organization',
        'loginTime',
        'clientIdentityCode',
        'onlineTime',
        'accountId' ,
        'loginType'
    ]
});