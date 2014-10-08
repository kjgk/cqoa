Ext.define('withub.ext.std.logFile.LogFileList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.std.logFile.LogFileModel'],
    initComponent: function () {

        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.std.logFile.LogFileModel',
            baseUrl: '/std/logFile',
            enablePagginBar: true,
            border: false,
            columns: [
                Ext.create('Ext.grid.RowNumberer'),
                {text: '日志名称', width: 200, dataIndex: 'fileName'}  ,
                {text: '日志文件大小(KB)', width: 200, dataIndex: 'logFileSize', align: 'right' }   ,
                {text: '日志创建时间', width: 200, dataIndex: 'logFileCreateTime', align: 'right', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.SECOND)}

            ]


        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var fileName = record.get('fileName');

            items.push(
                {
                    text: '下载',
                    iconCls: 'icon-edit',
                    handler: function () {
                        var windId = Ext.id();
                        var url = PageContext.contextPath + '/std/logFile/Download?fileName=' + fileName;
                        var template = new Ext.XTemplate('<div style="padding: 20px 10px; font-size: 15px; text-align: center;">' +
                            '<a href="{url}" onclick="Ext.getCmp(\'' + windId + '\').close();" >{fileName}</a>' +
                            '</div>');
                        var wind = new Ext.Window({
                            id: windId,
                            title: '日志下载',
                            height: 95,
                            width: 320,
                            plain: true,
                            resizable: false,
                            modal: true
                        });
                        wind.on('afterrender', function () {
                            template.overwrite(wind.body, {
                                url: url,
                                fileName: fileName
                            });
                        }, this);
                        wind.show();

                    },
                    scope: this
                }
            );

        }, this);
        this.items = this.gridPanel;

        this.callParent();

        this.query();
    },

    query: function () {

        this.gridPanel.getStore().loadPage(1);
    }


});