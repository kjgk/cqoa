Ext.define('withub.ext.std.bulletin.Bulletin', {
    extend: 'withub.ext.common.Window',
    title: '公告通知',
    width: 840,
    baseUrl: '/std/bulletin',
    enableButton2: true,
    button2Text: '发布',
    button2Action: 'issue',

    initComponent: function () {

        this.attachList = Ext.create('withub.ext.common.SwfUploadList', {
            style: 'margin: 0px 5px 5px 5px;',
            height: 110
        });

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 5,
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '标题',
                    name: 'title',
                    maxLength: 100,
                    emptyText: '请输入标题',
                    allowBlank: false
                },
                {
                    xtype: 'treecombo',
                    fieldLabel: '通知范围',
                    name: 'organization.objectId',
                    url: '/system/organization/loadTree',
                    value: this.action == 'create' ? PageContext.currentUser['organizationId'] : undefined,
                    allowBlank: false
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'codecombo',
                            fieldLabel: '类型',
                            labelWidth: 75,
                            name: 'bulletinType.objectId',
                            codeColumnTag: 'BulletinType',
                            defaultCodeTag: this.bulletinType,
                            allowBlank: false,
                            flex: 3
                        },
                        {xtype: 'splitter'},
                        {
                            xtype: 'codecombo',
                            fieldLabel: '级别',
                            labelWidth: 50,
                            name: 'bulletinLevel.objectId',
                            codeColumnTag: 'BulletinLevel',
                            allowBlank: false,
                            flex: 2
                        },
                        {xtype: 'splitter'},
                        {
                            itemId: 'effectiveTime',
                            xtype: 'datetimefield',
                            labelWidth: 50,
                            fieldLabel: '有效期',
                            name: 'effectiveTime',
                            allowBlank: false,
                            flex: 5
                        }
                    ]
                },
                {
                    xtype: 'htmleditor',
//                    enableFont:false,
                    enableFontSize: false,
                    enableSourceEdit: false,
                    name: 'content',
                    emptyText: '请输入内容',
                    allowBlank: false,
                    height: 260,
                    flex: 1
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel, this.attachList];

        this.callParent();

        this.on('beforesave', function (form, params) {
            if (this.formPanel.down('#effectiveTime').getValue() <= new Date().getTime()) {
                ExtUtil.Msg.info('有效期不能小于当前时间！');
                return false;
            }
        }, this);
    }
});

