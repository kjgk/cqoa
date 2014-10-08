Ext.define('withub.ext.system.user.UserList', {
    extend: 'Ext.Viewport',
    closable: true,
    layout: 'fit',
    requires: ['withub.ext.system.user.UserModel'],
    defaultQueryStatusTag: 'Active',

    initComponent: function () {
        this.gridPanel = Ext.create('withub.ext.common.ManagerGrid', {
            model: 'withub.ext.system.user.UserModel',
            baseUrl: '/system/user',
            enablePagginBar: true,
            enableDeleteItem: true,
            userInterfaceTag: 'UserList',
            columns: [
                {xtype: 'rownumberer', width: 32 },
                {text: '姓名', width: 100, dataIndex: 'name' },
                {text: '性别', width: 70, dataIndex: 'sex', align: 'center'},
                {text: '帐号', width: 110, dataIndex: 'account' },
                {text: '组织机构', minWidth: 200, flex: 1, dataIndex: 'organization'},
                {text: '角色', width: 160, dataIndex: 'role'},
                {text: '手机', width: 150, dataIndex: 'mobile'},
                {text: '状态', width: 50, dataIndex: 'status'}
            ],
            tbar: [
                '组织机构',
                {
                    itemId: 'organization',
                    xtype: 'treecombo',
                    width: 220,
                    treeWidth: 360,
                    url: '/system/organization/loadTree',
                    showPathName: true,
                    pathNameDepth: 3,
                    emptyText: '请选择组织机构'
                },
                '角色',
                {
                    itemId: 'role',
                    xtype: 'simplecombo',
                    entity: 'Role',
                    order: 'orderNo',
                    emptyText: '请选择角色',
                    showAll: true
                },
                '状态',
                {
                    itemId: 'status',
                    width: 70,
                    xtype: 'codecombo',
                    codeColumnTag: 'UserStatus',
                    defaultCodeTag: 'Active',
                    showAll: true
                },
                '姓名',
                {
                    itemId: 'name',
                    xtype: 'textfield',
                    width: 100,
                    emptyText: '请输入姓名'
                },
                {
                    xtype: 'button',
                    text: '搜索',
                    iconCls: 'icon-query',
                    handler: this.queryUser,
                    scope: this
                },
                '-',
                {
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        Ext.create('withub.ext.system.user.User', {
                            action: 'create',
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                }
            ]
        });

        this.gridPanel.on('createcontextmenu', function (items, store, record, index, event) {
            var me = this;
            var objectId = record.get('objectId');
            var name = record.get('name');
            var accountId = record.get('accountId');
            items.push(
                {
                    text: '编辑',
                    iconCls: 'icon-edit',
                    handler: function () {
                        Ext.create('withub.ext.system.user.User', {
                            action: 'update',
                            objectId: objectId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '设置账号',
                    iconCls: 'icon-key',
                    handler: function () {
                        Ext.create('withub.ext.system.user.Account', {
                            userId: objectId,
                            objectId: accountId,
                            listeners: {
                                success: function () {
                                    this.gridPanel.getStore().load();
                                },
                                scope: this
                            }
                        }).show();
                    },
                    scope: this
                },
                {
                    text: '设置随机密码',
                    iconCls: 'icon-key',
                    handler: function () {
                        ExtUtil.Msg.confirm('确定设置随机密码吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/account/createRandomPassword/" + accountId,
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("设置成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                },
                {
                    text: '设为系统管理员',
                    iconCls: 'icon-user-orange',
                    menuTag: 'SetAdministrator',
                    hidden: record.get('administrator') == 1,
                    handler: function () {
                        ExtUtil.Msg.confirm('确定将 ' + record.get('name') + ' 设置为系统管理员吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/user/setAdministrator/" + objectId,
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("设置成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                },
                {
                    text: '取消系统管理员',
                    iconCls: 'icon-user-gray',
                    menuTag: 'CancelAdministrator',
                    hidden: record.get('administrator') != 1,
                    handler: function () {
                        ExtUtil.Msg.confirm('确定取消当前用户的系统管理员资格吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/user/cancelUserAdministrator/" + objectId,
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("设置成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                },
                {
                    text: '归档',
                    iconCls: 'icon-user-delete',
                    menuTag: 'Archive',
                    hidden: record.get('statusTag') == 'Archive',
                    handler: function () {
                        ExtUtil.Msg.confirm('确定将员工 ' + record.get('name') + ' 归档吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/user/archiveUser/" + objectId,
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("归档成功!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                },
                {
                    text: '取消归档',
                    iconCls: 'iconEdit',
                    hidden: record.get('statusTag') != 'Archive',
                    menuTag: 'Active',
                    handler: function () {
                        ExtUtil.Msg.confirm('确定取消当前用户归档吗?', function (select) {
                            if (select == 'no') {
                                return;
                            }
                            Ext.Ajax.request({
                                url: PageContext.contextPath + "/system/user/activeUser/" + objectId,
                                success: function (response) {
                                    var result = Ext.decode(response.responseText);
                                    if (result.success) {
                                        ExtUtil.Msg.info("已经取消归档!", function () {
                                            store.load();
                                        });
                                    } else {
                                        ExtUtil.Msg.error(result.message);
                                    }
                                }
                            });
                        });
                    },
                    scope: this
                }
            );
        }, this);

        this.items = this.gridPanel;

        this.callParent();

        this.queryUser();
    },

    queryUser: function () {
        var name = this.gridPanel.down('#name');
        var organization = this.gridPanel.down('#organization');
        var status = this.gridPanel.down('#status');
        var role = this.gridPanel.down('#role');
        Ext.apply(this.gridPanel.getStore().getProxy().extraParams, {
            organizationId: organization.getObjectValue(),
            name: name.getValue(),
            statusId: status.getValue(),
            roleId: role.getValue(),
            statusTag: this.defaultQueryStatusTag
        });
        this.defaultQueryStatusTag = '';
        this.gridPanel.getStore().loadPage(1);
    }
});