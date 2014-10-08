Ext.define('withub.ext.std.file.FileConfig', {
    extend: 'withub.ext.common.Window',
    title: '文件配置',
    baseUrl: '/std/fileConfig',
    initComponent: function () {
        this.formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding: 5px 10px',
            border: false,
            baseCls: 'x-plain',
            defaultType: 'textfield',
            defaults: {
                labelWidth: 75,
                anchor: '100%'
            },
            items: [
                {
                    xtype: 'treecombo',
                    fieldLabel: '实体',
                    url: '/system/metadata/loadTree',
                    params: {depth: 2},
                    selectType: 'Entity',
                    name: 'entity.objectId',
                    allowBlank: false
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: '存储类型',
                    allowBlank: false,
                    items: [
                        {boxLabel: '文件系统', name: 'storageType', inputValue: 1, checked: true},
                        {boxLabel: '数据库', name: 'storageType', inputValue: 2}
                    ]
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: '附件数量',
                    name: 'count',
                    allowDecimals: false,
                    value: 2,
                    minValue: 0,
                    allowBlank: false
                },
                {
                    xtype: 'simplecombo',
                    fieldLabel: '服务器',
                    name: 'server.objectId',
                    entity: 'com.withub.model.std.po.Server',
                    allowBlank: false
                },
                {
                    fieldLabel: '路径',
                    name: 'serverPath',
                    maxLength: 100,
                    allowBlank: false
                },
                {
                    xtype: 'checkbox',
                    name: 'traceDownload',
                    boxLabel: '记录下载',
                    inputValue: 1
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

