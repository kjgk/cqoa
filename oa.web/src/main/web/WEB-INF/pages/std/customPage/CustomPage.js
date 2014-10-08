Ext.define('withub.ext.std.customPage.CustomPage', {
    extend: 'withub.ext.common.Window',
    title: '自定义展示图',
    baseUrl: '/std/customPage',
    initComponent: function () {

        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyPadding: 5,
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
                    fieldLabel: '上级',
                    url: '/std/customPage/loadTree',
                    name: 'customPageCategory.objectId',
                    params: {
                        depth: 1
                    },
                    value: this.customPageCategoryId ? 'CustomPageCategory_' + this.customPageCategoryId : undefined,
                    allowBlank: false
                },
                {
                    fieldLabel: '名称',
                    name: 'name',
                    maxLength: 30,
                    allowBlank: false
                },
                {
                    fieldLabel: '标识',
                    name: 'code',
                    maxLength: 100
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