'use strict';

angular.module('withub.ui', [])

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

                scope.$watch('grid.loading', function () {
                    if (scope.grid.loading === true) {
                        element.find('tr:last').not('.loading-text').after('<tr class="loading-text"><td colspan="100">正在加载数据...</td></tr>');
                        element.find('tr.empty-text').remove();
                    } else {
                        element.find('tr.loading-text').remove();
                        if (_.isEmpty(scope.grid.items)) {
                            element.find('tr:last').not('.empty-text').after('<tr class="empty-text"><td colspan="100">没有数据！</td></tr>');
                        } else {
                            element.find('tr.empty-text').remove();
                        }
                    }
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

                var me = this;
                options = options || {};

                var self = {
                    items: [],
                    queryInfo: {},
                    params: options.params || {},
                    currentPage: 1,
                    loading: false,
                    pageSize: options.pageSize || paginationConfig.itemsPerPage,

                    fetchData: function (page) {
                        page = page || me.currentPage;
                        var params = _.extend(me.params || {}, {
                            pageNo: page,
                            pageSize: me.pageSize
                        });
                        me.items = [];
                        me.total = 0;
                        me.loading = true;
                        fetchFn(params).then(function (response) {
                            var result = response.data;
                            me.items = result.items;
                            me.total = result.total;
                        }).finally(function () {
                            me.loading = false;
                        });
                    },
                    refresh: function () {
                        this.fetchData();
                    },
                    query: function (params) {
                        var queryInfo = {};
                        for (var key in me.queryInfo) {
                            var value = me.queryInfo[key], _value;
                            if (_.isDate(value)) {
                                _value = moment(value).format('YYYY-MM-DD');
                            } else {
                                _value = value;
                            }
                            queryInfo[key] = _value;
                        }
                        _.extend(this.params, _.extend(queryInfo, params || {}));
                        this.fetchData(1);
                    }
                };
                _.extend(this, self);
            };
        };
    })
;
