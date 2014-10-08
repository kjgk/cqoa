Ext.define('withub.ext.system.userCluster.UserCluster', {
    extend: 'withub.ext.common.Window',
    title: '用户簇',
    baseUrl: '/system/userCluster',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 100,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '用户簇分类',
                    url: '/system/userCluster/loadTree',
                    params: {depth: 1},
                    name: 'userClusterCategory.objectId',
                    value: this.userClusterCategoryId ? 'UserClusterCategory_' + this.userClusterCategoryId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'userClusterTag',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '实体实例簇',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {
                            boxLabel: '是',
                            name: 'entityInstanceCluster',
                            inputValue: 1
                        },
                        {
                            boxLabel: '否',
                            name: 'entityInstanceCluster',
                            inputValue: 0,
                            checked: true
                        }
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '运行时',
                    allowBlank: false,
                    columns: 6,
                    items: [
                        {
                            boxLabel: '是',
                            name: 'runtime',
                            inputValue: 1
                        },
                        {
                            boxLabel: '否',
                            name: 'runtime',
                            inputValue: 0,
                            checked: true
                        }
                    ]
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();
    }
});

