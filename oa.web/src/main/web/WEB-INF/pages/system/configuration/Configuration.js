Ext.define('withub.ext.system.configuration.Configuration', {
    extend: 'Ext.window.Window',
    width: 520,
    height: 460,
    resizable: false,
    modal: true,
    title: '系统参数配置',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            border: false,
            items: [
                {
                    xtype: 'tabpanel',
                    bodyStyle: 'padding: 5px 10px',
                    border: false,
                    items: [
                        {
                            title: '系统',
                            defaultType: 'textfield',
                            layout: 'anchor',
                            border: false,
                            defaults: {
                                labelWidth: 110,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    fieldLabel: 'URL',
                                    name: 'systemConfigInfo.url',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '系统启用年份',
                                    name: 'systemConfigInfo.systemEnabledYear',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '数据库类型',
                                    name: 'systemConfigInfo.databaseType',
                                    value: 'Oracle',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '临时文件路径',
                                    name: 'systemConfigInfo.tempDirectory',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '日志文件路径',
                                    name: 'systemConfigInfo.logFileDirectory',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '日志文件保留天数',
                                    name: 'systemConfigInfo.logFileKeepDays',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: 'IP段',
                                    name: 'systemConfigInfo.ipSegments',
                                    allowBlank: true
                                }
                            ]
                        },
                        {
                            title: '安全选项',
                            defaultType: 'textfield',
                            layout: 'anchor',
                            border: false,
                            defaults: {
                                labelWidth: 110,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    xtype: 'radiogroup',
                                    fieldLabel: '要求登录验证码',
                                    items: [
                                        {boxLabel: '是', name: 'securityConfigInfo.requiredLoginValidateCode', inputValue: 1, checked: true},
                                        {boxLabel: '否', name: 'securityConfigInfo.requiredLoginValidateCode', inputValue: 0}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    xtype: 'radiogroup',
                                    fieldLabel: '允许修改帐号',
                                    items: [
                                        {boxLabel: '是', name: 'securityConfigInfo.allowChangeAccount', inputValue: 1},
                                        {boxLabel: '否', name: 'securityConfigInfo.allowChangeAccount', inputValue: 0, checked: true}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    xtype: 'radiogroup',
                                    fieldLabel: '强制修改密码',
                                    items: [
                                        {boxLabel: '是', name: 'securityConfigInfo.forceChangePassword', inputValue: 1, checked: true},
                                        {boxLabel: '否', name: 'securityConfigInfo.forceChangePassword', inputValue: 0}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '密码强度',
                                    xtype: 'radiogroup',
                                    columns: 2,
                                    items: [
                                        {boxLabel: '弱', name: 'securityConfigInfo.passwordStrength', inputValue: 'Week'},
                                        {boxLabel: '低', name: 'securityConfigInfo.passwordStrength', inputValue: 'Normal', checked: true},
                                        {boxLabel: '中', name: 'securityConfigInfo.passwordStrength', inputValue: 'Good'},
                                        {boxLabel: '强', name: 'securityConfigInfo.passwordStrength', inputValue: 'Strong'}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '密码最少长度',
                                    xtype: 'numberfield',
                                    allowDecimals: false,
                                    minValue: 6,
                                    value: 6,
                                    name: 'securityConfigInfo.passwordLength',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '密码修改周期',
                                    xtype: 'numberfield',
                                    allowDecimals: false,
                                    minValue: 7,
                                    value: 30,
                                    name: 'securityConfigInfo.passwordIntervalDays',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '密码可重复前历史',
                                    xtype: 'numberfield',
                                    allowDecimals: false,
                                    minValue: 0,
                                    value: 2,
                                    name: 'securityConfigInfo.passwordRepeatHistory',
                                    allowBlank: false
                                }
                            ]
                        },
                        {
                            title: '邮件服务器',
                            defaultType: 'textfield',
                            layout: 'anchor',
                            border: false,
                            defaults: {
                                labelWidth: 80,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    fieldLabel: '服务器IP',
                                    name: 'smtpConfigInfo.host',
                                    allowBlank: false
                                },
                                {
                                    xtype: 'numberfield',
                                    fieldLabel: '端口',
                                    name: 'smtpConfigInfo.port',
                                    maxValue: 65535,
                                    allowDecimals: false,
                                    minValue: 1,
                                    value: 25,
                                    allowBlank: false
                                },
                                {
                                    xtype: 'radiogroup',
                                    fieldLabel: '要求验证',
                                    items: [
                                        {boxLabel: '是', name: 'smtpConfigInfo.certificateRequired', inputValue: 1, checked: true},
                                        {boxLabel: '否', name: 'smtpConfigInfo.certificateRequired', inputValue: 0}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '用户名',
                                    name: 'smtpConfigInfo.username',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '密码',
                                    name: 'smtpConfigInfo.password',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '邮箱地址',
                                    name: 'smtpConfigInfo.mailAddress',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '显示名称',
                                    name: 'smtpConfigInfo.displayName',
                                    allowBlank: true
                                },
                                {
                                    fieldLabel: '返回地址',
                                    name: 'smtpConfigInfo.returnAddress',
                                    allowBlank: true
                                }
                            ]
                        },
                        {
                            title: 'SMS',
                            defaultType: 'textfield',
                            layout: 'anchor',
                            border: false,
                            defaults: {
                                labelWidth: 70,
                                anchor: '100%'
                            },
                            items: [
                                {
                                    xtype: 'radiogroup',
                                    fieldLabel: '启用SMS',
                                    columns: 6,
                                    items: [
                                        {boxLabel: '是', name: 'smsConfigInfo.enableSms', inputValue: 1},
                                        {boxLabel: '否', name: 'smsConfigInfo.enableSms', inputValue: 0, checked: true}
                                    ],
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '端口',
                                    name: 'smsConfigInfo.port',
                                    value: 'COM1',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '比特率',
                                    xtype: 'numberfield',
                                    allowDecimals: false,
                                    name: 'smsConfigInfo.baudrate',
                                    value: 9600,
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '制造商',
                                    name: 'smsConfigInfo.manufacturer',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '型号',
                                    name: 'smsConfigInfo.model',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: '协议',
                                    name: 'smsConfigInfo.protocol',
                                    value: 'PDU',
                                    allowBlank: false
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        this.items = [this.formPanel];

        this.buttons = [
            {
                text: '保存',
                handler: function () {
                    var form = this.formPanel.getForm();
                    if (!form.isValid()) {

                        return;
                    }
                    this.doSave();
                },
                scope: this
            },
            {
                text: '取消',
                handler: this.close,
                scope: this
            }
        ];

        this.callParent();


        this.formPanel.getForm().load({
            url: PageContext.contextPath + '/system/configuration/load',
            method: 'GET'
        });
    },

    doSave: function () {

        var form = this.formPanel.getForm();

        Ext.apply(Ext.form.Basic.prototype, {waitMsgTarget: this.getId()});
        form.submit({
            url: PageContext.contextPath + '/system/configuration/save',
            success: function (form, action) {
                ExtUtil.Msg.info('保存成功！', function () {
                    this.fireEvent('success');
                    this.close();
                }, this);
            },
            failure: function (form, action) {
                ExtUtil.Msg.error(action.result.message);
            },
            method: 'POST',
            timeout: 60 * 1000,
            scope: this,
            waitMsg: PageContext.waitMsg,
            waitTitle: PageContext.msgTitle
        });
    }
});

