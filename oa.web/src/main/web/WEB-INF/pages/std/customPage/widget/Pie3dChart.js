Ext.define('withub.ext.std.customPage.widget.Pie3dChart', {
    extend: 'Ext.Component',
    width: 480,
    height: 320,
    layout: 'fit',
    initComponent: function () {

        this.callParent();

        this.on('resize', function (component, width, height) {
            this.chart.setSize(width - 4, height - 4, true);
        });

        var data = [];
        Ext.each(window.data1, function (_data) {
            var value = 0;
            Ext.each(_data.data, function (v) {
                value += v;
            });
            data.push([_data.name, Math.round(value)])
        });

        this.on('afterrender', function (component) {


            this.chart = new Highcharts.Chart({
                chart: {
                    renderTo: component.getId(),
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                colors: Highcharts.map(Highcharts.getOptions().colors, function (color) {
                    return {
                        radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
                        stops: [
                            [0, color],
                            [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                        ]
                    };
                }),
                title: {
                    text: '标题'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.y}</b>',
                    percentageDecimals: 2
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            color: '#000000',
                            connectorColor: '#000000',
                            formatter: function () {
                                return '<b>' + this.point.name + '</b>: ' + Math.round(this.percentage) + ' %';
                            }
                        }
                    }
                },
                series: [
                    {
                        type: 'pie',
                        name: '功耗',
                        data: data,
                        shadow: true
                    }
                ]
            });
        }, this)
    }
});