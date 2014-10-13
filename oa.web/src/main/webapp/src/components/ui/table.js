'use strict';

angular.module('withub.ui.table', [])

    .constant('paginationConfig', {
        itemsPerPage: 10,
        boundaryLinks: false,
        directionLinks: true,
        firstText: '首页',
        previousText: '上一页',
        nextText: '下一页',
        lastText: '尾页',
        rotate: true
    })

    .directive('simpleTable', function () {
        return {
            link: function (scope, element, attrs) {
                scope.$watch('grid.currentPage', function () {
                    scope.grid.fetchData(scope.grid.currentPage);
                });
            }
        }
    })

    .provider('SimpleTable', function (paginationConfig) {
        this.$get = function () {
            return  function SimpleGrid(fetchFn, options) {
                if (!(this instanceof SimpleGrid)) {
                    return new SimpleGrid(fetchFn, options);
                }

                options = options || {};

                var self = {
                    items: [],
                    params: options.params || {},
                    currentPage: 1,
                    pageSize: options.pageSize || paginationConfig.itemsPerPage,

                    fetchData: function (page) {
                        var grid = this;
                        page = page || grid.currentPage;
                        var params = _.extend(grid.params || {}, {
                            pageNo: page,
                            pageSize: grid.pageSize
                        });
                        fetchFn(params).then(function (response) {
                            var result = response.data;
                            grid.items = result.items;
                            grid.total = result.total;
                        });
                    },
                    refresh: function () {
                        this.fetchData();
                    },
                    query: function (params) {
                        _.extend(this.params, params);
                        this.fetchData(1);
                    }
                };
                _.extend(this, self);
            };
        };
    })
;
