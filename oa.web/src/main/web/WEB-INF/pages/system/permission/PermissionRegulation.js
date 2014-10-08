Ext.define('withub.ext.system.permission.PermissionRegulation', {
    extend: 'withub.ext.common.Window',
    title: '权限规则',
    width: 500,
    baseUrl: '/system/permissionRegulation',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 50,
                    allowBlank: false
                },
                {
                    fieldLabel: '实体属性',
                    name: 'entityProperty',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '数据类型',
                    name: 'propertyDataType.objectId',
                    codeColumnTag: 'PropertyDataType',
                    emptyText: '请选择数据类型',
                    allowBlank: false
                },
                {
                    fieldLabel: '用户属性',
                    name: 'userProperty',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    fieldLabel: '实体属性值',
                    name: 'entityPropertyValue',
                    maxLength: 100,
                    allowBlank: true
                },
                {
                    xtype: 'codecombo',
                    fieldLabel: '操作符',
                    name: 'expressionOperation.objectId',
                    codeColumnTag: 'ExpressionOperation',
                    emptyText: '请选择操作符',
                    allowBlank: false
                },
                {
                    fieldLabel: '规则',
                    xtype: 'textarea',
                    name: 'regulation',
                    height: 300
                },
                {
                    xtype: 'hidden',
                    name: 'parent.objectId',
                    value: this.parentId
                },
                {
                    xtype: 'hidden',
                    name: 'permission.objectId',
                    value: this.permissionId
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});


