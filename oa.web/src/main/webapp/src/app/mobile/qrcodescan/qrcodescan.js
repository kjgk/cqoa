angular.module('app.mobile')

    .config(function ($stateProvider) {
        $stateProvider
            .state('mobile.qrcodescan', {
                url: '/qrcodescan',
                templateUrl: 'app/mobile/qrcodescan/qrcodescan-list.html',
                controller: function(){

                }
            })
        ;
    })

;