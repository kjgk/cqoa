Ext.define('withub.ext.std.customPage.widget.FunnelChart', {
    extend: 'Ext.Component',
    width: 480,
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
                    renderTo: component.getId(),
                    type: 'funnel',
                    marginRight: 100
                },
                title: {
                    text: '容量',
                    x: -50
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b> ({point.y:,.0f})',
                            color: 'black',
                            softConnector: true
                        },
                        neckWidth: '30%',
                        neckHeight: '25%'

                        //-- Other available options
                        // height: pixels or percent
                        // width: pixels or percent
                    }
                },
                legend: {
                    enabled: false
                },
                series: [
                    {
                        name: 'Unique users',
                        data: [
                            ['Website visits', 12654],
                            ['Downloads', 4064],
                            ['Requested price list', 1987],
                            ['Invoice sent', 976],
                            ['Finalized', 846]
                        ]
                    }
                ]
            });
        }, this)
    }
});