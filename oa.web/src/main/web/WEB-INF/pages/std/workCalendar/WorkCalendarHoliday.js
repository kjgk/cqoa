Ext.define('withub.ext.std.workCalendar.WorkCalendarHoliday', {
    extend: 'withub.ext.common.Window',
    title: '节假日',
    baseUrl: '/std/workCalendarHoliday',
    initComponent: function() {
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
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    fieldLabel: '日期',
                    name: 'day',
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '节假日',
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'holiday', inputValue: 1,checked: true},
                        {boxLabel: '否', name: 'holiday', inputValue: 0}
                    ]
                },
                {
                    xtype: 'hidden',
                    name: 'workCalendar.objectId',
                    value:this.workCalendarId
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

