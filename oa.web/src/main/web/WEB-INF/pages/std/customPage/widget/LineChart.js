Ext.define('withub.ext.std.customPage.widget.LineChart', {
    extend: 'Ext.Component',
    width: 640,
    height: 360,
    layout: 'fit',
    dataPoint: true,
    initComponent: function () {

        this.callParent();

        this.on('resize', function (component, width, height) {
            this.chart.setSize(width - 4, height - 4, true);
        });

        this.on('afterrender', function (component) {
            this.chart = new Highcharts.Chart({
                chart: {
                    renderTo: component.getId(),
                    type: 'spline'
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
                yAxis: {
                    min: 0,
                    title: {
                        text: '总能耗(KW)'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.1f} KW</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: []
            });
        }, this)
    }
});