Ext.define('withub.ext.std.calendarPlan.CalendarPlan', {
    extend: 'withub.ext.common.Window',
    title: '编辑日程',
    baseUrl: '/std/calendarPlan',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            items: [
                {
                    name: 'title',
                    fieldLabel: '主题',
                    xtype: 'textfield',
                    allowBlank: false,
                    anchor: '100%'
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'datetimefield',
                            name: 'startTime',
                            labelWidth: 80,
                            fieldLabel: '从',
                            allowBlank: false,
                            value: this.startTime,
                            flex: 1
                        },
                        {
                            xtype: 'splitter'
                        },
                        {
                            xtype: 'datetimefield',
                            name: 'endTime',
                            labelWidth: 80,
                            allowBlank: false,
                            margin: '0 0 0 30',
                            fieldLabel: '到',
                            flex: 1
                        }
                    ]
                },
                {
                    name: 'importanceLevel',
                    allowBlank: false,
                    anchor: '100%',
                    xtype: 'combo',
                    showAll: true,
                    store: [
                        [1, '高'],
                        [2, '中'],
                        [3, '低']
                    ],
                    value: 3,
                    fieldLabel: '重要性级别'
                },
                {
                    name: 'location',
                    fieldLabel: '地点',
                    xtype: 'textfield',
                    anchor: '100%'
                },
                {
                    name: 'description',
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    height: 150,
                    maxLength: 1000,
                    anchor: '100%'
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
                text: '删除',
                hidden: this.deleteButton,
                handler: function () {
                    var objectId = this.formPanel.getForm().findField('objectId').getValue();
                    Ext.Ajax.request({
                        url: PageContext.contextPath + '/std/calendarPlan/delete/' + objectId,
                        success: function (response) {
                            this.fireEvent('success');
                            this.close();
                        },
                        failure: function (response) {
                            ExtUtil.Msg.error(response.responseText);
                        },
                        scope: this
                    });
                },
                scope: this
            }
        ];

        this.callParent();
    }
});

