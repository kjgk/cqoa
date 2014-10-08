Ext.define('withub.ext.std.systemEvent.EventNotifyServiceType', {
    extend:'withub.ext.common.Window',
    title:'事件通知模板',
    width:640,
    baseUrl:'/std/eventNotifyServiceType',
    initComponent:function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle:'padding: 5px 10px',
            border:false,
            baseCls:'x-plain',
            defaultType:'textfield',
            defaults:{
                labelWidth:75,
                anchor:'100%'
            },
            items:[
                {
                    xtype:'hidden',
                    fieldLabel:'事件',
                    name:'systemEvent.objectId',
                    value:this.systemEventId
                },
                {
                    fieldLabel:'标题模板',
                    name:'titleTemplate',
                    maxLength:100,
                    allowBlank:false
                },
                {
                    fieldLabel:'内容模板',
                    xtype:'textarea',
                    name:'contentTemplate',
                    height:200,
                    allowBlank:false
                },
                {
                    fieldLabel:'默认模板',
                    xtype:'radiogroup',
                    allowBlank:false,
                    items:[
                        {boxLabel:'是', name:'defaultTemplate', inputValue:1},
                        {boxLabel:'否', name:'defaultTemplate', inputValue:0, checked:true}
                    ]
                },
                {
                    xtype:'hidden',
                    fieldLabel:'服务类型',
                    name:'notifyServiceType.objectId',
                    value:this.notifyServiceTypeId
                },
                {
                    xtype:'hidden',
                    name:'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

