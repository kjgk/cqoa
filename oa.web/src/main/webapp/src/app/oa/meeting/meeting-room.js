'use strict';

angular.module('app.oa')

    .config(function ($stateProvider) {
        $stateProvider
            .state('oa.meetingroom', {
                url: '/meetingroom',
                templateUrl: 'app/oa/meeting/meeting-room-list.html',
                controller: 'MeetingRoomCtrl'
            })
        ;
    })

    .factory('MeetingRoomService', function ($http, Restangular) {
        var api = Restangular.all('oa/meeting/meetingRoom');
        return {
            query: function (params) {
                return $http({
                    url: PageContext.path + '/oa/meeting/meetingRoom',
                    method: 'GET',
                    params: params
                });
            },
            get: function (objectId) {
                return api.get(objectId);
            },
            create: function (meetingRoom) {
                return api.doPOST(meetingRoom);
            },
            update: function (meetingRoom) {
                return api.doPUT(meetingRoom);
            },
            remove: function (objectId) {
                return api.doDELETE(objectId);
            }
        }
    })

    .controller('MeetingRoomCtrl', function ($scope, $q, $modal, toaster, SimpleTable, MeetingRoomService) {

        $scope.grid = SimpleTable(MeetingRoomService.query);

        $scope.createMeetingRoom = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/meeting/meeting-room-form.html',
                controller: 'MeetingRoomCreateCtrl'
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.updateMeetingRoom = function (meetringRoom) {
            var modalInstance = $modal.open({
                templateUrl: 'app/oa/meeting/meeting-room-form.html',
                controller: 'MeetingRoomUpdateCtrl',
                resolve: {
                    objectId: function () {
                        return meetringRoom.objectId;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "保存成功！");
            });
        };
        $scope.deleteMeetingRoom = function (meetringRoom) {
            MeetingRoomService.remove(meetringRoom.objectId).then(function () {
                $scope.grid.refresh();
                toaster.pop('success', "信息", "删除成功！");
            });
        };
    })

    .controller('MeetingRoomCreateCtrl', function ($scope, $modalInstance, MeetingRoomService) {

        $scope.meetingRoom = {};
        $scope.title = '新增会场';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            MeetingRoomService.create($scope.meetingRoom).then(function () {
                $modalInstance.close();
            });
        };
    })

    .controller('MeetingRoomUpdateCtrl', function ($scope, $modalInstance, MeetingRoomService, objectId) {


        $scope.meetingRoom = MeetingRoomService.get(objectId).$object;
        $scope.title = '修改会场';

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.submit = function () {
            MeetingRoomService.update($scope.meetingRoom).then(function () {
                $modalInstance.close();
            });
        };
    });
