Ext.define('withub.ext.std.customPage.widget.DonutPieChart', {
    extend: 'Ext.Component',
    width: 480,
    height: 400,
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

            var colors = Highcharts.getOptions().colors, categories = [], data = [];

            Ext.each(window.data1, function (_data, index) {
                var value = 0, quarterValues = [0, 0, 0, 0], quarterCategories = ['一季度', '二季度', '三季度', '四季度'];
                Ext.each(_data.data, function (v, i) {
                    value += v;
                    var q = new Number(Math.floor(i / 3));
                    quarterValues[q] = quarterValues[q] + v;
                });
                categories.push(_data.name);

                data.push({
                    y: value,
                    color: colors[index],
                    drilldown: {
                        name: _data.name,
                        categories: quarterCategories,
                        data: quarterValues,
                        color: colors[index]
                    }
                });
            });

            // Build the data arrays
            var browserData = [];
            var versionsData = [];
            for (var i = 0; i < data.length; i++) {

                // add browser data
                browserData.push({
                    name: categories[i],
                    y: data[i].y,
                    color: data[i].color
                });

                // add version data
                for (var j = 0; j < data[i].drilldown.data.length; j++) {
                    var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5;
                    versionsData.push({
                        name: data[i].drilldown.categories[j],
                        y: data[i].drilldown.data[j],
                        color: Highcharts.Color(data[i].color).brighten(brightness).get()
                    });
                }
            }


            this.chart = new Highcharts.Chart({
                chart: {
                    type: 'pie',
                    renderTo: component.getId()
                },
                title: {
                    text: '标题'
                },
                yAxis: {
                    title: {
                        text: ''
                    }
                },
                plotOptions: {
                    pie: {
                        shadow: false,
                        center: ['50%', '50%']
                    }
                },
                tooltip: {
                    valueSuffix: 'KW'
                },
                series: [
                    {
                        name: '全年',
                        data: browserData,
                        size: '40%',
                        dataLabels: {
                            formatter: function () {
                                return this.y > 5 ? this.point.name : null;
                            },
                            color: 'white',
                            distance: -30
                        }
                    },
                    {
                        name: '季度',
                        data: versionsData,
                        size: '60%',
                        innerSize: '40%',
                        dataLabels: {
                            formatter: function () {
                                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y : null;
                            }
                        }
                    }
                ]
            });
        }, this)
    }
});