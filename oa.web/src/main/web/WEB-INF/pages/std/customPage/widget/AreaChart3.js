Ext.define('withub.ext.std.customPage.widget.AreaChart3', {
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
                    renderTo: component.getId(),
                    type: 'area'
                },
                title: {
                    text: '标题'
                },
                xAxis: {
                    labels: {
                        formatter: function () {
                            return this.value; // clean, unformatted number for year
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: '温度℃'
                    },
                    labels: {
                        formatter: function () {
                            return this.value + '℃';
                        }
                    }
                },
                tooltip: {
                },
                plotOptions: {
                    area: {
                        marker: {
                            enabled: false,
                            symbol: 'circle',
                            radius: 2,
                            states: {
                                hover: {
                                    enabled: true
                                }
                            }
                        }
                    }
                },
                series: [
                    {
                        name: '区域-1',
                        data: [ 10, 9, 9, 8, 8, 7, 9, 11, 12, 15, 16, 18, 20, 21, 22, 24, 25, 22, 21, 18, 14, 16, 15, 15]
                    },
                    {
                        name: '区域-2',
                        data: [ 8, 6, 6, 7, 9, 9, 10, 11, 14, 16, 17, 18, 22, 22, 22, 24, 21, 19, 18, 17, 14, 12, 10, 7]
                    }
                ]
            });
        }, this)
    }
});