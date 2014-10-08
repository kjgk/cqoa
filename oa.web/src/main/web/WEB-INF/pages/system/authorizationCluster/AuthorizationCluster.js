Ext.define('withub.ext.system.authorizationCluster.AuthorizationCluster', {
    extend: 'withub.ext.common.Window',
    title: '授权对象',
    baseUrl: '/system/authorizationCluster',
    userClusterCategoryTag: 'AuthorizationUserCluster',
    bodyPadding: 10,
    height: 480,
    width: 640,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {

        this.height = Math.max(this.height, ExtUtil.getFitHeight());

        this.treePanel = Ext.create('withub.ext.base.Tree', {
            url: '/system/userClusterRegulation/loadCheckTree',
            extraParams: {
                userClusterCategoryTag: this.userClusterCategoryTag
            }
        });

        this.formPanel = Ext.create('Ext.form.Panel', {
            border: false,
            defaultType: 'textfield',
            bodyPadding: 5,
            defaults: {
                labelWidth: 60,
                anchor: '100%'
            },
            items: [
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'container',
                    layout: 'column',
                    defaults: {
                        labelWidth: 60,
                        style: 'margin-right: 10px;'
                    },
                    height: 32,
                    items: [
                        {
                            xtype: 'numberfield',
                            fieldLabel: '优先级',
                            allowBlank: false,
                            name: 'priority',
                            maxValue: 10,
                            minValue: 1,
                            value: 1,
                            columnWidth: .3
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '允许重复',
                            labelWidth: 70,
                            allowBlank: false,
                            items: [
                                {
                                    boxLabel: '是',
                                    name: 'allowRepetition',
                                    inputValue: 1,
                                    checked: true
                                },
                                {
                                    boxLabel: '否',
                                    name: 'allowRepetition',
                                    inputValue: 0
                                }
                            ],
                            columnWidth: .35
                        },
                        {
                            xtype: 'radiogroup',
                            fieldLabel: '启用',
                            allowBlank: false,
                            labelWidth: 40,
                            items: [
                                {
                                    boxLabel: '是',
                                    name: 'enable',
                                    inputValue: 1,
                                    checked: true
                                },
                                {
                                    boxLabel: '否',
                                    name: 'enable',
                                    inputValue: 0
                                }
                            ],
                            columnWidth: .35
                        }
                    ]
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 68,
                    maxLength: 500
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [
            {
                xtype: 'fieldset',
                title: '基本信息',
                height: 160,
                items: this.formPanel,
                layout: 'fit'
            },
            {
                xtype: 'fieldset',
                title: '授权用户簇',
                flex: 1,
                items: this.treePanel,
                layout: 'fit'
            }
        ];

        this.callParent();

        this.on('beforesave', function (form, params) {
            Ext.each(this.treePanel.getChecked(), function (node, index) {
                params['userClusterDetailList[' + index + '].userClusterRegulation.objectId'] = node.get('id').split('_')[1];
                params['userClusterDetailList[' + index + '].relatedObjectId'] = node.get('id').split('_')[2];
            }, this);
        }, this);

        this.on('load', function (form, action) {
            this.treePanel.setChecked(action.result.data['userClusterDetails']);
        }, this)
    }
});

