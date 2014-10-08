Ext.define('withub.ext.std.customPage.CustomPageDisplay', {
    extend: 'Ext.Viewport',
    layout: 'border',
    zIndex: 1000,
    initComponent: function () {

        var me = this;
        me.defaultChartColors = [
            '058DC7', '50B432', 'ED561B', 'DDDF00', '24CBE5', '64E572', 'FF9655', 'FFF263',
            '514F78', '42A07B', '9B5E4A', '72727F', '1F949A', '82914E', '86777F', '6AF9C4',
            'DDDF0D', '7798BF', '55BF3B', 'DF5353', '008000', '008080', '0000FF', '808080',
            'AAEEEE', 'FF0066', 'FF00FF', 'FFCC00', 'FFFF00', '00FF00', '00FFFF', '00CCFF',
            'C0C0C0', 'FF99CC', 'FFCC99', 'FFFF99', 'CCFFCC', 'CCFFFF', '99CCFF', 'CC99FF'
        ];

        this.mainPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            layout: 'absolute',
            html: '<div id="backgroundImage" style=" position: absolute;top: 0px; left: 0px; z-index: 1"></div>'
        });

        this.items = [
            this.mainPanel
        ];

        this.callParent();

        this.on('afterrender', function () {
            this.loadWidgets();
        }, this);
    },

    createWidget: function (type, config) {
        var me = this;
        if (type == 'withub.ext.std.customPage.widget.ImageScroll1') {
            config.loader = {
                url: PageContext.contextPath + '/loadPage/std/customPage/widget/ImageScroll1.page',
                params: {
                    width: config.width,
                    height: config.height - 30
                },
                scripts: true,
                autoLoad: true
            }
        }
        if (type == 'withub.ext.std.customPage.widget.ImageScroll2') {
            config.loader = {
                url: PageContext.contextPath + '/loadPage/std/customPage/widget/ImageScroll2.page',
                params: {
                    width: config.width,
                    height: config.height
                },
                scripts: true,
                autoLoad: true
            }
        }
        if (type == 'withub.ext.std.customPage.widget.WeatherFeed') {
            config.loader = {
                url: PageContext.contextPath + '/loadPage/std/customPage/widget/WeatherFeed.page',
                params: {
                    id: Ext.id(),
                    width: config.width,
                    height: config.height
                },
                scripts: true,
                autoLoad: true
            }
        }

        var widget = Ext.create(type, Ext.apply({
            style: 'z-index: ' + me.zIndex++
        }, config));

        widget.on('afterrender', function () {
            widget.getEl().on('contextmenu', function (event) {
                event.preventDefault();
                event.stopPropagation();
                if (widget.chart) {
                    Ext.create('Ext.menu.Menu', {
                        items: [
                            {
                                iconCls: 'icon-export',
                                text: '导出PDF',
                                handler: function () {

                                }
                            }
                        ]
                    }).showAt(event.getXY());
                }
            });

            if (!Ext.isEmpty(widget.pageData.dataPoints)) {
                if (type == 'Ext.toolbar.TextItem') {
                    widget.setText(Math.round(Math.random() * 100));
                } else {
                    Ext.each(widget.pageData.dataPoints, function (dataPoint) {
                        var data = [];
                        for (var i = 1; i < 12; i++) {
                            data.push(Math.random() * 100);
                        }
                        var series = Ext.clone(dataPoint);
                        series.color = '#' + series.color;
                        series.data = data;
                        widget.chart.addSeries(series);
                    });
                }
            }
            if (!Ext.isEmpty(widget.pageData['title'])) {
                widget.chart.setTitle({text: widget.pageData['title']});
            }
            if (!Ext.isEmpty(widget.pageData['subTitle'])) {
                widget.chart.setTitle(null, {text: widget.pageData['subTitle']});
            }
            if (!Ext.isEmpty(widget.pageData['text'])) {
                widget.setText(widget.pageData['text']);
            }
            if (!Ext.isEmpty(widget.pageData['fontFamily'])) {
                widget.getEl().setStyle('font-family', widget.pageData['fontFamily'])
            }
            if (!Ext.isEmpty(widget.pageData['fontSize'])) {
                widget.getEl().setStyle('font-size', widget.pageData['fontSize'] + 'px')
            }
            if (!Ext.isEmpty(widget.pageData['color'])) {
                widget.getEl().setStyle('color', widget.pageData['color'])
            }
            if (!Ext.isEmpty(widget.pageData['fileName'])) {
                widget.setSrc(PageContext.contextPath + '/std/customPage/backgroundImage/load?fileName=' + widget.pageData['fileName']);
            }
        });

        return widget;
    },

    loadWidgets: function () {
        var me = this;
        Ext.Ajax.request({
            url: PageContext.contextPath + '/std/customPage/load/' + this.customPageId,
            method: 'GET',
            success: function (response) {
                var result = Ext.decode(response.responseText);
                Ext.each(result['items'], function (item) {
                    var widget;
                    var config = {
                        name: item['name'],
                        width: item['width'],
                        height: item['height'],
                        x: item['left'] - 1,
                        y: item['top'] - 1,
                        pageData: Ext.decode(item['pageData'])
                    };
                    if (item.page == 'Ext.toolbar.TextItem') {
                        Ext.apply(config, {
                            text: item['name']
                        })
                    } else if (item.page == 'withub.ext.std.customPage.widget.Clock1') {
                        Ext.apply(config, {
                            enableClock: true,
                            liveUpdate: config.pageData.refreshInterval > 0
                        })
                    }
                    widget = this.createWidget(item.page, config);
                    this.mainPanel.add(widget);
                }, this);

                if (result.data['fileInfoId']) {
                    me.backgroundImage = Ext.create('Ext.Img', {
                        autoEl: 'div',
                        renderTo: 'backgroundImage',
                        src: PageContext.contextPath + '/std/file/picture?fileInfoId=' + result.data['fileInfoId']
                    });
                }
            },
            scope: this
        });
    }
});

Ext.define('Ext.ux.TextItem', {
    extend: 'Ext.toolbar.TextItem'
});

window.data1 = [
    {
        dataPointId: 'P-1',
        name: '机组-1',
        data: [49, 71, 106, 129, 144, 176, 135, 148, 216, 174, 95, 54]
    },
    {
        dataPointId: 'P-2',
        name: '机组-2',
        data: [83, 78, 98, 93, 106, 84, 105, 104, 91, 83, 106, 92]
    },
    {
        dataPointId: 'P-3',
        name: '机组-3',
        data: [48, 38, 39, 41, 47, 48, 59, 59, 52, 65, 59, 51]
    },
    {
        dataPointId: 'P-4',
        name: '机组-4',
        data: [42, 33, 34, 39, 52, 75, 57, 60, 47, 39, 46, 51]
    }
];