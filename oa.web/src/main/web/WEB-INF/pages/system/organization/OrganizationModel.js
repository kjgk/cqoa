Ext.define('withub.ext.system.organization.OrganizationModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'organizationType',
        'organizationTag',
        'parentOrganization',
        'code',
        'contact',
        'phone',
        {name: 'enterDate', type: 'int'}
    ]
});