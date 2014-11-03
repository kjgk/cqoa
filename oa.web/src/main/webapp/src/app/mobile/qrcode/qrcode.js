angular.module('app.mobile')

    .config(function ($stateProvider) {
        $stateProvider
            .state('mobile.qrcode', {
                url: '/qrcode',
                templateUrl: 'app/mobile/qrcode/qrcode-list.html',
                controller: function () {

                }
            })
        ;
    })

;