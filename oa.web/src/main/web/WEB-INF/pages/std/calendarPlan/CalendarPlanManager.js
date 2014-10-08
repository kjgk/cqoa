Ext.define('withub.ext.std.calendarPlan.CalendarPlanManager', {

    requires:[
        'Extensible.calendar.CalendarPanel',
        'Extensible.calendar.data.EventModel',
        'Extensible.calendar.data.EventMappings'
    ],

    constructor:function () {

        Extensible.Date.use24HourTime = true;

        Extensible.calendar.data.EventMappings.EventId.name = 'objectId';       // 重新映射字段
        Extensible.calendar.data.EventMappings.EventId.mapping = 'objectId';       // 重新映射字段
        Extensible.calendar.data.EventMappings.EventId.type = 'string';       // 重新映射字段
        Extensible.calendar.data.EventModel.reconfigure();

        this.calendarStore = Ext.create('Ext.data.JsonStore', {
            autoLoad:true,
            model:'Extensible.calendar.data.CalendarModel',
            requires:[
                'Ext.data.reader.Json',
                'Extensible.calendar.data.CalendarModel',
                'Extensible.calendar.data.CalendarMappings'
            ],
            proxy:{
                type:'ajax',
                url:PageContext.contextPath + '/std/calendarPlan/importanceLevel/query',
                noCache:false,
                reader:{
                    type:'json',
                    root:'items'
                }
            }
        });

        this.eventStore = Ext.create('Ext.data.JsonStore', {
            autoLoad:true,
            model:'Extensible.calendar.data.EventModel',
            requires:[
                'Ext.data.reader.Json',
                'Extensible.calendar.data.EventModel',
                'Extensible.calendar.data.EventMappings'
            ],
//            idProperty:'obejectId',
            proxy:{
                type:'ajax',
                url:PageContext.contextPath + '/std/calendarPlan/query',
                reader:{
                    type:'json',
                    root:'items'
                },
                listeners:{
                    exception:function (proxy, response, operation, options) {
                        var msg = response.message ? response.message : Ext.decode(response.responseText).message;
                        Ext.Msg.alert('Server Error', msg);
                    }
                }
            },
            listeners:{
                beforeload:function (store, operation, options) {
//                    operation.params.end = Ext.util.Format.date(operation.params.end, 'Y-m-d');
//                    operation.params.start = Ext.util.Format.date(operation.params.start, 'Y-m-d');
                }
            }
        });


        Ext.create('Ext.Viewport', {
            layout:'border',
            items:[
                {
                    id:'app-west',
                    region:'west',
                    width:179,
                    items:[
                        {
                            xtype:'datepicker',
                            id:'app-nav-picker',
                            cls:'ext-cal-nav-picker',
                            listeners:{
                                'select':{
                                    fn:function (dp, dt) {
                                        Ext.getCmp('app-calendar').setStartDate(dt);
                                    },
                                    scope:this
                                }
                            }
                        },
                        {
                            xtype:'extensible.calendarlist',
                            store:this.calendarStore,
                            border:false,
                            height:400,
                            width:240
                        }
                    ]
                },
                {
                    xtype:'extensible.calendarpanel',
                    eventStore:this.eventStore,
                    calendarStore:this.calendarStore,
                    border:false,
                    dayText:'天',
                    weekText:'周',
                    monthText:'月',
                    todayText:'今天',
                    jumpToText:'跳转',
                    goText:'到',
//                    showDayView:false,
//                    showMonthView:false,
                    showMultiWeekView:false,
//                    readOnly:true,
//                    showWeekView:false,
                    id:'app-calendar',
                    region:'center',
                    activeItem:1, // month view     hourHeight
                    weekViewCfg:{
                        startDay:1
//                        startDayIsStatic:true,
//                        dayCount:5
                    },
                    monthViewCfg:{
//                        showHeader:true,
                        showWeekLinks:true
//                        showWeekNumbers:true
                    },
                    viewConfig:{
                        startDay:1,
                        hourHeight:48,
                        enableDD:false, // 禁用拖动
                        enableContextMenus:false, // 禁用右键菜单
                        viewStartHour:9,
                        viewEndHour:18
                    },
                    listeners:{
                        'eventclick':{
                            fn:function (vw, rec, el) {
                                var objectId = rec.data.objectId;
                                this.editWin = Ext.create('withub.ext.std.calendarPlan.CalendarPlan', {
                                    objectId:objectId,
                                    listeners:{
                                        success:function () {
                                            this.eventStore.load();
                                        },
                                        scope:this
                                    }
                                }).show();
                            },
                            scope:this
                        },
                        'dayclick':{
                            fn:function (vw, dt, ad, el) {
                                this.editWin = Ext.create('withub.ext.std.calendarPlan.CalendarPlan', {
                                    startTime:dt,
                                    deleteButton:true,
                                    listeners:{
                                        success:function () {
                                            this.eventStore.load();
                                        },
                                        scope:this
                                    }
                                }).show();
                            },
                            scope:this
                        },
                        'viewchange':{
                            fn:function (p, vw, dateInfo) {
                                if (dateInfo) {
                                    Ext.getCmp('app-nav-picker').setValue(dateInfo.activeDate);
                                }
                            },
                            scope:this
                        },
                        'eventmove':{
                            fn:function (vw, rec) {
                                var params = {};
                                params['objectId'] = rec.data.objectId;
                                params['startTime'] = Ext.util.Format.date(rec.data.StartDate, 'Y-m-d H:i');
                                params['endTime'] = Ext.util.Format.date(rec.data.EndDate, 'Y-m-d H:i');

                                Ext.Ajax.request({
                                    url:PageContext.contextPath + '/std/calendarPlan/update',
                                    params:params,
                                    method:'POST',
                                    success:function (response) {

                                        Ext.getCmp('app-calendar').setStartDate(new Date());
                                        this.eventStore.load();
                                    },
                                    failure:function (response) {
                                        ExtUtil.Msg.error(response.responseText);
                                    },
                                    scope:this
                                });
                            },
                            scope:this
                        }
                    }
                }
            ]
        });

//  国际化代码
//        Ext.Ajax.request({
//            url:PageContext.contextPath + '/scripts/extjs/extensible/src/locale/extensible-lang-zh_CN.js',
//            disableCaching:false,
//            success:function (resp, opts) {
//                eval(resp.responseText); // apply the Extensible locale overrides
//            }
//        });


    }
});