Ext.define('withub.ext.std.systemEvent.EventNotifyServiceTypeTree', {
    closable: true,
    extend: 'Ext.Window',
    title: '通知服务',
    width: 800,
    height: 500,
    layout: 'border',
    requires: ['withub.ext.std.systemEvent.EventNotifyServiceTypeModel'],
    initComponent: function () {

        this.treePanel = Ext.create('withub.ext.common.ManagerTree', {
            region: 'west',
            title: '通知服务类型',
            split: true,
            width: 160,
            singleExpand: true,
            extraParams: {'systemEventId': this.systemEventId},
            baseUrl: '/std/eventNotifyServiceType'
        });


        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            title: '通知服务模板',
            region: 'center',
            margins: '5 5 5 0',
            url: PageContext.contextPath + '/std/eventNotifyServiceType/update',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'hidden',
                    fieldLabel: '事件',
                    name: 'systemEvent.objectId',
                    value: this.systemEventId
                },
                {
                    fieldLabel: '标题模板',
                    name: 'titleTemplate',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '内容模板',
                    xtype: 'textarea',
                    name: 'contentTemplate',
                    height: 240,
                    allowBlank: false
                },
                {
                    fieldLabel: '默认模板',
                    xtype: 'radiogroup',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'defaultTemplate', inputValue: 1},
                        {boxLabel: '否', name: 'defaultTemplate', inputValue: 0, checked: true}
                    ]
                },
                {
                    fieldLabel: '是否启用',
                    xtype: 'radiogroup',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {boxLabel: '是', name: 'enable', inputValue: 1},
                        {boxLabel: '否', name: 'enable', inputValue: 0, checked: true}
                    ]
                },
                {
                    xtype: 'hidden',
                    fieldLabel: '服务类型',
                    name: 'notifyServiceType.objectId'
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ],
            buttons: [
                {
                    text: '保存',
                    formBind: true,
                    disabled: true,
                    handler: function () {
                        var form = this.up('form').getForm();
                        if (form.isValid()) {
                            form.submit({
                                success: function (form, action) {
                                    ExtUtil.Msg.info('保存成功！', this);
                                },
                                failure: function (form, action) {
                                    ExtUtil.Msg.error(action.result.message);
                                }
                            });
                        }

                    }
                },
                {
                    text: '关闭',
                    scope: this,
                    handler: function () {
                        this.close();
                    }
                }
            ]
        });

        this.items = [this.treePanel, this.formPanel];

        this.treePanel.on('beforeselect', function (tree, record, index) {

            if (record.get('type') == 'SystemEvent') {
                // record.data.text=this.systemEventName;
                tree.view.select(record.firstChild, false, false);
                return false;
            } else {
                this.formPanel.getForm().reset();
                var notifyServiceTypeId = record.get('objectId');
                this.formPanel.getForm().findField("notifyServiceType.objectId").setValue(notifyServiceTypeId);
                this.doLoad(notifyServiceTypeId, this.systemEventId);
            }

        }, this);

        this.callParent();

    },

    doLoad: function (notifyServiceTypeId, systemEventId) {
        var mask = new Ext.LoadMask(this.getId(), {msg: PageContext.loadMsg});
        var form = this.formPanel.getForm();

        mask.show();
        form.load({
            url: PageContext.contextPath + '/std/eventNotifyServiceType/load/' + notifyServiceTypeId + '/' + systemEventId,
            method: 'GET',
            timeout: 60 * 1000,
            success: function (form, action) {
                mask.hide();
                this.fireEvent('load', form, action);
            },
            failure: function (form, action) {
                mask.hide();
            },
            scope: this
        });
    }

});


