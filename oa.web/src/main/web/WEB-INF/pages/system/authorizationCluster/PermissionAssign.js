Ext.define('withub.ext.system.authorizationCluster.PermissionAssign', {
    extend: 'Ext.Window',
    layout: 'border',
    title: '权限分配',
    width: 640,
    height: 480,
    resizable: false,
    modal: true,
    initComponent: function () {

        this.height = Math.max(this.height, ExtUtil.getFitHeight());

        this.treePanel = Ext.create('withub.ext.base.Tree', {
            region: 'west',
            title: '实体权限',
            split: true,
            width: 280,
            singleExpand: true,
            margins: '0 0 0 0',
            url: '/system/permission/loadManagerTree'
        });

        this.treeRegulationPanel = Ext.create('withub.ext.base.Tree', {
            region: 'center',
            title: '权限规则',
            margins: '0 0 0 0',
            rootVisible: true,
            root: {
                id: 'Root',
                text: '授予',
                checked: false,
                expanded: true
            },
            url: '/system/authorizationCluster/loadPermissionRegulationTree'
        });

        this.items = [this.treePanel, this.treeRegulationPanel];

        this.treeRegulationPanel.on('checkchange', function (node, checked) {
            if (checked) {
                this.checkParent(node);
            } else {
                this.uncheckChild(node);
            }
        }, this);

        var me = this;
        this.treeRegulationPanel.on('load', function () {
            Ext.Ajax.request({
                url: PageContext.contextPath + '/system/authorizationCluster/getAssignedPermission',
                params: {
                    authorizationClusterId: this.authorizationClusterId,
                    permissionId: this.treeRegulationPanel.permissionId
                },
                success: function (response) {
                    var result = Ext.decode(response.responseText);
                    var store = me.treeRegulationPanel.getStore();
                    if (result['assigned']) {
                        store.getRootNode().set("checked", true);
                    }
                    Ext.each(result['permissionRegualtionIdList'], function (id) {
                        var node = store.getNodeById('PermissionRegulation_' + id);
                        node.set("checked", true);
                    });
                },
                scope: this
            }, this);
        }, this);

        this.treePanel.on('select', function (tree, record, index) {
            if (record.get('type') != 'Permission') {
                this.treeRegulationPanel.permissionId = "";
                return;
            }
            this.treeRegulationPanel.permissionId = record.get("objectId");
            this.treeRegulationPanel.setRootNode({
                id: 'Permission_' + record.get("objectId"),
                text: '授予',
                checked: false,
                expanded: true
            });

        }, this);

        this.buttons = [
            {
                text: '保存',
                handler: function () {
                    if (Ext.isEmpty(this.treeRegulationPanel.permissionId)) {
                        ExtUtil.Msg.info('请选择一个权限!');
                        return;
                    }
                    var rootNode = this.treeRegulationPanel.getStore().getRootNode();
                    if (rootNode.get("checked")) {
                        this.assignPermission();
                    } else {
                        this.cancelPermission();
                    }
                },
                scope: this
            },
            {
                text: '关闭',
                handler: this.close,
                scope: this
            }
        ];

        this.callParent();
    },

    assignPermission: function () {
        var rootNode = this.treeRegulationPanel.getStore().getRootNode();
        var params = {};
        params['authorizationCluster.objectId'] = this.authorizationClusterId;
        params['permission.objectId'] = this.treeRegulationPanel.permissionId;

        if (rootNode.hasChildNodes()) {
            var checkedNodeArray = new Array();
            Ext.each(this.treeRegulationPanel.getChecked(), function (node) {
                if (node.get("id") != rootNode.get("id")) {
                    checkedNodeArray.push({nodeId: node.get("id"), objectId: ExtUtil.getUuid()});
                }
            });
            var index = 0;
            Ext.each(this.treeRegulationPanel.getChecked(), function (node) {
                if (node.get("id") == rootNode.get("id")) {
                    return true;
                }
                var parentId = '';
                var objectId = '';
                Ext.each(checkedNodeArray, function (o) {
                    if (node.get("parentId") == o.nodeId) {
                        parentId = o.objectId;
                    }
                    if (node.get("id") == o.nodeId) {
                        objectId = o.objectId;
                    }
                    if (objectId && parentId) {
                        return false;
                    }
                }, this);

                params['authorizationRegulationList[' + index + '].objectId'] = objectId;
                if (!Ext.isEmpty(parentId)) {
                    params['authorizationRegulationList[' + index + '].parent.objectId'] = parentId;
                }
                params['authorizationRegulationList[' + index + '].permissionRegulation.objectId'] = node.get("id").split('_')[1];
                index++;
            });
        }

        var mask = new Ext.LoadMask(this.id, {msg: '正在分配权限，请稍后。。。'});
        mask.show();
        Ext.Ajax.request({
            url: PageContext.contextPath + '/system/authorizationCluster/assignPermission',
            params: params,
            success: function (response) {
                mask.hide();
                var result = Ext.decode(response.responseText);
                if (result['success']) {
                    ExtUtil.Msg.info('权限分配成功！');
                } else {
                    ExtUtil.Msg.error(result.message);
                }
            },
            scope: this
        }, this);
    },

    cancelPermission: function () {

        var permissionId = this.treeRegulationPanel.permissionId;
        var mask = new Ext.LoadMask(Ext.getBody(), {msg: '正在取消权限...'});
        mask.show();
        Ext.Ajax.request({
            url: PageContext.contextPath + "/system/authorizationCluster/cancelPermission",
            params: {
                authorizationClusterId: this.authorizationClusterId,
                permissionId: permissionId
            },
            success: function (response) {
                mask.hide();
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    ExtUtil.Msg.info('权限已经取消!', function () {
                    });
                } else {
                    ExtUtil.Msg.info(result.message);
                }
            },
            failure: function (response) {
                mask.hide();
                ExtUtil.Msg.info(response.responseText);
            }
        }, this);
    },

    uncheckChild: function (node) {
        node.eachChild(function (childNode) {
            childNode.set("checked", false);
            this.uncheckChild(childNode);
        }, this);
    },

    checkParent: function (node) {
        var parentNode = this.treeRegulationPanel.getStore().getNodeById(node.get('parentId'));
        if (parentNode) {
            parentNode.set("checked", true);
            this.checkParent(parentNode);
        }
    }
});