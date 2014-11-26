Ext.define('withub.ext.system.user.User', {
    extend: 'withub.ext.common.Window',
    title: '用户',
    baseUrl: '/system/user',
    width: 410,
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 5,
            border: false,
            baseCls: 'x-plain',
            layout: 'hbox',
            trackResetOnLoad: true,
            items: [
                {
                    baseCls: 'x-plain',
                    flex: 1,
                    items: [
                        {
                            xtype: 'fieldset',
                            title: '基本信息',
                            defaultType: 'textfield',
                            defaults: {
                                labelWidth: 90,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    fieldLabel: '姓名',
                                    name: 'name',
                                    maxLength: 30,
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '员工编号',
                                    name: 'code',
                                    maxLength: 30,
                                    allowBlank: true
                                },
                                {
                                    xtype: 'coderadiogroup',
                                    fieldLabel: '性别',
                                    name: 'sex.objectId',
                                    codeColumnTag: 'Sex',
                                    columns: 3,
                                    allowBlank: false
                                },
                                {
                                    xtype: 'datefield',
                                    format: 'Y-m-d',
                                    fieldLabel: '出生日期',
                                    name: 'birthday',
                                    allowBlank: true
                                },
                                {
                                    xtype: 'codecombo',
                                    fieldLabel: '民族',
                                    name: 'nation.objectId',
                                    codeColumnTag: 'nation',
                                    allowBlank: true
                                },
                                {
                                    xtype: 'treecombo',
                                    fieldLabel: '组织机构',
                                    name: 'organization.objectId',
                                    url: '/system/organization/loadTree',
//                                    value: PageContext.currentUser['organizationId'],
                                    showPathName: true,
                                    pathNameDepth: 3,
                                    allowBlank: false,
                                    listeners: {
                                        select: function (nodeId, objectId, nodeType, record, index) {
                                            this.formPanel.getForm().findField('organization.objectId').store.load({
                                                params: {organizationId: objectId}
                                            });
                                        },
                                        scope: this
                                    }
                                },
                                {
                                    fieldLabel: '角色',
                                    xtype: 'simplecombo',
                                    name: 'role.objectId',
                                    entity: 'Role',
                                    order: 'orderNo',
                                    emptyText: '请选择角色',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '排序号',
                                    xtype: 'numberfield',
                                    name: 'orderNo',
                                    value: 10000,
                                    allowDecimals: false,
                                    minValue: 1,
                                    maxValue: 99999,
                                    emptyText: '请输入排序号',
                                    allowBlank: false
                                },
                                {
                                    xtype: 'hidden',
                                    name: 'objectId'
                                }
                            ]
                        } ,
                        {
                            xtype: 'fieldset',
                            title: '联系方式',
                            defaultType: 'textfield',
                            defaults: {
                                labelWidth: 90,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    fieldLabel: '手机',
                                    name: 'mobile'
                                },
                                {
                                    fieldLabel: '电子邮箱',
                                    name: 'email',
                                    vtype: 'email',
                                    maxLength: 30
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

