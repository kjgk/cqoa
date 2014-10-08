Ext.define('withub.ext.system.metadata.RecursionEntityRootNode', {
    extend: 'Ext.Window',
    title: '根节点',
    width: 480,
    resizable: false,
    modal: true,
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 70,
                anchor: '100%'
            },
            trackResetOnLoad: true,
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '节点标识',
                    name: 'nodeTag',
                    maxLength: 30,
                    value: 'Root',
                    allowBlank: false
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    value: ' ',
                    height: 150,
                    maxLength: 1000
                },
                {
                    xtype: 'hidden',
                    name: 'entityName',
                    value: this.entityName
                }
            ]
        });

        this.buttons = [
            {
                text: '保存',
                handler: function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {
                        return;
                    }
                    form.submit({
                        url: PageContext.contextPath + '/system/metadata/setRecursionEntityRootNode',
                        success: function () {
                            ExtUtil.Msg.info('根节点设置成功！', function () {
                                this.close();
                            }, this)
                        },
                        failure: function (from, action) {
                            var result = Ext.decode(action.response.responseText);
                            ExtUtil.Msg.error(result.message);
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

