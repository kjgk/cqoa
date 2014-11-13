'use strict';

angular.module('withub.common', [])
    .constant('DateFormat', {
        DAY: 'YYYY-MM-DD',
        HOUR: 'YYYY-MM-DD HH',
        MINUTE: 'YYYY-MM-DD HH:mm',
        SECOND: 'YYYY-MM-DD HH:mm:ss',
        TIMESTAMP: 'YYYY-MM-DD HH:mm:ss.SSS'
    })

    .directive('withubCommon', function ($q, ngDialog, toaster) {
        var dialog_ = {
            alert: function (message) {
                return ngDialog.open({
                    template: 'components/ui/template/dialog-alert.html',
                    controller: function ($scope) {
                        $scope.message = message || '';
                    }
                });
            },
            confirm: function (message) {
                return ngDialog.openConfirm({
                    template: 'components/ui/template/dialog-confirm.html',
                    controller: function ($scope) {
                        $scope.message = message || '';
                    }
                });
            },
            confirmDelete: function () {
                return dialog_.confirm('确认删除？');
            }
        };
        var toaster_ = {
            success: function (message) {
                return toaster.pop('success', "信息", message);
            },
            info: function (message) {
                return toaster.pop('info', "信息", message);
            },
            warning: function (message) {
                return toaster.pop('warning', "警告", message);
            },
            error: function (message) {
                return toaster.pop('error', "错误", message);
            }
        };
        window.Dialog = dialog_;
        window.Toaster = toaster_;
        return {
            link: function (scope, element, attrs) {

            }
        }
    })
;
