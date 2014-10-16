'use strict';

angular.module('workflow', ['ui.router'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {
                url: '/{id}',
                templateUrl: 'diagram.html',
                controller: 'WorkflowCtrl'
            })
        ;
    })

    .value('emptyDiagram', {"cells": [
        {"angle": 0, "attrs": {"circle": {"fill": "#5cb85c"}, "text": {"text": "开始"}}, "embeds": "", "id": "b5f3e9d2-2076-4bdb-9b7b-6d843e83c39d", "nodeType": "begin", "position": {"x": 35, "y": 40}, "size": {"height": 50, "width": 80}, "type": "basic.Circle", "z": 1},
        {"Executer": "#{starter}", "FlowNodeTag": "", "HandlerOnFlowNode": "", "FlowNodeType": "first", "SuspendDescription": "", "UserPropertyOnEntity": "", "angle": 0, "attrs": {"rect": {"fill": "#2798EC"}, "text": {"text": "第一个节点"}}, "embeds": "", "id": "68da8b30-4c3e-4bbd-8746-334b488a3eb0", "nodeType": "first", "position": {"x": 205, "y": 40}, "size": {"height": 50, "width": 80}, "type": "basic.Rect", "z": 2},
        {"angle": 0, "attrs": {"circle": {"fill": "#d9534f"}, "text": {"text": "结束"}}, "embeds": "", "id": "be0634ec-5fd8-4d07-8c22-97befd256888", "nodeType": "end", "position": {"x": 35, "y": 240}, "size": {"height": 50, "width": 80}, "type": "basic.Circle", "z": 3},
        {"RamusType": "1", "attrs": {}, "embeds": "", "id": "b6121b94-05f4-4353-bb95-bf95a9aadd47", "source": {"id": "b5f3e9d2-2076-4bdb-9b7b-6d843e83c39d"}, "target": {"id": "68da8b30-4c3e-4bbd-8746-334b488a3eb0"}, "type": "link", "z": 4}
    ]})

    .controller('WorkflowCtrl', function ($scope, $http, $state, emptyDiagram) {

        var graph = new joint.dia.Graph;

        var paper = new joint.dia.Paper({
            el: '#paper-container',
            width: 640,
            height: 476,
            gridSize: 5,
            model: graph
        });

        var stencil = new joint.ui.Stencil({
            graph: graph,
            paper: paper,
            width: 120,
            height: 476
        });
        $('#stencil-container').append(stencil.render().el);

        var beginNode = new joint.shapes.basic.Circle({
            nodeType: 'begin',
            position: { x: 20, y: 10 }, size: { width: 80, height: 50 },
            attrs: { circle: { fill: '#5cb85c' }, text: { text: '开始', fill: 'black' } }
        });
        var endNode = new joint.shapes.basic.Circle({
            nodeType: 'end',
            position: { x: 20, y: 150 }, size: { width: 80, height: 50 },
            attrs: { circle: { fill: '#d9534f' }, text: { text: '结束', fill: 'black' } }
        });
        var firstNode = new joint.shapes.basic.Rect({
            nodeType: 'first',
            position: { x: 20, y: 80 }, size: { width: 80, height: 50 },
            attrs: { rect: { fill: '#2798EC' }, text: { text: '第一个节点', fill: 'black' } }
        });
        var normalNode = new joint.shapes.basic.Rect({
            nodeType: 'normal',
            position: { x: 20, y: 20 }, size: { width: 80, height: 50 },
            attrs: { rect: { fill: '#2798EC' }, text: { text: '节点', fill: 'black' } }
        });

        var snaplines = new joint.ui.Snaplines({ paper: paper });
        snaplines.startListening();

        var inspector;

        function createInspector(cellView) {

            var type = cellView.model.attributes.nodeType || cellView.model.attributes.type;

            // No need to re-render inspector if the cellView didn't change.
            if (!inspector || inspector.options.cellView !== cellView) {

                if (inspector) {
                    // Apply all unsaved changes on the cell before we remove the old inspector.
                    inspector.updateCell();
                    // Clean up the old inspector if there was one.
                    inspector.remove();
                }

                var inputs = {
                    attrs: {
                        text: {
                            text: { type: 'text', group: 'base', label: '名称', index: 1 }
                        }
                    }
                };
                if (type == 'link') {
                    _.extend(inputs, {
                        RamusTag: { type: 'text', group: 'base', label: '分支标识', index: 2 },
                        RamusType: { type: 'select', group: 'base', label: '分支类型', index: 3, options: [
                            {value: '1', content: '条件' },
                            {value: '2', content: '审批' }
                        ]},
                        EntityStatusTag: { type: 'text', group: 'base', label: '状态标识', index: 4 },
                        Cond: { type: 'textarea', group: 'base', label: '条件', index: 5 },
                        Event: { type: 'text', group: 'base', label: '事件', index: 6 },
                        Desc: { type: 'textarea', group: 'base', label: '说明', index: 7 }
                    });
                } else if (type == 'first' || type == 'normal') {
                    _.extend(inputs, {
                        FlowNodeTag: { type: 'text', group: 'base', label: '标识', index: 2 },
                        Config: { type: 'object', group: 'base', label: ' ', index: 3,
                            properties: {
                                ManualSelectHandler: { type: 'toggle', label: '手动选择下一个节点处理人'},
                                SkipHandler: { type: 'toggle', label: '跳过上次执行通过的处理人' },
                                AllowAgent: { type: 'toggle', label: '允许代理' },
                                NotifyInstanceCreator: { type: 'toggle', label: '通知流程的发起人' },
                                InstanceReturnMode: { type: 'toggle', label: '并行任务处理完后再退回' },
                                SuspendInstance: { type: 'toggle', label: '挂起流程' }
                            } },
                        SuspendDescription: { type: 'text', group: 'base', label: '流程挂起描述', index: 4 },
                        Executer: { type: 'text', group: 'base', label: '取人方法', index: 5 },
                        HandlerOnFlowNode: { type: 'text', group: 'base', label: '前面流程节点上的执行人', index: 6 },
                        UserPropertyOnEntity: { type: 'text', group: 'base', label: '实体上的用户属性', index: 7 }
                    });

                    if (type == 'first') {
                        _.extend(inputs, {
                            FlowNodeType: { type: 'select', group: 'base', defaultValue: 'first', label: '节点类型', index: 3, options: [
                                {value: 'first', content: '第一个节点' }
                            ] }
                        });
                    }

                    if (type == 'normal') {
                        _.extend(inputs, {
                            FlowNodeType: { type: 'select', defaultValue: 'andsign', group: 'base', label: '节点类型', index: 3, options: [
                                {value: 'andsign', content: '会签' },
                                {value: 'modify', content: '修改' },
                                {value: 'submit', content: '提交' },
                                {value: 'finish', content: '完成' }
                            ]},
                            OrganizationId: { type: 'text', group: 'base', label: '组织机构ID', index: 8 },
                            RoleId: { type: 'text', group: 'base', label: '角色ID', index: 9 },
                            UseRootNode: { type: 'text', group: 'base', label: '使用根节点', index: 10 },
                            OrganizationProperty: { type: 'text', group: 'base', label: '组织机构属性', index: 11 },
                            RoleProperty: { type: 'text', group: 'base', label: '角色属性', index: 12 },
                            HandlerFetchCount: { type: 'text', group: 'base', label: '取人数', defaultValue: 1, index: 13 },
                            HandlerFetchType: { type: 'object', group: 'base', label: '取人方式', index: 14,
                                properties: {
                                    Random: { type: 'toggle', label: '随机' },
                                    IdleMost: { type: 'toggle', label: '最空闲' },
                                    TaskLeast: { type: 'toggle', label: '同类任务最少' }
                                } },
                            TimeLimit: { type: 'text', group: 'base', label: '处理时限（工作小时）', defaultValue: 0, index: 15 },
                            FlowNodeAction: { type: 'object', group: 'base', label: '行为动作', index: 16,
                                properties: {
                                    passAction: { type: 'toggle', label: '通过' },
                                    returnAction: { type: 'toggle', label: '退回' },
                                    rejectAction: { type: 'toggle', label: '否决' },
                                    completeAction: { type: 'toggle', label: '完成' },
                                    discardAction: { type: 'toggle', label: '弃权' }
                                }},
                            WarnType: { type: 'object', group: 'base', label: '提醒方式', index: 17,
                                properties: {
                                    note: { type: 'toggle', label: '内部消息' },
                                    sms: { type: 'toggle', label: '手机短信' },
                                    email: { type: 'toggle', label: '邮件' }
                                }},
                            EntityStatusTag: { type: 'text', group: 'base', label: '表单状态', index: 18 },
                            Activity: { type: 'text', group: 'base', label: '处理界面', index: 19 },
                            NotifyContent: { type: 'textarea', group: 'base', label: '通知内容模板', index: 20 }
                        });
                    }
                }

                inspector = new joint.ui.Inspector({
                    inputs: inputs,
                    groups: {
                        base: { label: '属性', index: 1 }
                    },
                    cellView: cellView
                });
                $('#inspector-container').html(inspector.render().el);
            }
        }

        paper.on('cell:pointerup', function (cellView) {
            // We don't want a Halo for links.
            if (cellView.model instanceof joint.dia.Link) return;

            var halo = new joint.ui.Halo({ graph: graph, paper: paper, cellView: cellView });
            halo.render();

            createInspector(cellView);
        });

        paper.on('link:options', function (event, cellView) {

            createInspector(cellView);
        });

//        stencil.load([beginNode, firstNode, normalNode, endNode]);
        stencil.load([normalNode]);

        $scope.save = function () {
            $http({
                url: '/oa/workflow/flowType/' + $state.params.id + '/saveWorkflowConfig',
                method: 'POST',
                data: {
                    content: graph.toJSON()
                }
            }).then(function () {
                alert('保存成功!');
            });
        }

        $http({
            url: '/oa/workflow/flowType/' + $state.params.id + '/loadWorkflowConfig',
            method: 'GET'
        }).then(function (response) {
            if (response.data.data) {
                graph.fromJSON(angular.fromJson(response.data.data));
            } else {
                graph.fromJSON(emptyDiagram);
            }
        })

    })
;
