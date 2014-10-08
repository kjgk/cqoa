Ext.define('withub.ext.workflow.task.TaskTransmit', {
    extend: 'Ext.Window',
    width: 300,
    resizable: false,
    modal: true,
    title: '任务转发',
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
                    fieldLabel: '接受人',
                    name: 'handler',
                    allowBlank: false
                }
            ]
        });

        this.buttons = [
            {
                text: '确定',
                handler: function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {
                        return;
                    }
                    Ext.MessageBox.wait('正在转发任务 ...', Global.waitTitle);
                    form.submit({
                        url: Global.contextPath + '/workflow/task/transmit/' + this.taskId,
                        success: function () {
                            Ext.MessageBox.updateProgress(1);
                            Ext.MessageBox.hide();
                            ExtUtil.showSuccessMsgBox('任务转发成功！', function () {
                                this.fireEvent("success");
                                this.close();
                            }, this)
                        },
                        failure: function (from, action) {
                            Ext.MessageBox.updateProgress(1);
                            Ext.MessageBox.hide();
                            ExtUtil.showInfoMsgBox("任务转发失败！");
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

