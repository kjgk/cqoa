Ext.define('withub.ext.system.user.PasswordChange', {
    extend: 'Ext.Window',
    title: '修改密码',
    width: 320,
    resizable: false,
    modal: true,
    initComponent: function () {
        var allowChangeAccount = false;
        ExtUtil.request({
            url: PageContext.contextPath + '/system/account/loadPasswordConfig',
            async: false,
            success: function (result) {
                var data = result.data;
                allowChangeAccount = data.allowChangeAccount;
            }
        });
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '用户名',
                    name: 'account',
                    itemId: 'account',
                    maxLength: 30,
                    allowBlank: false,
                    value: PageContext.currentUser.account,
                    readOnly: !allowChangeAccount,
                    vtype: 'unique',
                    vtypeEntity: 'Account'
                },
                {
                    fieldLabel: '旧密码',
                    id: 'oldPassword',
                    name: 'oldPassword',
                    inputType: 'password',
                    allowBlank: false,
                    blankText: '请输入旧密码!'
                },
                {
                    fieldLabel: '新密码',
                    id: 'newPassword',
                    name: 'newPassword',
                    inputType: 'password',
                    vtype: 'passwordstrength',
                    allowBlank: false
                },
                {
                    fieldLabel: '确认新密码',
                    name: 'confirmPassword',
                    inputType: 'password',
                    allowBlank: false,
                    blankText: '请输入确认密码!',
                    vtype: 'inputConfirm',
                    vtypeText: "两次密码不一致！",
                    confirmTarget: 'newPassword'
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
                        url: PageContext.contextPath + '/security/changePassword',
                        success: function () {
                            ExtUtil.Msg.info('密码修改成功！', function () {
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

