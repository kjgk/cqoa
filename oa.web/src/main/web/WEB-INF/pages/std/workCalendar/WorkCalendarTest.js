Ext.define('withub.ext.std.workCalendar.WorkCalendarTest', {
    extend: 'Ext.Window',
    title: '工作日历测试',
    width: 320,
    resizable: false,
    modal: true,
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
                    xtype: 'datetimefield',
                    fieldLabel: '开始时间',
                    name: 'startTime',
                    value: new Date(),
                    allowBlank: false
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '期限(小时)',
                    name: 'hours',
                    allowDecimals: false,
                    minValue: 1,
                    allowBlank: false
                },
                {
                    xtype: 'hidden',
                    name: 'workCalendarId',
                    value: this.workCalendarId
                }
            ]
        });

        this.buttons = [
            {
                text: '获取',
                handler: function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {
                        return;
                    }
                    form.submit({
                        url: PageContext.contextPath + '/std/workCalendar/getEndWorkTime',
                        waitTitle: '工作日历',
                        waitMsg: '正在获取工作期限时间......',
                        success: function (from, action) {
                            var result = Ext.decode(action.response.responseText);
                            ExtUtil.Msg.info(result["result"]);
                        },
                        scope: this
                    })
                },
                scope: this
            },
            {
                text: '取消',
                handler: this.close,
                scope: this
            }
        ];

        this.items = [this.formPanel];

        this.callParent();

    }
});

