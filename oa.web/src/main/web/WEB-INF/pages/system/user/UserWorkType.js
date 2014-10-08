Ext.define('withub.ext.system.user.UserWorkType', {
    extend: 'Ext.Window',
    width: 480,
    resizable: false,
    modal: true,
    title: '用户工种',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '工种',
                    itemId: 'workType',
                    name: 'workType',
                    url: '/std/workType/loadTree',
                    params: {depth: 2},
                    selectType: 'WorkType',
                    emptyText: '请选择工种',
                    allowBlank: false
                },
                {
                    fieldLabel: '证书编号',
                    itemId: 'certificateNo',
                    name: 'certificateNo',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
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
                    this.fireEvent("input", this.down("#workType").getObjectValue(), this.down("#workType").getRawValue(), this.down("#certificateNo").getValue());
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
    },

    setData: function (record) {
        this.down('#workType').setValue("WorkType_" + record.get('workTypeId'));
        this.down('#certificateNo').setValue(record.get('certificateNo'));
    }
});