'use strict';

angular.module('app.workflow', [])

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.workflow', {
                url: '/workflow',
                template: '<div ui-view></div>'
            })
            .state('oa.workflow.diagram', {
                url: '/diagram',
                templateUrl: 'app/workflow/diagram.html',
                controller: 'WorkflowDiagramCtrl'
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

    .controller('WorkflowDiagramCtrl', function ($scope, emptyDiagram) {

        var graph = new joint.dia.Graph;

        var paper = new joint.dia.Paper({
            el: '#paper-holder-loading',
            width: 600,
            height: 436,
            gridSize: 5,
            model: graph
        });

        var stencil = new joint.ui.Stencil({
            graph: graph,
            paper: paper,
            width: 200,
            height: 476
        });
        $('#stencil-holder-loading').append(stencil.render().el);

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

                if (type == 'link') {
                    inspector = new joint.ui.Inspector({
                        inputs: {
                            attrs: {
                                text: {
                                    text: { type: 'text', group: 'base', label: '名称', index: 1 }
                                }
                            },
                            branchTag: { type: 'text', group: 'base', label: '分支标识', index: 2 },
                            statusTag: { type: 'text', group: 'base', label: '条件标识', index: 3 },
                            condition: { type: 'textarea', group: 'base', label: '条件', index: 4 },
                            event: { type: 'text', group: 'base', label: '事件', index: 5 },
                            description: { type: 'textarea', group: 'base', label: '说明', index: 6 }
                        },
                        groups: {
                            base: { label: '属性', index: 1 }
                        },
                        cellView: cellView
                    });
                } else if (type == 'start' || type == 'end') {
                    inspector = new joint.ui.Inspector({
                        inputs: {
                            attrs: {
                                text: {
                                    text: { type: 'text', group: 'base', label: '名称', index: 1 }
                                }
                            }
                        },
                        groups: {
                            base: { label: '属性', index: 1 }
                        },
                        cellView: cellView
                    });
                }


                $('#inspector-holder-create').html(inspector.render().el);
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

//        graph.fromJSON(emptyDiagram);

        $scope.fn = function () {
            $scope.graph = graph;
        }

    })
;
