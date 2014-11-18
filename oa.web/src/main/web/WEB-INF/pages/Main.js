Ext.define('withub.ext.Main', {
    extend: 'Ext.Viewport',
    layout: 'border',
    initComponent: function () {

        var topPanel = Ext.create('Ext.Panel', {
            region: 'north',
            split: true,
            height: 80,
            contentEl: 'header',
            border: false
        });

        var tabPanel = Ext.create('Ext.tab.Panel', {
            id: 'tab-panel',
            region: 'center',
            border: false,
            margins: '0 5 5 0'
        });

        var loginLabel = Ext.create('Ext.form.Label', {
            style: {
                color: '#003399',
                fontSize: '13px',
                fontWeight: 'bold'
            }
        });

        var menuPanel = Ext.create('Ext.Panel', {
            title: '菜单',
            layout: 'accordion',
            region: 'west',
            split: true,
            width: 240,
            margins: '0 0 5 5'
        });

        // 加载当前登录用户信息
        ExtUtil.request({
            async: false,
            url: PageContext.contextPath + '/security/getCurrentUserInfo',
            success: function (result) {
                var currentUser = result['userInfo'];
                loginLabel.setText('当前用户: ' + currentUser.name);
                PageContext.currentUser = currentUser;
            }
        });

        Ext.Ajax.request({
            url: PageContext.contextPath + '/security/loadMenu',
            method: 'GET',
            params: {
                node: 'Root'
            },
            success: function (response) {
                var result = Ext.decode(response.responseText);
                Ext.each(result['nodes'], function (node) {
                    if (node.id == '2A52FA66-0FA7-4A86-AC1E-4F03D2CD9454') {
                        return true;
                    }
                    var treePanel = Ext.create('withub.ext.base.Tree', {
                        title: '<span style="color: #003399; font-size: 14px; font-weight: bold;">' + node.text + '</span>',
                        singleExpand: false,
                        rootVisible: false,
                        lines: true,
                        iconCls: Ext.isEmpty(node.iconCls) ? undefined : node.iconCls,
                        url: '/security/loadMenu',
                        root: {
                            id: node.id
                        },
                        listeners: {
                            itemclick: function (view, record, item, index, e, options) {
                                var text = record.get('text');
                                var iconCls = record.get('iconCls');
                                iconCls = '';
                                var attributes = record.get('attributes');
                                var page = attributes['page'];
                                var openMode = attributes['openMode'];
                                var pageParams = attributes['pageParams'];

                                if (Ext.isEmpty(page)) {
                                    if (record.get('leaf')) {
                                        ExtUtil.Msg.info('开发中');
                                        return;
                                    } else {
                                        return;
                                    }
                                }

                                if (openMode == 0) {
                                    Ext.create(page, Ext.apply({
                                        id: page,
                                        title: text,
                                        iconCls: iconCls
                                    }, pageParams)).show();
                                } else if (openMode == 1) {
                                    var frame = tabPanel.getComponent(page);
                                    if (!frame) {
                                        frame = Ext.create('Ext.ux.IFrame', {
                                            id: page,
                                            closable: true,
                                            title: text,
                                            iconCls: iconCls,
                                            src: PageContext.contextPath + '/loadPage/' + page,
                                            listeners: {
                                                load: function () {
                                                    if (frame.getWin().location.href.indexOf('login.page') != -1) {
                                                        frame.getWin().parent.location.reload();
                                                    }
                                                },
                                                scope: this
                                            }
                                        });
                                        frame.on('afterrender', function () {
                                            frame.el.mask('界面加载中...');
                                        });
                                        tabPanel.add(frame);
                                    }
                                    tabPanel.setActiveTab(frame);
                                } else if (openMode == 2) {
                                    window.open(page);
                                }
                            },
                            scope: this
                        }
                    });
                    menuPanel.add(treePanel)
                });
            }
        });

        this.items = [
            topPanel,
            menuPanel,
            tabPanel
        ];

//        tabPanel.add(Ext.create('Ext.ux.IFrame', {
//            closable: false,
//            title: '任务卡监控',
//            src: PageContext.contextPath + '/loadPage/withub.ext.dye.processCard.ProcessCardMonitor',
//            listeners: {
//                load: function () {
//                    tabPanel.el.unmask();
//                },
//                scope: this
//            }
//        }));
//        tabPanel.setActiveTab(0);
//        tabPanel.doLayout();

        this.callParent();
    }
});