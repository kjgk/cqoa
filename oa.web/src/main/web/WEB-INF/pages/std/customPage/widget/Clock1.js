Ext.define('withub.ext.std.customPage.widget.Clock1', {
    extend: 'Ext.toolbar.TextItem',
    width: 160,
    height: 40,
    layout: 'fit',
    enableClock: false,
    initComponent: function () {

        var me = this;

        this.callParent();

        this.on('afterrender', function () {

            if (me.enableClock === false) {
                return;
            }

            var updateClock = function () {
                Ext.get(me.getId()).update(Ext.Date.format(new Date(), me.pageData.format || ''));
            }
            if (this.liveUpdate) {
                var task = Ext.TaskManager.start({
                    run: updateClock,
                    interval: me.pageData.refreshInterval * 1000
                });
            } else {
                updateClock();
            }
        })
    }


});