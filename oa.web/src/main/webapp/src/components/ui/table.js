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

                var me = this;
                options = options || {};

                var self = {
                    items: [],
                    queryInfo: {},
                    params: options.params || {},
                    currentPage: 1,
                    pageSize: options.pageSize || paginationConfig.itemsPerPage,

                    fetchData: function (page) {
                        page = page || me.currentPage;
                        var params = _.extend(me.params || {}, {
                            pageNo: page,
                            pageSize: me.pageSize
                        });
                        fetchFn(params).then(function (response) {
                            var result = response.data;
                            me.items = result.items;
                            me.total = result.total;
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
