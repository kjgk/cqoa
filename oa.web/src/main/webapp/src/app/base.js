'use strict';

angular.module('base', ['restangular', 'ui.bootstrap', 'ui.router'
    , 'validation', 'validation.rule', 'angular-loading-bar', 'toaster'
])
    .value('PageContext', PageContext)

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

    .filter('$date', function ($filter) {
        return function (input, format) {
            if (input) {
                format = format || 'yyyy-MM-dd HH:mm';
                if (_.isNumber(input)) {
                    return $filter('date')(input, format);
                } else {
                    return $filter('date')(input.time, format);
                }
            }
        };
    })
;

angular.module('validation.rule', ['validation'])
    .config(function ($validationProvider) {

        var expression = {
            required: function (value) {
                return !!value;
            },
            url: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/,
            email: /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/,
            number: /^\d+$/
        };

        var defaultMsg = {
            required: {
                error: '必填项',
                success: '<i class="fa fa-check"></i>'
            },
            url: {
                error: 'Url格式不正确',
                success: '<i class="fa fa-check"></i>'
            },
            email: {
                error: 'Email格式不正确',
                success: '<i class="fa fa-check"></i>'
            },
            number: {
                error: '请输入数字',
                success: '<i class="fa fa-check"></i>'
            }
        };

        $validationProvider.setExpression(expression).setDefaultMsg(defaultMsg);

        $validationProvider.showSuccessMessage = false;

        $validationProvider.setErrorHTML(function (msg) {
            return '<p class="validation validation-invalid">' + msg + '</p>';
        });

        $validationProvider.setSuccessHTML(function (msg) {
            return '<p class="validation validation-valid">' + msg + '</p>';
        });

    })
;