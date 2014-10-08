Ext.define('withub.ext.std.customPage.widget.CombinationChart', {
    extend: 'Ext.Component',
    width: 640,
    height: 480,
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
                    text: '标题'
                },
                xAxis: {
                    categories: ['1', '2', '3', '4', '5']
                },
                tooltip: {
                    formatter: function () {
                        var s;
                        if (this.point.name) { // the pie chart
                            s = '' +
                                this.point.name + ': ' + this.y + ' fruits';
                        } else {
                            s = '' +
                                this.x + ': ' + this.y;
                        }
                        return s;
                    }
                },
                series: [
                    {
                        type: 'column',
                        name: '设备-1',
                        data: [3, 2, 1, 3, 4]
                    },
                    {
                        type: 'column',
                        name: '设备-2',
                        data: [2, 3, 5, 7, 6]
                    },
                    {
                        type: 'column',
                        name: '设备-3',
                        data: [4, 3, 3, 9, 0]
                    },
                    {
                        type: 'spline',
                        name: '设备-4',
                        data: [3, 2.67, 3, 6.33, 3.33],
                        marker: {
                            lineWidth: 2,
                            lineColor: Highcharts.getOptions().colors[3],
                            fillColor: 'white'
                        }
                    },
                    {
                        type: 'pie',
                        name: 'Total consumption',
                        data: [
                            {
                                name: '设备-1',
                                y: 13,
                                color: Highcharts.getOptions().colors[0] // Jane's color
                            },
                            {
                                name: '设备-2',
                                y: 23,
                                color: Highcharts.getOptions().colors[1] // John's color
                            },
                            {
                                name: '设备-3',
                                y: 19,
                                color: Highcharts.getOptions().colors[2] // Joe's color
                            }
                        ],
                        center: [100, 80],
                        size: 100,
                        showInLegend: false,
                        dataLabels: {
                            enabled: false
                        }
                    }
                ]
            });
        }, this)
    }
});