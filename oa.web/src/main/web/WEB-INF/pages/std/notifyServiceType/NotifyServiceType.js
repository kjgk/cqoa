Ext.define('withub.ext.std.notifyServiceType.NotifyServiceType', {
    extend:'withub.ext.common.Window',
    title:'通知服务类型',
    baseUrl:'/std/notifyServiceType',
    initComponent:function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle:'padding: 5px 5px',
            border:false,
            baseCls:'x-plain',
            defaultType:'textfield',
            defaults:{
                labelWidth:100,
                anchor:'100%'
            },
            items:[
                {
                    fieldLabel:'名称',
                    name:'name',
                    allowBlank:false
                },
                {
                    fieldLabel:'标识',
                    name:'notifyServiceTypeTag',
                    allowBlank:false
                },
                {
                    fieldLabel:'用户接收地址',
                    name:'userPropertyAddress'
                },
                {
                    fieldLabel:'自定义通知时间',
                    xtype:'radiogroup',
                    allowBlank:false,
                    columns:6,
                    items:[
                        {boxLabel:'是', name:'customiseNotifyTime', inputValue:1},
                        {boxLabel:'否', name:'customiseNotifyTime', inputValue:0, checked:true}
                    ]
                },
                {
                    xtype:'radiogroup',
                    fieldLabel:'启用通知',
                    allowBlank:false,
                    columns:6,
                    items:[
                        {boxLabel:'是', name:'enable', inputValue:1},
                        {boxLabel:'否', name:'enable', inputValue:0, checked:true}
                    ]
                },
                {
                    fieldLabel:'描述',
                    name:'description',
                    xtype: 'textarea',
                    height: 150
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

