Ext.define('withub.ext.std.customPage.widget.BarChart', {
    extend: 'Ext.Component',
    width: 480,
    height: 320,
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
                    type: 'bar'
                },
                title: {
                    text: 'Historic World Population by Region'
                },
                subtitle: {
                    text: 'Source: Wikipedia.org'
                },
                xAxis: {
                    categories: ['Africa', 'America', 'Asia', 'Europe', 'Oceania'],
                    title: {
                        text: null
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Population (millions)',
                        align: 'high'
                    },
                    labels: {
                        overflow: 'justify'
                    }
                },
                tooltip: {
                    valueSuffix: ' millions'
                },
                plotOptions: {
                    bar: {
                        dataLabels: {
                            enabled: true
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                series: [
                    {
                        name: 'Year 1800',
                        data: [107, 31, 635, 203, 2]
                    },
                    {
                        name: 'Year 1900',
                        data: [133, 156, 947, 408, 6]
                    },
                    {
                        name: 'Year 2008',
                        data: [973, 914, 4054, 732, 34]
                    }
                ]
            });
        }, this)
    }
});