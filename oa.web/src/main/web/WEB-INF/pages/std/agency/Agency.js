Ext.define('withub.ext.std.agency.Agency', {
    extend: 'withub.ext.common.Window',
    title: '代理设置',
    baseUrl: '/std/agency',
    width: 420,
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 60,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'usersearchcombo',
                    fieldLabel: '委托人',
                    name: 'owner.objectId',
                    readOnly: true,
                    allowBlank: false,
                    value: PageContext.currentUser['objectId']
                },
                {
                    xtype: 'usersearchcombo',
                    fieldLabel: '代理人',
                    name: 'agent.objectId',
                    allowBlank: false
                },
                {
                    xtype: 'datetimefield',
                    fieldLabel: '开始时间',
                    name: 'startTime',
                    allowBlank: false,
                    value: new Date()
                },
                {
                    xtype: 'datetimefield',
                    fieldLabel: '结束时间',
                    name: 'endTime'
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '原因',
                    name: 'reason',
                    height: 100,
                    allowBlank: false,
                    maxlength: 1000
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

