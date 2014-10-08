Ext.define('withub.ext.workflow.instance.InstanceView', {
    extend: 'Ext.Panel',
    baseCls: 'x-plain',
    width: 800,
    title: '流程查看',

    initComponent: function () {

        var gid = Ext.id();
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 5,
            baseCls: 'x-plain',
            items: [
                {
                    xtype: 'fieldset',
                    title: '流程信息',
                    defaults: {
                        xtype: 'displayfield',
                        anchor: '100%'
                    },
                    items: [
                        {
                            itemId: 'instanceName',
                            xtype: 'fieldcontainer',
                            fieldLabel: '流程名称'
                        },
                        {
                            name: 'flowType',
                            fieldLabel: '流程类型'
                        },
                        {
                            name: 'organization',
                            fieldLabel: '发起单位'
                        },
                        {
                            name: 'creator',
                            fieldLabel: '发起人'
                        },
                        {
                            name: 'handler',
                            fieldLabel: '当前处理人'
                        },
                        {
                            name: 'createTime',
                            fieldLabel: '开始时间'
                        },
                        {
                            name: 'finishTime',
                            fieldLabel: '完成时间'
                        },
                        {
                            name: 'status',
                            fieldLabel: '状态'
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: '流程日志',
                    layout: 'fit',
                    items: [
                        Ext.create('withub.ext.workflow.instance.InstanceTaskLogList', {
                            title: null,
                            height: 240,
                            instanceId: this.objectId,
                            taskId: this.taskId,
                            relatedObjectId: this.relatedObjectId
                        })
                    ]
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();

        this.formPanel.getForm().load({
            url: Global.contextPath + '/workflow/instance/view/',
            params: {objectId: this.objectId, relatedObjectId: this.relatedObjectId, taskId: this.taskId},
            success: function (form, action) {
                var result = Ext.decode(action.response.responseText);
                var instanceName = result.data['name'];
                var url = result.data['url'];
                var relatedObjectId = result.data['relatedObjectId'];

                this.formPanel.down('#instanceName').add({
                    id: gid,
                    xtype: 'component',
                    html: '<a href="#" class="default-link">' + instanceName + '</a>'
                });

                this.formPanel.doLayout();

                Ext.get(gid).on('click', function () {
                    Ext.create(url, {
                        objectId: relatedObjectId,
                        viewOnly: true
                    }).show();
                }, this);
            },
            scope: this,
            method: 'GET'
        });
    }
});