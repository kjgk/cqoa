Ext.define('withub.ext.std.customPage.widget.DualAxesChart2', {
    extend: 'Ext.Component',
    width: 640,
    height: 360,
    layout: 'fit',
    initComponent: function () {

        this.callParent();

        this.on('resize', function (component, width, height) {
            this.chart.setSize(width - 4, height - 4, true);
        });

        this.on('afterrender', function (component) {

            this.chart = new Highcharts.Chart({
                chart: {
                    renderTo: component.getId()
                },
                title: {
                    text: '主标题'
                },
                subtitle: {
                    text: '副标题'
                },
                xAxis: {
                    categories: [
                        '一月',
                        '二月',
                        '三月',
                        '四月',
                        '五月',
                        '六月',
                        '七月',
                        '八月',
                        '九月',
                        '十月',
                        '十一月',
                        '十二月'
                    ]
                },
                yAxis: [
                    { // Primary yAxis
                        labels: {
                            format: '{value}°C',
                            style: {
                                color: '#41B3DD'
                            }
                        },
                        title: {
                            text: '温度',
                            style: {
                                color: '#41B3DD'
                            }
                        }
                    },
                    { // Secondary yAxis
                        title: {
                            text: '功耗',
                            style: {
                                color: '#E23C2B'
                            }
                        },
                        labels: {
                            format: '{value} KW',
                            style: {
                                color: '#E23C2B'
                            }
                        },
                        opposite: true
                    }
                ],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    x: 120,
                    verticalAlign: 'top',
                    y: 100,
                    floating: true,
                    backgroundColor: '#FFFFFF'
                },
                series: [
                    {
                        name: '功耗',
                        color: '#E23C2B',
                        type: 'column',
                        yAxis: 1,
                        data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
                        tooltip: {
                            valueSuffix: ' mm'
                        }

                    },
                    {
                        name: '温度',
                        color: '#41B3DD',
                        type: 'spline',
                        data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6],
                        tooltip: {
                            valueSuffix: '°C'
                        }
                    }
                ]
            });
        }, this)
    }
});