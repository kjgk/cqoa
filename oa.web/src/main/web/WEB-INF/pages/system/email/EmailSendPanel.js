Ext.define('withub.ext.system.email.EmailSendPanel', {
    extend:'Ext.window.Window',
    width:520,
    resizable:false,
    modal:true,
    title:'发送邮件',
    initComponent:function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding:10,
            border:false,
            baseCls:'x-plain',
            defaultType:'textfield',
            defaults:{
                labelWidth:80,
                anchor:'100%'
            },
            items:[
                {
                    fieldLabel:'收件人地址',
                    name:'address',
                    vtype:"email",
                    allowBlank:false
                },
                {
                    fieldLabel:'主题',
                    name:'title',
                    maxLength:100,
                    allowBlank:false
                },
                {
                    fieldLabel:'内容',
                    xtype:'textarea',
                    height:240,
                    name:'content',
                    allowBlank:false
                },
            ]
        });

        this.items = [this.formPanel];


        this.buttons = [
            {
                text:'发送',
                handler:function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {

                        return;
                    }
                    this.doSave();
                },
                scope:this
            },
            {
                text:'关闭',
                handler:this.close,
                scope:this
            }
        ];

        this.callParent();
    },

    doSave:function () {

        var form = this.formPanel.getForm();

        Ext.apply(Ext.form.Basic.prototype, {waitMsgTarget:this.getId()});
        form.submit({
            url:PageContext.contextPath + '/system/email/send',
            success:function (form, action) {
                ExtUtil.Msg.info('发送成功！', function () {
                    this.fireEvent('success');
                    this.close();
                }, this);
            },
            failure:function (form, action) {
                ExtUtil.Msg.error(action.result.message);
            },
            method:'POST',
            timeout:60 * 1000,
            scope:this,
            waitMsg:PageContext.waitMsg,
            waitTitle:PageContext.msgTitle
        });
    }
});

