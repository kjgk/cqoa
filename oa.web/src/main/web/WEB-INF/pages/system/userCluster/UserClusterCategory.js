Ext.define('withub.ext.system.userCluster.UserClusterCategory', {
    extend: 'withub.ext.common.Window',
    title: '用户簇分类',
    baseUrl: '/system/userClusterCategory',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 5px',
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
                    xtype: 'treecombo',
                    fieldLabel: '上级分类',
                    url: '/system/userCluster/loadTree',
                    params: {depth: 1},
                    name: 'parent.objectId',
                    value: this.action == 'create' ? 'UserClusterCategory_' + this.parentId : undefined,
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
                    name: 'nodeTag',
                    vtype: 'unique',
                    vtypeEntity: 'UserClusterCategory',
                    vtypeProperty: 'nodeTag',
                    allowBlank: false,
                    maxLength: 100
                },
                {
                    fieldLabel: '缓存实体名',
                    name: 'cacheEntityName',
                    allowBlank: true,
                    maxLength: 100
                },
                {
                    xtype: 'textarea',
                    fieldLabel: '描述',
                    name: 'description',
                    height: 150,
                    maxLength: 1000
                },
                {
                    xtype: 'hidden',
                    name: 'objectId'
                }
            ]
        });

        this.items = [this.formPanel];

        this.callParent();

        this.on('load', function (form, action) {
            if (Ext.isEmpty(action.result.data['parent.objectId'])) {
                form.findField('parent.objectId').destroy();
            }
        });
    }
});


