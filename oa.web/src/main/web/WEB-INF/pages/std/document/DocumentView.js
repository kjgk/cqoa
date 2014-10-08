Ext.define('withub.ext.std.document.DocumentView', {
    extend: 'Ext.Panel',
    width: 842,
    height: 480,
    title: '文档查看',

    initComponent: function () {

        this.html = ExtUtil.buildIframe(Global.contextPath + '/std/document/view/' + this.objectId + '.page', this.width - 2, this.height);
        this.callParent();
    }
});