Ext.define('withub.ext.system.user.UserSingleSelect', {
    extend: 'Ext.Window',
    width: 380,
    resizable: false,
    modal: true,
    title: '员工选择',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
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
                    fieldLabel: '姓名',
                    itemId: 'user',
                    allowBlank: false
                }
            ]
        });

        this.items = [this.formPanel];

        this.buttons = [
            {
                text: '确定',
                handler: function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {
                        return;
                    }
                    this.fireEvent("select", this.down("#user").getValue(), this.down("#user").getRawValue());
                    this.close();
                },
                scope: this
            },
            {
                text: '取消',
                handler: this.close,
                scope: this
            }
        ];

        this.callParent();
    }
});