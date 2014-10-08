Ext.define('withub.ext.std.customPage.widget.GaugeChart', {
    extend: 'Ext.Component',
    width: 200,
    height: 240,
    layout: 'fit',
    initComponent: function () {

        this.callParent();

        this.on('resize', function (component, width, height) {
            this.chart.setSize(width - 4, height - 4, true);
        });

        this.on('afterrender', function (component) {

            this.chart = new Highcharts.Chart({
                chart: {
                    renderTo: component.getId(),
                    type: 'gauge',
                    plotBackgroundColor: null,
                    plotBackgroundImage: null,
                    plotBorderWidth: 0,
                    plotShadow: false
                },

                title: {
                    text: '温度（℃）'
                },

                pane: {
                    startAngle: -150,
                    endAngle: 150,
                    background: [
                        {
                            backgroundColor: {
                                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                                stops: [
                                    [0, '#FFF'],
                                    [1, '#333']
                                ]
                            },
                            borderWidth: 0,
                            outerRadius: '109%'
                        },
                        {
                            backgroundColor: {
                                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                                stops: [
                                    [0, '#333'],
                                    [1, '#FFF']
                                ]
                            },
                            borderWidth: 1,
                            outerRadius: '107%'
                        },
                        {
                            // default background
                        },
                        {
                            backgroundColor: '#DDD',
                            borderWidth: 0,
                            outerRadius: '105%',
                            innerRadius: '103%'
                        }
                    ]
                },

                // the value axis
                yAxis: {
                    min: 0,
                    max: 100,

                    minorTickInterval: 'auto',
                    minorTickWidth: 1,
                    minorTickLength: 10,
                    minorTickPosition: 'inside',
                    minorTickColor: '#666',

                    tickPixelInterval: 30,
                    tickWidth: 2,
                    tickPosition: 'inside',
                    tickLength: 10,
                    tickColor: '#666',
                    labels: {
                        step: 2,
                        rotation: 'auto'
                    },
                    title: {
                        text: '℃'
                    },
                    plotBands: [
                        {
                            from: 0,
                            to: 20,
                            color: '#0077FF' // blue
                        },
                        {
                            from: 20,
                            to: 40,
                            color: '#55BF3B' // green
                        },
                        {
                            from: 40,
                            to: 60,
                            color: '#DDDF0D' // yellow
                        },
                        {
                            from: 60,
                            to: 100,
                            color: '#DF5353' // red
                        }
                    ]
                },

                series: [
                    {
                        name: '温度',
                        data: [25],
                        tooltip: {
                            valueSuffix: ' ℃'
                        }
                    }
                ]

            });
        }, this)
    }
});