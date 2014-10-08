Ext.define('withub.ext.system.user.Account', {
    extend: 'Ext.Window',
    width: 320,
    resizable: false,
    modal: true,
    title: '账号设置',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 10,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 80,
                anchor: '100%'
            },
            trackResetOnLoad: true,
            items: [
                {
                    fieldLabel: '用户名',
                    name: 'name',
                    itemId: 'account',
                    maxLength: 30,
                    allowBlank: false,
                    vtype: 'unique',
                    vtypeEntity: 'Account'
                },
                {
                    id: 'password',
                    fieldLabel: '密码',
                    name: 'password',
                    inputType: 'password',
                    vtype: 'passwordstrength',
                    allowBlank: false
                },
                {
                    fieldLabel: '确认密码',
                    name: 'confirmPassword',
                    inputType: 'password',
                    maxLength: 30,
                    allowBlank: false,
                    submitValue: false,
                    vtype: 'inputConfirm',
                    vtypeText: '两次密码不一致！',
                    confirmTarget: 'password'
                },
                {
                    xtype: 'hidden',
                    name: 'user.objectId',
                    value: this.userId
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
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

                    var account = this.formPanel.down("#account").getValue();
                    if (Ext.String.trim(account) == '') {
                        ExtUtil.showInfoMsgBox('用户名不能为空!');
                        this.formPanel.down("#account").focus(false, 500);
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

        if (!Ext.isEmpty(this.objectId)) {
            this.formPanel.getForm().load({
                url: PageContext.contextPath + '/system/account/load/' + this.objectId,
                method: 'GET',
                success: function (form) {
                    var passwordField = form.findField('password');
                    passwordField.submitValue = false;
                    passwordField.on('change', function (field) {
                        field.submitValue = true;
                    }, this);
                    form.clearInvalid();
                },
                scope: this
            });
        }

        this.on('show', function () {
            this.formPanel.getComponent(0).focus(false, 500);
        }, this);
    },

    doSave: function () {
        var form = this.formPanel.getForm();

        Ext.apply(Ext.form.Basic.prototype, {waitMsgTarget: this.getId()});
        form.submit({
            url: PageContext.contextPath + '/system/account/save',
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