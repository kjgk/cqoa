Ext.define('withub.ext.workflow.taskOpinionTemplate.TaskOpinionTemplate', {
    extend:'withub.ext.common.Window',
    title:'任务意见模板',
    baseUrl:'/workflow/taskOpinionTemplate',
    initComponent:function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle:'padding: 5px 5px',
            border:false,
            baseCls:'x-plain',
            defaultType:'textfield',
            defaults:{
                labelWidth:50,
                anchor:'100%'
            },
            items:[
                {
                    fieldLabel:'姓名',
                    readOnly:true,
                    allowBlank:false,
                    value:PageContext.currentUser.name
                },
                {
                    fieldLabel: '意见',
                    xtype: 'textarea',
                    height: 150,
                    allowBlank:false,
                    name: 'opinion'
                },
                {
                    xtype:'hidden',
                    allowBlank:false,
                    name: 'user.objectId',
                    value: this.action =='create'? PageContext.currentUser.objectId:undefined
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

