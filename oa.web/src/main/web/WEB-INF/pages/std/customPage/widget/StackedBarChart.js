Ext.define('withub.ext.std.customPage.widget.StackedBarChart', {
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
                    text: 'Stacked bar chart'
                },
                xAxis: {
                    categories: ['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Total fruit consumption'
                    }
                },
                legend: {
                    backgroundColor: '#FFFFFF',
                    reversed: true
                },
                plotOptions: {
                    series: {
                        stacking: 'normal'
                    }
                },
                series: [
                    {
                        name: 'John',
                        data: [5, 3, 4, 7, 2]
                    },
                    {
                        name: 'Jane',
                        data: [2, 2, 3, 2, 1]
                    },
                    {
                        name: 'Joe',
                        data: [3, 4, 4, 2, 5]
                    }
                ]
            });
        }, this)
    }
});