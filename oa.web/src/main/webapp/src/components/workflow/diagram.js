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
        {
            "type": "basic.Circle",
            "size": {
                "width": 80,
                "height": 50
            },
            "position": {
                "x": 45,
                "y": 35
            },
            "angle": 0,
            "id": "69446d13-0194-4ae7-92ae-77b17fd161a6",
            "embeds": "",
            "z": 1,
            "base": 5,
            "attrs": {
                "circle": {
                    "fill": "#5cb85c"
                },
                "text": {
                    "text": "开始"
                }
            }
        },
        {
            "type": "basic.Rect",
            "position": {
                "x": 220,
                "y": 35
            },
            "size": {
                "width": 80,
                "height": 50
            },
            "angle": 0,
            "id": "f8df118a-9905-402b-be2e-84aba553f738",
            "embeds": "",
            "z": 2,
            "base": 5,
            "attrs": {
                "rect": {
                    "fill": "#2798EC"
                },
                "text": {
                    "text": "第一个节点"
                }
            }
        },
        {
            "type": "basic.Circle",
            "size": {
                "width": 80,
                "height": 50
            },
            "position": {
                "x": 45,
                "y": 330
            },
            "angle": 0,
            "id": "22a95b01-71eb-4068-81db-7136f543bc78",
            "embeds": "",
            "z": 3,
            "base": 5,
            "attrs": {
                "circle": {
                    "fill": "#d9534f"
                },
                "text": {
                    "text": "结束"
                }
            }
        },
        {
            "type": "link",
            "id": "0c7fba7f-888c-4824-8795-fbb1b9c9ce99",
            "embeds": "",
            "source": {
                "id": "69446d13-0194-4ae7-92ae-77b17fd161a6"
            },
            "target": {
                "id": "f8df118a-9905-402b-be2e-84aba553f738"
            },
            "z": 4,
            "attrs": {}
        }
    ]})

    .controller('WorkflowCtrl', function ($scope, $http, $state, emptyDiagram) {

        var graph = new joint.dia.Graph;

        var paper = new joint.dia.Paper({
            el: '#paper-container',
            width: 640,
            height: 436,
            gridSize: 5,
            model: graph
        });

        var stencil = new joint.ui.Stencil({
            graph: graph,
            paper: paper,
            width: 180,
            height: 476
        });
        $('#stencil-container').append(stencil.render().el);

        var startNode = new joint.shapes.basic.Circle({
            nodeType: 'start',
            position: { x: 60, y: 10 }, size: { width: 80, height: 50 },
            attrs: { circle: { fill: '#5cb85c' }, text: { text: '开始', fill: 'black' } }
        });
        var endNode = new joint.shapes.basic.Circle({
            nodeType: 'end',
            position: { x: 60, y: 150 }, size: { width: 80, height: 50 },
            attrs: { circle: { fill: '#d9534f' }, text: { text: '结束', fill: 'black' } }
        });
        var firstNode = new joint.shapes.basic.Rect({
            nodeType: 'first',
            position: { x: 60, y: 80 }, size: { width: 80, height: 50 },
            attrs: { rect: { fill: '#2798EC' }, text: { text: '第一个节点', fill: 'black' } }
        });
        var normalNode = new joint.shapes.basic.Rect({
            nodeType: 'normal',
            position: { x: 60, y: 280 }, size: { width: 80, height: 50 },
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
                        branchTag: { type: 'text', group: 'base', label: '分支标识', index: 2 },
                        statusTag: { type: 'text', group: 'base', label: '状态标识', index: 3 },
                        condition: { type: 'textarea', group: 'base', label: '条件', index: 4 },
                        event: { type: 'text', group: 'base', label: '事件', index: 5 },
                        description: { type: 'textarea', group: 'base', label: '说明', index: 6 }
                    });
                } else if (type == 'first' || type == 'normal') {
                    _.extend(inputs, {
                        nodeTag: { type: 'text', group: 'base', label: '标识', index: 2 },
                        ProcType: { type: 'text', group: 'base', label: '节点类型', index: 3 },
                        SuspendDescription: { type: 'text', group: 'base', label: '流程挂起描述', index: 4 },
                        Executer: { type: 'text', group: 'base', label: '取人方法', index: 5 },
                        HandlerOnFlowNode: { type: 'text', group: 'base', label: '前面流程节点上的执行人', index: 6 },
                        UserPropertyOnEntity: { type: 'text', group: 'base', label: '实体上的用户属性', index: 7 },
                        OrganizationId: { type: 'text', group: 'base', label: '组织机构ID', index: 8 },
                        RoleId: { type: 'text', group: 'base', label: '角色ID', index: 9 },
                        UseRootNode: { type: 'text', group: 'base', label: '使用根节点', index: 10 },
                        OrganizationProperty: { type: 'text', group: 'base', label: '组织机构属性', index:11 },
                        RoleProperty: { type: 'text', group: 'base', label: '角色属性', index: 12 },
                        HandlerFetchCount: { type: 'text', group: 'base', label: '取人数', index: 13 },
                        HandlerFetchType: { type: 'text', group: 'base', label: '取人方式', index: 14 },
                        TimeLimit: { type: 'text', group: 'base', label: '处理时限', index: 15 },
                        FlowNodeAction: { type: 'text', group: 'base', label: '行为动作', index: 16 },
                        WarnType: { type: 'text', group: 'base', label: '提醒方式', index: 17 },
                        EntityStatusTag: { type: 'text', group: 'base', label: '表单状态', index: 18 },
                        Activity: { type: 'text', group: 'base', label: '处理界面', index: 19 },
                        NotifyContent: { type: 'text', group: 'base', label: '通知内容模板', index: 20 }
                    });
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

        stencil.load([startNode, firstNode, normalNode, endNode]);

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
