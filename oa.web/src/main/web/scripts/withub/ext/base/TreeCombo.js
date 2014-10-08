Ext.define('withub.ext.base.TreeCombo', {
    extend: 'Ext.form.field.Picker',
    requires: ['Ext.tree.Panel'],
    alias: 'widget.treecombo',
    rootId: 'Root',
    rootText: 'Root',
    nodeParam: 'node',
    editable: false,
    enableClear: false,
    enableSearch: false,
    selectType: undefined,
    treeWidth: undefined,
    showPathName: false,
    pathNameDepth: 0,
    queryDefer: 200,
    trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',
    trigger2Cls: Ext.baseCSSPrefix + 'form-trigger',

    initComponent: function () {
        var me = this;
        me.enableKeyEvents = me.enableSearch;
        me.editable = me.enableSearch;
        me.callParent();
        if (me.selectType) {
            me.on('beforeselect', function (nodeId, objectId, nodeType, record, index) {
                var selectType = [];
                if (Ext.isArray(me.selectType)) {
                    selectType = me.selectType;
                } else {
                    selectType = [me.selectType];
                }
                return Ext.Array.contains(selectType, nodeType);
            });
        }
        me.createTreePicker();
    },

    onKeyUp: function (e) {
        var me = this;
        Ext.defer(function () {
            var rawValue = me.getRawValue();
            var picker = me.picker;
            var clearTriggerCell = me.triggerCell.item(0);
            if (rawValue) {
                if (!me.listPicker) {
                    me.createListPicker();
                }
                me.picker = me.listPicker;
                if (!clearTriggerCell.isVisible()) {
                    clearTriggerCell.setDisplayed(true);
                    me.updateLayout();
                }
                me.queryHander(rawValue);
            } else {
                me.picker = me.treePicker;
                if (clearTriggerCell.isVisible()) {
                    clearTriggerCell.setDisplayed(false);
                    me.updateLayout();
                }
            }

            if (picker != me.picker) {
                me.collapse();
            }
            if (!me.isExpanded) {
                me.expand();
                me.focus();
            }
        }, me.queryDefer);
    },

    afterRender: function () {
        this.callParent();
        this.triggerCell.item(0).setDisplayed(false);
    },

    onTrigger1Click: function () {
        this.clearValue();
        this.triggerCell.item(0).setDisplayed(false);
        this.updateLayout();
    },

    createListPicker: function () {
        var me = this;
        var listPanel = Ext.create('Ext.Panel', {
            height: 300,
            width: me.treeWidth ? me.treeWidth : undefined,
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: Ext.getBody(),
            floating: true,
            hidden: true,
            resizable: true,
            layout: 'fit',
            items: [
                Ext.create('Ext.view.View', {
                    autoScroll: true,
                    store: me.searchStore || Ext.create('withub.ext.base.Store', {
                        url: me.searchUrl,
                        method: 'GET',
                        fields: ['objectId', 'name', 'description']
                    }),
                    itemSelector: 'li.list-item',
                    overItemCls: 'list-item-over',
                    tpl: me.searchTemplate || new Ext.XTemplate(
                        '<ul class="search-result">',
                        '<tpl for=".">',
                        '<li class="list-item {[xindex % 2 === 0 ? "even" : "odd"]}">',
                        '<div class="name">{name}</div><div class="desc">{description}</div>',
                        '</li>',
                        '</tpl>',
                        '</ul>'
                    ),
                    listeners: {
                        select: function (view, record) {
                            var objectId = record.get('objectId');
                            var name = record.get('name');
                            me.setRawValue(name);
                            me.objectValue = objectId;
                            me.value = objectId;
                            me.fireEvent('select', null, objectId, '', record, null);
                            me.collapse();
                        }
                    }
                })
            ]
        });

        me.picker = listPanel;
        me.listPicker = listPanel;
        return listPanel;
    },

    createTreePicker: function () {
        var me = this;
        var store = Ext.create('Ext.data.TreeStore', {
            fields: ['objectId', 'text', 'type', 'attributes'],
            proxy: {
                type: 'ajax',
                url: PageContext.contextPath + this.url,
                extraParams: this.params,
                reader: {
                    type: 'json',
                    root: 'nodes'
                }
            },
            root: {
                id: this.rootId,
                text: this.rootText
            },
            nodeParam: this.nodeParam,
            listeners: {
                load: function () {
                    if (me.value) {
                        setTimeout(function () {
                            me.setValue(me.value);
                        }, 100)
                    }
                }
            }
        });

        var treePanel = Ext.create('Ext.tree.Panel', {
            height: 300,
            width: me.treeWidth ? me.treeWidth : undefined,
            autoScroll: true,
            pickerField: me,
            ownerCt: me.ownerCt,
            renderTo: Ext.getBody(),
            floating: true,
            hidden: true,
            shadow: true,
            useArrows: false,
            store: store,
            rootVisible: false,
            resizable: true
        });

        store.on('load', function (store, records, successful, options) {
            if (me.isLoadingWaitNodes) {
                me.doLoadWaitNode();
            }
        });

        treePanel.on('itemclick', function (view, record, item, index, event, options) {
            var nodeType = record.get('type');
            var nodeId = record.get('id');
            var objectId = record.get('objectId');
            if (me.fireEvent('beforeselect', nodeId, objectId, nodeType, record, index) === false) {
                return;
            }
            me.setValue(nodeId);
            me.fireEvent('select', nodeId, objectId, nodeType, record, index);
            me.collapse();
        });

        me.picker = treePanel;
        me.treePicker = treePanel;
        return treePanel;
    },

    getValue: function () {
        return this.value;
    },

    getSubmitValue: function () {
        return this.objectValue || this.getValue();
    },

    getObjectValue: function () {
        return this.objectValue;
    },

    getObjectType: function () {
        var view = this.getPicker().getView();
        if (view.getSelectedNodes().length > 0) {
            return view.getRecord(view.getSelectedNodes()[0]).get('type');
        }
        return undefined;
    },

    clearValue: function () {
        var me = this;
        me.setValue(undefined);
        me.objectValue = undefined;
        if (me.picker == me.treePicker) {
            var view = me.picker.getView();
            view.deselect(view.getRecord(view.getSelectedNodes()[0]), false);
        } else {
            me.picker = me.treePicker;
            me.collapse();
            me.expand();
        }
    },

    setValue: function (value) {
        var me = this
        me.value = value;
        if (Ext.isEmpty(value) || !me.rendered) {
            me.callParent(arguments);
            return me;
        }

        var picker = me.getPicker();
        if (picker == me.listPicker) {
            me.callParent(arguments);
            return me;
        }

        var treePanel = picker, store = treePanel.getStore();
        if (treePanel.getStore().loading) {
            return me;
        }

        var node = store.getNodeById(value);
        if (node) {
            me.callParent(arguments);
            me.objectValue = node.get('objectId');
            var text = node.get('text');
            if (me.showPathName && node.getDepth() >= me.pathNameDepth) {
                var _n = node;
                text = '';
                while (_n.getDepth() >= me.pathNameDepth) {
                    text = _n.get('text') + ' ' + text;
                    _n = _n.parentNode;
                }
            }
            me.setRawValue(text);
            treePanel.getView().select(node, false, false);

            if (me.enableClear) {
                me.triggerCell.item(0).setDisplayed(true);
                me.updateLayout();
            }
        } else {
            me.waitNodes = [];
            Ext.Ajax.request({
                url: PageContext.contextPath + me.url + 'Path?' + me.nodeParam + '=' + value,
                method: 'GET',
                success: function (response) {
                    var result = Ext.decode(response.responseText);
                    Ext.each(result['node'].reverse(), function (nodeId) {
                        me.waitNodes.push(nodeId);
                    });
                    me.doLoadWaitNode();
                }
            });
        }
        return me;
    },

    doLoadWaitNode: function () {
        if (Ext.isDefined(this.waitNodes) && this.waitNodes.length > 0) {
            var nodeId = this.waitNodes.shift();
            var node = this.getPicker().getStore().getNodeById(nodeId);
            if (node) {
                if (node.isLoaded()) {
                    node.expand(false, this.doLoadWaitNode, this);
                } else {
                    this.isLoadingWaitNodes = true;
                    node.expand();
                }
            }
        } else {
            this.isLoadingWaitNodes = false;
            this.setValue(this.value);
        }
    },

    onCollapse: function () {
        var me = this;
        if (me.treePicker.isVisible()) {
            me.treePicker.hide();
        }
        if (me.listPicker && me.listPicker.isVisible()) {
            me.listPicker.hide();
        }
    },

    alignPicker: function () {
        var me = this,
            picker = me.getPicker();

        if (me.isExpanded) {
            if (me.matchFieldWidth && !me.treeWidth) {
                picker.setWidth(me.bodyEl.getWidth());
            }
            if (picker.isFloating()) {
                me.doAlign();
            }
        }
    },

    // private
    queryHander: function (keyword) {
        var me = this;
        var store = me.listPicker.getComponent(0).getStore();
        Ext.apply(store.getProxy().extraParams, {
            keyword: Ext.String.trim(keyword)
        });
        store.load();
    }
});