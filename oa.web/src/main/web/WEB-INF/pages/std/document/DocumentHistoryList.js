Ext.define('withub.ext.std.document.DocumentHistoryList', {
    extend: 'withub.ext.common.ManagerGrid',
    height: 480,
    width: 800,
    title: '文档历史列表',
    model: 'withub.ext.std.document.DocumentHistoryModel',
    url: '/std/documentHistory/list',
    autoQuery: true,
    enablePagginBar: false,
    initComponent: function () {

        this.extraParams = {
            documentId: this.documentId
        };
        this.columns = [
            Ext.create('Ext.grid.RowNumberer'),
            {width: 30, renderer: function (value, metaData, record) {
                var html = '<a href="{0}" target="_blank" class="file-download" style="margin-left: 3px;"><img style="height: 14px;" src="{1}"/></a>';
                return Ext.String.format(html, Global.contextPath + '/std/file/download?fileInfoId=' + record.get('fileInfoId')
                    , Global.contextPath + '/images/icon/attach.png');
            }},
            {text: '文档名称', flex: 1, dataIndex: 'name'},
            {text: '版本', width: 50, dataIndex: 'version'},
            {text: '大小', width: 80, dataIndex: 'documentSize', sortable: false, renderer: SWFUpload.speed.formatBytes},
            {text: '上传者', width: 100, dataIndex: 'creator', sortable: false},
            {text: '编写日期', width: 100, dataIndex: 'writeDate', sortable: false, renderer: ExtUtil.dateRenderer(DateFormat.DAY)}
        ];

        this.callParent();
    }
});

Ext.define('withub.ext.std.document.DocumentHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
        'objectId',
        'name',
        'documentSize',
        'version',
        'creator',
        'fileInfoId',
        {name: 'writeDate', type: 'int'}
    ]
});