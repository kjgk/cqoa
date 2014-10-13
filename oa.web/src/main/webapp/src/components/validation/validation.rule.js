'use strict';

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