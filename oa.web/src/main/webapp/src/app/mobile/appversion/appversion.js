angular.module('app.mobile')

    .config(function ($stateProvider) {
        $stateProvider
            .state('mobile.appversion', {
                url: '/appversion',
                templateUrl: 'app/mobile/appversion/appversion-list.html',
                controller: 'AppVersionCtrl'
            })
        ;
    })

    .factory('AppVersionService', function ($http, Restangular) {
        var api = Restangular.all('std/appVersion');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/std/appVersion',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (appVersion) {
                return api.doPOST(appVersion);
            },
            update: function (appVersion) {
                return api.doPUT(appVersion);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            },
            enable: function (objectId) {
                return Restangular.one('std/appVersion/enable', objectId).post();
            },
            disable: function (objectId) {
                return Restangular.one('std/appVersion/disable', objectId).post();
            }
        }
    })

    .controller('AppVersionCtrl', function ($scope, $q, $modal, SimpleTable, AppVersionService) {

        $scope.grid = SimpleTable(AppVersionService.query);

        $scope.createAppVersion = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/mobile/appVersion/appVersion-form.html',
                controller: 'AppVersionCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                Toaster.success("保存成功！");
            });
        };
        $scope.deleteAppVersion = function (appVersion) {
            AppVersionService.remove(appVersion.objectId).then(function () {
                $scope.grid.refresh();
                Toaster.success("删除成功！");
            });
        };
        $scope.enableAppVersion = function (appVersion) {
            AppVersionService.enable(appVersion.objectId).then(function () {
                $scope.grid.refresh();
                Toaster.success("启用成功！");
            });
        };
        $scope.disableAppVersion = function (appVersion) {
            AppVersionService.disable(appVersion.objectId).then(function () {
                $scope.grid.refresh();
                Toaster.success("停用成功！");
            });
        };
    })

    .controller('AppVersionCreateCtrl', function ($scope, $modalInstance, FileUploader, AppVersionService) {

        $scope.appVersion = {
            status: 0
        };

        $scope.title = '新增App';

        var uploader = $scope.uploader = new FileUploader({
            url: PageContext.path + '/std/swf/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });

        uploader.onProgressItem = function (fileItem, progress) {
            uploader.progress = progress;
        };

        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.appVersion.apkUrl = ' ';
            $scope.appVersion.fileUploadInfo = {
                fileName: response.fileName,
                tempFileName: response.tempFileName
            };
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = AppVersionService.create($scope.appVersion).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('AppVersionUpdateCtrl', function ($scope, $modalInstance, FileUploader, AppVersionService, objectId) {

        $scope.promise = AppVersionService.get(objectId);

        $scope.appVersion = $scope.promise.$object;

        $scope.title = '修改App';

        var uploader = $scope.uploader = new FileUploader({
            url: PageContext.path + '/std/swf/upload',
            alias: 'attachment',
            removeAfterUpload: true,
            autoUpload: true
        });

        uploader.onProgressItem = function (fileItem, progress) {
            uploader.progress = progress;
        };

        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            $scope.appVersion.apkUrl = ' ';
            $scope.appVersion.fileUploadInfo = {
                fileName: response.fileName,
                tempFileName: response.tempFileName
            };
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            $scope.promise = AppVersionService.update($scope.appVersion).then(function () {
                $modalInstance.close();
            });
        };
    })

;