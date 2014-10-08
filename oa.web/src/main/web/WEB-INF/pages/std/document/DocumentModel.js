Ext.define('withub.ext.std.document.DocumentModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'documentSize',
        'version',
        'creator',
        'fileInfoId',
        {name: 'writeDate', type: 'int'},
        {name: 'documentType.objectId', mapping: 'documentTypeId'},
        {name: 'documentType.name', mapping: 'documentTypeName'},
        {name: 'documentType.code', mapping: 'documentTypeCode'},
        {name: 'organization.name', mapping: 'organizationName'},
        {name: 'organization.objectId', mapping: 'organizationId'},
        {name: 'secrecyLevel.name', mapping: 'secrecyLevelName'}
    ]
});