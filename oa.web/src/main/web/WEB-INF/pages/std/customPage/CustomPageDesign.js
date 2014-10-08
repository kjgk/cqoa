Ext.define('withub.ext.std.customPage.CustomPageDesign', {
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

        var widgetItemConfig = [
            ['单属性', [
                {text: '曲线图-多曲线', leaf: true, iconCls: 'icon-chart-line', attributes: {page: 'withub.ext.std.customPage.widget.LineChart'} },
                {text: '曲线图-多轴', leaf: true, iconCls: 'icon-chart-line', attributes: {page: 'withub.ext.std.customPage.widget.DualAxesChart'} },
                {text: '曲线图-堆积', leaf: true, iconCls: 'icon-chart-line', attributes: {page: 'withub.ext.std.customPage.widget.AreaChart'} },
                {text: '柱形图-圆柱', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '柱形图-方形', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '柱形图-三维', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '柱形图-平面', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '柱形图-堆积', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '柱形图-簇状', leaf: true, iconCls: 'icon-chart-column', attributes: {page: 'withub.ext.std.customPage.widget.ColumnChart'} },
                {text: '饼图-平面', leaf: true, iconCls: 'icon-chart-pie', attributes: {page: 'withub.ext.std.customPage.widget.PieChart'} },
                {text: '饼图-三维', leaf: true, iconCls: 'icon-chart-pie', attributes: {page: 'withub.ext.std.customPage.widget.Pie3dChart'} },
                {text: '饼图-分离型', leaf: true, iconCls: 'icon-chart-pie', attributes: {page: 'withub.ext.std.customPage.widget.DonutPieChart'} },
                {text: '条形图-平面', leaf: true, iconCls: 'icon-chart-bar', attributes: {page: 'withub.ext.std.customPage.widget.BarChart'} },
                {text: '条形图-堆积', leaf: true, iconCls: 'icon-chart-bar', attributes: {page: 'withub.ext.std.customPage.widget.StackedBarChart'} },
                {text: '面积图', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.AreaChart2'} },
                {text: '游标指示', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.GaugeChart'} },
                {text: '指针指示', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.GaugeChart'} },
                {text: '容量指示', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.FunnelChart'} }
            ]],
            ['多属性', [
                {text: '走势图与柱状图', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.DualAxesChart2'} },
                {text: '双层面积对比', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.AreaChart3'} }
            ]] ,
            ['复合', [
                {text: '切换图形', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.CombinationChart'} },
                {text: '切换关联图形', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.CombinationChart'} }
            ]] ,
            ['文字', [
                {text: '静态文本', leaf: true, iconCls: 'icon-item', attributes: {page: 'Ext.ux.TextItem'}},
                {text: '动态文本', leaf: true, iconCls: 'icon-item', attributes: {page: 'Ext.toolbar.TextItem'}}
            ]] ,
            ['图片', [
                {text: '图片', leaf: true, iconCls: 'icon-item', attributes: {page: 'Ext.Img'}}
            ]] ,
            ['表格', []] ,
            ['模版', []] ,
            ['报告', []] ,
            ['特殊', [
                {text: '图片特效-1', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.ImageScroll1'}},
                {text: '图片特效-2', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.ImageScroll2'}},
                {text: '天气', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.WeatherFeed'}},
                {text: '日期/时间', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.Clock1'}},
                {text: '时钟', leaf: true, iconCls: 'icon-item', attributes: {page: 'withub.ext.std.customPage.widget.Clock2'} }
            ]]
        ];

        var widgetItems = [];
        Ext.each(widgetItemConfig, function (config) {
            widgetItems.push(
                Ext.create('Ext.tree.Panel', {
                    title: config[0],
                    border: false,
                    lines: false,
                    store: Ext.create('Ext.data.TreeStore', {
                        fields: ['id', 'text', 'attributes'],
                        root: {
                            expanded: true,
                            children: config[1]
                        }
                    }),
                    rootVisible: false,
                    viewConfig: {
                        plugins: {
                            ptype: 'treeviewdragdrop',
                            enableDrag: true,
                            enableDrop: false,
                            ddGroup: 'ddd'
                        }
                    }
                })
            )
        });

        this.widgetPanel = Ext.create('Ext.tab.Panel', {
            tabPosition: 'right',
            region: 'east',
            width: 170,
            layout: 'fit',
            split: true,
            items: widgetItems
        });

        this.dataPointPanel = Ext.create('Ext.grid.Panel', {
            border: false,
            hidden: true,
            store: Ext.create('Ext.data.Store', {
                fields: ['dataPointId', 'name', 'color'],
                proxy: {
                    type: 'memory',
                    reader: {
                        type: 'json',
                        root: 'items'
                    }
                }
            }),
            tbar: [
                {
                    xtype: 'button',
                    text: '添加数据点',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.std.customPage.customPageDesign.DataPoint', {
                            listeners: {
                                select: function (records) {
                                    var widget = me.getCurrentWidget();
                                    widget.pageData.dataPoints = widget.pageData.dataPoints || [];
                                    if (widget.self.getName() == 'Ext.toolbar.TextItem') {
                                        widget.pageData.dataPoints = [records[0].data];
                                        widget.setText(Math.round(Math.random() * 100));
                                        me.dataPointPanel.getStore().loadData(widget.pageData.dataPoints, false);
                                    } else {
                                        var colors = Ext.Array.clone(me.defaultChartColors);
                                        Ext.each(widget.pageData.dataPoints, function (dataPoint) {
                                            Ext.Array.remove(colors, dataPoint.color);
                                        });
                                        Ext.each(records, function (record, index) {
                                            widget.pageData.dataPoints.push(Ext.apply({color: colors[index]}, record.data));
                                            var series = {color: '#' + colors[index]};
                                            var data = [];
                                            for (var i = 1; i < 12; i++) {
                                                data.push(Math.random() * 100);
                                            }
                                            Ext.apply(series, {data: data}, record.data);
                                            widget.chart.addSeries(series);
                                        });
                                        me.dataPointPanel.getStore().loadData(widget.pageData.dataPoints);
                                    }
                                }
                            }
                        }).show();
                    }
                }
            ],
            selModel: {
                selType: 'cellmodel'
            },
            columns: [
                {text: '数据点', dataIndex: 'name', flex: 1 }
            ],
            listeners: {
                itemcontextmenu: function (grid, record, item, index, e, eOpts) {
                    e.preventDefault();
                    Ext.create('Ext.menu.Menu', {
                        items: [
                            {
                                iconCls: 'icon-color',
                                text: '设置颜色',
                                hidden: me.getCurrentWidget().self.getName() == 'Ext.toolbar.TextItem',
                                menu: Ext.create('Ext.menu.ColorPicker', {
                                    value: record.get('color'),
                                    colors: me.defaultChartColors,
                                    listeners: {
                                        select: function (colorPicker, color) {
                                            var widget = me.getCurrentWidget();
                                            widget.chart.series[index].update({
                                                color: '#' + color
                                            });
                                            widget.pageData.dataPoints[index].color = color;
                                            record.set('color', color);
                                        }
                                    }
                                })
                            },
                            {
                                iconCls: 'icon-delete',
                                text: '删除数据点',
                                handler: function () {
                                    grid.getStore().removeAt(index);
                                    var widget = me.getCurrentWidget();
                                    if (widget.dataPoint == true) {
                                        widget.chart.series[index].remove();
                                    }
                                    Ext.Array.replace(widget.pageData.dataPoints, index, 1);
                                }
                            }
                        ]
                    }).showAt(e.getXY());

                }
            }
        });


        this.propertyPanel = Ext.create('Ext.grid.property.Grid', {
            title: '属性',
            nameColumnWidth: 120,
            sortableColumns: false,
            region: 'center',
            margins: '0 5 0 0',
            source: {},
            hideHeaders: true,
            listeners: {
                propertychange: function (source, field, value, oldValue) {
                    var widget = me.getCurrentWidget();
                    if (widget) {
                        if (field == 'width' && widget.getWidth() != value) {
                            widget.setWidth(value);
                        }
                        if (field == 'height' && widget.getHeight() != value) {
                            widget.setHeight(value);
                        }
                        if (field == 'position' && widget.getXY().join(',') != value) {
                            widget.setPosition(new Number(value.split(',')[0]), new Number(value.split(',')[1]));
                        }
                        if (field == 'title') {
                            widget.chart.setTitle({text: value});
                            widget.pageData.title = value;
                        }
                        if (field == 'subTitle') {
                            widget.chart.setTitle(null, {text: value});
                            widget.pageData.subTitle = value;
                        }

                        if (field == 'text') {
                            widget.setText(value);
                            widget.pageData.text = value;
                        }
                        if (field == 'fontFamily') {
                            widget.getEl().setStyle('font-family', value);
                            widget.pageData.fontFamily = value;
                        }
                        if (field == 'fontSize') {
                            widget.getEl().setStyle('font-size', value + 'px');
                            widget.pageData.fontSize = value;
                        }
                        if (field == 'color') {
                            widget.getEl().setStyle('color', value);
                            widget.pageData.color = value;
                        }
                        if (field == 'format') {
                            widget.pageData.format = value;
                        }
                        if (field == 'refreshInterval') {
                            widget.pageData.refreshInterval = value;
                        }
                    }
                }
            }
        });

        this.saveButton = Ext.create('Ext.Button', {
            text: '保存',
            handler: function () {
                this.saveCustomPage(function () {
                    ExtUtil.Msg.info('保存成功！');
                });
            },
            scope: this
        });

        this.previewButton = Ext.create('Ext.Button', {
            text: '预览',
            handler: function () {
                this.saveCustomPage(function () {
                    window.open(PageContext.contextPath + '/loadPage/withub.ext.std.customPage.CustomPageDisplay?customPageId=' + me.customPageId
                        , 'CustomPageDisplay', 'height=' + me.mainPanel.getHeight() + ',width=' + me.mainPanel.getWidth() + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
                });
            },
            scope: this
        });

        this.mainPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            layout: 'absolute',
            html: '<div id="backgroundImage" style=" position: absolute;top: 0px; left: 0px; z-index: 1"></div>',
            listeners: {
                render: function (panel) {
                    var body = panel.body;
                    var dropTarget = new Ext.dd.DropTarget(body, {
                        ddGroup: 'ddd',
                        notifyEnter: function (ddSource, e, data) {
                            body.stopAnimation();
                        },
                        notifyDrop: function (ddSource, e, data) {
                            var record = ddSource.dragData.records[0];
                            var page = record.get('attributes')['page'];
                            var widget;
                            var config = {
                                text: record.get('text'),
                                name: record.get('text'),
                                x: e.getXY()[0],
                                y: e.getXY()[1],
                                cls: ['block', 'block-select'],
                                resizable: true,
                                draggable: true,
                                pageData: {}
                            }
                            if (page == 'Ext.toolbar.TextItem' || page == 'Ext.ux.TextItem') {
                                Ext.apply(config, {
                                    width: 80,
                                    height: 32,
                                    cls: ['block', 'block-select', 'text-block']
                                });
                            } else if (page == 'withub.ext.std.customPage.widget.ImageScroll1'
                                || page == 'withub.ext.std.customPage.widget.ImageScroll2') {
                                Ext.apply(config, {
                                    html: record.get('text')
                                });
                            } else if (page == 'withub.ext.std.customPage.widget.WeatherFeed') {
                                Ext.apply(config, {
                                    html: record.get('text'),
                                    resizable: false
                                });
                            } else if (page == 'Ext.Img') {
                                Ext.apply(config, {
                                    width: 100,
                                    height: 100
                                });
                            }
                            widget = me.createWidget(page, config);
                            panel.add(widget);
                            me.selectWidget(widget);
                            return true;
                        }
                    });
                }
            }
        });

        this.items = [
            this.mainPanel,
            this.widgetPanel,
            {
                region: 'south',
                layout: 'border',
                height: 170,
                split: true,
                border: false,
                items: [
                    {
                        region: 'west',
                        width: 320,
                        layout: 'fit',
                        split: true,
                        items: this.dataPointPanel
                    },
                    this.propertyPanel,
                    {
                        width: 170,
                        region: 'east',
                        border: false,
                        layout: {
                            type: 'vbox',
                            padding: '5',
                            align: 'stretch'
                        },
                        defaults: {margins: '0 0 5 0', flex: 1},
                        items: [this.previewButton, this.saveButton]
                    }
                ]
            }
        ];


        this.callParent();

        this.on('afterrender', function () {

            this.loadWidgets();

            this.mainPanel.getEl().on('contextmenu', function (event, t) {
                event.preventDefault();
                Ext.create('Ext.menu.Menu', {
                    items: [
                        {
                            iconCls: 'icon-import',
                            text: '导入模版',
                            handler: function () {
                            }
                        },
                        {
                            iconCls: 'icon-export',
                            text: '导出模版',
                            handler: function () {
                            }
                        },
                        {
                            iconCls: 'icon-image',
                            text: '设置背景图片',
                            handler: function () {
                                var wind = Ext.create('Ext.Window', {
                                    title: '设置背景图片',
                                    width: 320,
                                    height: 85,
                                    modal: true,
                                    border: false,
                                    layout: 'fit',
                                    buttonAlign: 'center',
                                    items: [
                                        Ext.create('Ext.form.Panel', {
                                            bodyPadding: 10,
                                            border: false,
                                            items: [
                                                {
                                                    xtype: 'swfuploadfield',
                                                    name: 'attachment',
                                                    fieldLabel: '选择图片',
                                                    labelWidth: 60,
                                                    anchor: '100%',
                                                    height: 24,
                                                    swfUploadConfig: {
                                                        fileTypes: '*.jpg;*.jpeg;*.png;*.gif;*.bmp;',
                                                        fileTypesDescription: '所有图片'
                                                    },
                                                    listeners: {
                                                        filequeued: function () {
                                                            var form = this.up('form').getForm();
                                                            this.doUpload(function (result) {
                                                                if (!me.backgroundImage) {
                                                                    me.backgroundImage = Ext.create('Ext.Img', {
                                                                        autoEl: 'div',
                                                                        renderTo: 'backgroundImage'
                                                                    });
                                                                }
                                                                me.backgroundImageInfo = result;
                                                                me.backgroundImage.setSrc(PageContext.contextPath + '/std/swf/getTempImage?tempFileName=' + result['tempFileName']);
                                                                wind.close();
                                                            });
                                                        }
                                                    }
                                                }
                                            ]
                                        })
                                    ]
                                }).show();
                            }
                        },
                        {
                            iconCls: 'icon-wrench',
                            text: '设置分辨率',
                            handler: function () {
                            }
                        }
                    ]
                }).showAt(event.getXY());
            });
        }, this);
    },

    createWidget: function (type, config) {
        var me = this;
        var widget = Ext.create(type, Ext.apply({
            style: 'z-index: ' + me.zIndex++
        }, config));

        widget.on('resize', function (component, width, height) {
            if (me.currentSelected == widget.getId()) {
                if (me.propertyPanel.getSource['width'] != width) {
                    me.propertyPanel.setProperty('width', width);
                }
                if (me.propertyPanel.getSource['height'] != height) {
                    me.propertyPanel.setProperty('height', height);
                }
            }
        });

        widget.on('move', function (cmp, x, y) {
            if (me.propertyPanel.getSource['position'] != x + ',' + y) {
                me.propertyPanel.setProperty('position', x + ',' + y);
            }
        });

        widget.on('afterrender', function () {
            widget.getEl().on('mousedown', function (element) {
                if (widget.getId() == me.currentSelected) {
                    return;
                }
                me.selectWidget(widget);
            });

            widget.getEl().on('contextmenu', function (event) {
                event.preventDefault();
                event.stopPropagation();
                Ext.create('Ext.menu.Menu', {
                    items: [
                        {
                            iconCls: 'icon-wrench',
                            text: '设置刷新时间',
                            hidden: Ext.isEmpty(widget.pageData.dataPoints),
                            handler: function () {

                            }
                        },
                        {
                            iconCls: 'icon-image',
                            text: '设置图片',
                            hidden: widget.self.getName() != 'Ext.Img',
                            handler: function () {
                                var wind = Ext.create('Ext.Window', {
                                    title: '设置图片',
                                    width: 320,
                                    height: 85,
                                    modal: true,
                                    border: false,
                                    layout: 'fit',
                                    buttonAlign: 'center',
                                    items: [
                                        Ext.create('Ext.form.Panel', {
                                            bodyPadding: 10,
                                            border: false,
                                            items: [
                                                {
                                                    xtype: 'swfuploadfield',
                                                    name: 'attachment',
                                                    fieldLabel: '选择图片',
                                                    labelWidth: 60,
                                                    anchor: '100%',
                                                    height: 24,
                                                    swfUploadConfig: {
                                                        fileTypes: '*.jpg;*.jpeg;*.png;*.gif;*.bmp;',
                                                        fileTypesDescription: '所有图片'
                                                    },
                                                    listeners: {
                                                        filequeued: function () {
                                                            var form = this.up('form').getForm();
                                                            this.doUpload(function (result) {
                                                                widget.pageData.image = result;
                                                                widget.setSrc(PageContext.contextPath + '/std/swf/getTempImage?tempFileName=' + result['tempFileName']);
                                                                wind.close();
                                                            });
                                                        }
                                                    }
                                                }
                                            ]
                                        })
                                    ]
                                }).show();
                            }
                        },
                        {
                            iconCls: 'icon-delete',
                            text: '删除',
                            handler: function () {
                                me.currentSelected = undefined;
                                me.propertyPanel.setSource({});
                                me.dataPointPanel.getStore().loadData([]);
                                me.dataPointPanel.setVisible(false);
                                widget.destroy();
                            }
                        }
                    ]
                }).showAt(event.getXY());
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
            if (!Ext.isEmpty(widget.pageData['image'])) {
                widget.setSrc(PageContext.contextPath + '/std/file/picture?fileInfoId=' + widget.pageData['image']['fileInfoId']);
            }
        });

        return widget;
    },

    selectWidget: function (widget) {
        var me = this;
        widget.getEl().addCls('block-select');
        widget.getEl().dom.style.zIndex = me.zIndex++;
        if (me.currentSelected && me.getCurrentWidget()) {
            me.getCurrentWidget().getEl().removeCls('block-select');
        }
        me.currentSelected = widget.getId();
        var source = {
            width: widget.getWidth(),
            height: widget.getHeight(),
            position: widget.getXY().join(',')
        };
        var sourceConfig = {
            width: {
                displayName: '宽度',
                editor: Ext.create('Ext.form.field.Number', {minValue: 1, maxValue: 1500, allowDecimals: false})
            },
            height: {
                displayName: '高度',
                editor: Ext.create('Ext.form.field.Number', {minValue: 1, maxValue: 1000, allowDecimals: false})
            },
            position: {
                displayName: '位置'
            }
        }
        //todo
        if (widget.self.getName() == 'Ext.toolbar.TextItem' || widget.self.getName() == 'Ext.ux.TextItem') {
            Ext.apply(source, {
                    text: widget.pageData.text || '',
                    fontFamily: widget.pageData.fontFamily || '',
                    fontSize: widget.pageData.fontSize || '',
                    color: widget.pageData.color || ''
                }
            )
            Ext.apply(sourceConfig, {
                    text: {
                        displayName: '文本'
                    },
                    fontFamily: {
                        displayName: '字体',
                        editor: new Ext.form.field.ComboBox({
                            typeAhead: true,
                            triggerAction: 'all',
                            selectOnTab: true,
                            editable: false,
                            store: [
                                ['宋体', '宋体'],
                                ['黑体', '黑体'],
                                ['楷体', '楷体']
                            ],
                            lazyRender: true,
                            listClass: 'x-combo-list-small'
                        })
                    },
                    fontSize: {
                        displayName: '字体大小',
                        editor: Ext.create('Ext.form.field.Number', {minValue: 1, maxValue: 100, allowDecimals: false})
                    },
                    color: {
                        displayName: '颜色'
                    }
                }
            )
        } else if (widget.self.getName() == 'withub.ext.std.customPage.widget.Clock1') {
            Ext.apply(source, {
                    format: widget.pageData.format = widget.pageData.format || 'Y-m-d',
                    refreshInterval: widget.pageData.refreshInterval = widget.pageData.refreshInterval || '1',
                    fontFamily: widget.pageData.fontFamily || '',
                    fontSize: widget.pageData.fontSize || '',
                    color: widget.pageData.color || ''
                }
            )
            Ext.apply(sourceConfig, {
                    format: {
                        displayName: '格式'
                    },
                    refreshInterval: {
                        displayName: '刷新间隔(秒)',
                        editor: Ext.create('Ext.form.field.Number', {minValue: 0, maxValue: 9999999, allowDecimals: false})
                    },
                    fontFamily: {
                        displayName: '字体',
                        editor: new Ext.form.field.ComboBox({
                            typeAhead: true,
                            triggerAction: 'all',
                            selectOnTab: true,
                            editable: false,
                            store: [
                                ['宋体', '宋体'],
                                ['黑体', '黑体'],
                                ['楷体', '楷体']
                            ],
                            lazyRender: true,
                            listClass: 'x-combo-list-small'
                        })
                    },
                    fontSize: {
                        displayName: '字体大小',
                        editor: Ext.create('Ext.form.field.Number', {minValue: 1, maxValue: 100, allowDecimals: false})
                    },
                    color: {
                        displayName: '颜色'
                    }
                }
            )
        } else if (widget.chart) {
            Ext.apply(source, {
                    title: widget.pageData.title || '',
                    subTitle: widget.pageData.subTitle || ''
                }
            )
            Ext.apply(sourceConfig, {
                    title: {
                        displayName: '主标题'
                    },
                    subTitle: {
                        displayName: '副标题'
                    }
                }
            )
        } else if (widget.self.getName() == 'withub.ext.std.customPage.widget.ImageScroll1') {

        } else if (widget.self.getName() == 'withub.ext.std.customPage.widget.ImageScroll2') {

        } else if (widget.self.getName() == 'Ext.Img') {

        }

        me.propertyPanel.setSource(source, sourceConfig);

        if (widget.dataPoint === true) {
            me.dataPointPanel.setVisible(true);
            if (Ext.isEmpty(widget.pageData.dataPoints)) {
                me.dataPointPanel.getStore().removeAll();
            } else {
                me.dataPointPanel.getStore().loadData(widget.pageData.dataPoints);
            }
        } else if (widget.self.getName() == 'Ext.toolbar.TextItem') {
            me.dataPointPanel.setVisible(true);
            if (Ext.isEmpty(widget.pageData.dataPoints)) {
                me.dataPointPanel.getStore().removeAll();
            } else {
                me.dataPointPanel.getStore().loadData(widget.pageData.dataPoints);
            }
        } else {
            me.dataPointPanel.setVisible(false);
            me.dataPointPanel.getStore().removeAll();
        }
    },

    getCurrentWidget: function () {

        return Ext.getCmp(this.currentSelected);
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
                        cls: ['block'],
                        name: item['name'],
                        width: item['width'],
                        height: item['height'],
                        x: item['left'],
                        y: item['top'],
                        resizable: true,
                        draggable: true,
                        pageData: Ext.decode(item['pageData'])
                    };
                    if (item.page == 'Ext.toolbar.TextItem' || item.page == 'Ext.ux.TextItem') {
                        Ext.apply(config, {
                            cls: ['block', 'text-block'],
                            text: item['name']
                        });
                    } else if (item.page == 'withub.ext.std.customPage.widget.ImageScroll1'
                        || item.page == 'withub.ext.std.customPage.widget.ImageScroll2') {
                        Ext.apply(config, {
                            html: item['name']
                        });
                    } else if (item.page == 'withub.ext.std.customPage.widget.WeatherFeed') {
                        Ext.apply(config, {
                            html: item['name'],
                            resizable: false
                        });
                    } else if (item.page == 'withub.ext.std.customPage.widget.Clock1') {
                        Ext.apply(config, {
                            text: item['name']
                        });
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
    },

    saveCustomPage: function (callback) {
        var me = this;
        var params = {
            objectId: this.customPageId
        };
        if (me.backgroundImageInfo) {
            params['backgroundImageInfo.fileName'] = me.backgroundImageInfo['fileName'];
            params['backgroundImageInfo.tempFileName'] = me.backgroundImageInfo['tempFileName'];
        }
        Ext.each(this.mainPanel.items, function (item, index) {
            var widget = this.mainPanel.getComponent(index);
            var page = widget.self.getName();
            params['customPageItemList[' + index + '].name'] = widget.name;
            params['customPageItemList[' + index + '].page'] = page;
            params['customPageItemList[' + index + '].width'] = widget.getWidth();
            params['customPageItemList[' + index + '].height'] = widget.getHeight();
            params['customPageItemList[' + index + '].left'] = widget.getX() - 1;
            params['customPageItemList[' + index + '].top'] = widget.getY() - 1;
            params['customPageItemList[' + index + '].pageData'] = Ext.encode(widget.pageData)

        }, this);
        Ext.Ajax.request({
            url: PageContext.contextPath + '/std/customPage/save',
            params: params,
            method: 'POST',
            success: function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    callback.call();
                } else {
                    ExtUtil.Msg.error(result['message']);
                }
            }
        });
    }
});


Ext.define('withub.ext.std.customPage.customPageDesign.DataPoint', {
    extend: 'Ext.Window',
    title: '选择数据点',
    width: 640,
    height: 480,
    resizable: false,
    modal: true,
    border: false,
    layout: 'fit',
    buttonAlign: 'center',
    initComponent: function () {

        var me = this;
//        var grid = Ext.create('Ext.grid.Panel', {
//            selType: 'checkboxmodel',
//            store: Ext.create('Ext.data.Store', {
//                fields: ['dataPointId', 'name'],
//                data: {'items': [
//                    { 'dataPointId': 'P-1', "name": "机组-1"},
//                    { 'dataPointId': 'P-2', "name": "机组-2"},
//                    { 'dataPointId': 'P-3', "name": "机组-3"},
//                    { 'dataPointId': 'P-4', "name": "机组-4"}
//                ]},
//                proxy: {
//                    type: 'memory',
//                    reader: {
//                        type: 'json',
//                        root: 'items'
//                    }
//                }
//            }),
//            columns: [
//                {text: '数据点', dataIndex: 'name', flex: 1 }
//            ]
//        });

        var grid = Ext.create('withub.ext.base.Grid', {
            url: '/std/database/query',
            autoQuery: true,
            selType: 'checkboxmodel',
            enablePagginBar: false,
            fields: ["dataPointId", "name"],
            columns: [
                {text: '数据点', dataIndex: 'name', flex: 1 }
            ]
        });

        this.buttons = [
            {
                text: '确定',
                handler: function () {
                    me.fireEvent('select', grid.getSelectionModel().getSelection());
                    this.up('window').close();
                }
            },
            {
                text: '取消',
                handler: function () {
                    this.up('window').close();
                }
            }
        ];

        this.items = [grid];

        this.callParent();

        this.on('afterrender', function () {
            if (me.value) {
                var names = [];
                Ext.each(Ext.decode(me.value), function (dataPoint) {
                    names.push(dataPoint['name']);
                    grid.getSelectionModel().select(grid.getStore().getAt
                    (grid.getStore().find('dataPointId', dataPoint['dataPointId'])), true);
                });
                me.setRawValue(names.join(','));
            }
        })
    }
});

Ext.define('Ext.ux.TextItem', {
    extend: 'Ext.toolbar.TextItem'
});

window.data1 = [
    {
        dataPointId: '1',
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