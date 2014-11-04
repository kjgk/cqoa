angular.module('app.mobile')

    .config(function ($stateProvider) {
        $stateProvider
            .state('mobile.qrcodescan', {
                url: '/qrcodescan',
                templateUrl: 'app/mobile/qrcodescan/qrcodescan-index.html',
                controller: function ($scope, $http) {

                    $http({
                        url: PageContext.path + '/std/appVersion/current',
                        method: 'GET'
                    }).success(function(response){
                        $scope.fileList = response.data.fileList;
                    })
                }
            })
        ;
    })

;