Ext.define('withub.ext.std.workCalendar.WorkCalendarHolidayManager', {
    extend: 'Ext.window.Window',
    title: '工作日历',
    requires: [
        'Extensible.calendar.CalendarPanel',
        'Extensible.calendar.data.EventModel',
        'Extensible.calendar.data.EventMappings'
    ],
    initComponent: function () {

        this.eventStore = Ext.create('Ext.data.JsonStore', {
            model: 'Extensible.calendar.data.EventModel',
            requires: [
                'Ext.data.reader.Json',
                'Extensible.calendar.data.EventModel',
                'Extensible.calendar.data.EventMappings'
            ],
            proxy: new Ext.data.MemoryProxy()
        });

        var columns = [];
        for (var i = 0; i < 12; i++) {
            columns.push({
                xtype: 'extensible.calendarpanel',
                width: 175,
                height: 160,
                id: "calendarpanel" + i,
                month: i,
                eventStore: this.eventStore,
                border: true,
                showNavBar: false,
                showNavNextPrev: false,
                showNavJump: false,
                showNavToday: false,
                showTodayText: false,
                showDayView: false,
                showMultiWeekView: false,
                showWeekView: false,
                monthViewCfg: {
                    showTime: false,
                    nextMonthCls: 'ext-cal-day-next-yearView',
                    prevMonthCls: 'ext-cal-day-prev-yearView',
                    weekendCls: '',
                    todayCls: ''
                },
                viewConfig: {
                    showHeader: true,
                    startDay: 1,
                    enableDD: false, // 禁用拖动
                    enableContextMenus: false, // 禁用右键菜单
                    viewStartHour: 9,
                    viewEndHour: 18
                },
                listeners: {
                    afterrender: {
                        fn: function (options) {
                            options.setStartDate(Ext.Date.add(new Date(this.calendarWin.year + '.1.1'), Ext.Date.MONTH, options.month));
                            var monthToolbar = new Ext.Toolbar({
                                dock: 'top',
                                defaultAlign: 'center',
                                items: [
                                    { text: Ext.Date.format(options.startDate, 'M')}
                                ]
                            });
                            options.addDocked(monthToolbar);

                            var exists = Ext.Function.bind(Ext.ClassManager.get, Ext.ClassManager);

                            if (exists('Extensible.calendar.template.BoxLayout')) {
                                Ext.apply(Extensible.calendar.template.BoxLayout.prototype, {
                                    multiDayMonthStartFormat: 'j',
                                    multiDayFirstDayFormat: 'j'
                                });
                            }
                        },
                        scope: this
                    },
                    dayclick: {
                        fn: function (vw, dt, ad, el) {
                            if (vw.startDate.getMonth() != dt.getMonth()) {
                                return false;
                            }

                            var holiday;
                            var url = PageContext.contextPath + '/std/workCalendarHoliday/';

                            if (el.hasCls('ext-cal-day-we-yearView')) {
                                holiday = 0;
                                url = url + 'cancel';
                            } else {
                                holiday = 1;
                                url = url + 'set';
                            }

                            var params = {
                                'workCalendar.objectId': this.workCalendarId,
                                'day': Ext.Date.format(dt, 'Y-m-d'),
                                'holiday': holiday
                            }

                            var mask = new Ext.LoadMask(this.calendarWin.id, {msg: '正在保存数据，请稍后...'});
                            mask.show();

                            Ext.Ajax.request({
                                url: url,
                                params: params,
                                success: function (response) {
                                    mask.hide();
                                    if (el.hasCls('ext-cal-day-we-yearView')) {
                                        el.removeCls('ext-cal-day-we-yearView');
                                    } else {
                                        el.addCls('ext-cal-day-we-yearView');
                                    }
                                },
                                failure: function (response) {
                                    ExtUtil.Msg.error(response.responseText);
                                },
                                scope: this
                            });
                        },
                        scope: this
                    }
                }
            })
        }


        this.calendarWin = Ext.create('Ext.Panel', {
            modal: true,
            layout: {
                type: 'table',
                columns: 4
            },
            defaults: {
                style: {
                    margin: '1px 1px'
                }
            },
            width: 710,
            year: new Date().getFullYear(),
            height: 515,
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            text: '上一年',
                            handler: function () {
                                var mask = new Ext.LoadMask(this.calendarWin.id, {msg: '正在加载数据，请稍后...'});
                                mask.show();
                                this.calendarWin.year = new Number(Ext.getCmp('yearText').text) - 1;
                                Ext.getCmp('yearText').setText(this.calendarWin.year);
                                this.loadYearView();
                                this.loadHolidays();
                                mask.hide();
                            },
                            scope: this
                        },
                        {
                            id: 'yearText',
                            text: Ext.Date.format(new Date(), 'Y')
                        },
                        {
                            xtype: 'button',
                            text: '下一年',
                            handler: function () {
                                var mask = new Ext.LoadMask(this.calendarWin.id, {msg: '正在加载数据，请稍后...'});
                                mask.show();
                                this.calendarWin.year = new Number(Ext.getCmp('yearText').text) + 1;
                                Ext.getCmp('yearText').setText(this.calendarWin.year);

                                this.loadYearView();
                                this.loadHolidays();
                                mask.hide();
                            },
                            scope: this
                        }
                    ]
                }
            ],
            items: columns,
            listeners: {

            }
        });

        this.items = [this.calendarWin];

        this.callParent();

        this.on('afterrender', function () {
            this.loadHolidays();
        }, this);
    },

    loadYearView: function () {
        for (var i = 0; i < 12; i++) {
            var monthview = Ext.getCmp('calendarpanel' + i);
            monthview.setStartDate(Ext.Date.add(new Date(this.calendarWin.year + '.1.1'), Ext.Date.MONTH, i));
        }
    },

    loadHolidays: function () {

        var startDate = Ext.Date.format(new Date(this.calendarWin.year + '.1.1'), 'Y-m-d');
        var endDate = Ext.Date.format(new Date(this.calendarWin.year + '.12.31'), 'Y-m-d');
        var mask = new Ext.LoadMask(this.calendarWin.id, {msg: '正在加载数据，请稍后...'});
        mask.show();
        Ext.Ajax.request({
            url: PageContext.contextPath + '/std/workCalendarHoliday/query',
            params: {
                beginDate: startDate,
                endDate: endDate,
                'workCalendarId': this.workCalendarId
            },
            success: function (response) {
                var result = Ext.decode(response.responseText);
                var day = new Date(startDate);
                if (this.weekendHoliday) {
//                    for (var i = 0; i < 12; i++) {
//                        var monthview = Ext.getCmp('calendarpanel' + i);
//                        Ext.apply(monthview.monthViewCfg, {weekendCls:'ext-cal-day-we-yearView'});
//                    }

                    while (Ext.Date.between(day, new Date(startDate), new Date(endDate))) {
                        if (Ext.Date.format(day, 'w') == 6 || Ext.Date.format(day, 'w') == 0) {
                            Ext.get('calendarpanel' + day.getMonth() + '-month-day-' + Ext.Date.format(day, 'Ymd')).addCls('ext-cal-day-we-yearView');
                        }
                        day = Ext.Date.add(day, Ext.Date.DAY, 1)
                    }
                }
                Ext.each(result.items, function (item) {
                    var day = Ext.Date.format(new Date(item.day), 'Ymd');
                    var el = Ext.get('calendarpanel' + new Date(item.day).getMonth() + '-month-day-' + day);
                    if (!Ext.isEmpty(el)) {
                        if (item.holiday == 1) {
                            el.addCls('ext-cal-day-we-yearView');
                        } else {
                            el.removeCls('ext-cal-day-we-yearView');
                        }
                    }
                })

                mask.hide();

            },
            scope: this
        });


    }
});

