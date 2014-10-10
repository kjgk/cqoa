'use strict';

angular.module('meeting')
    .controller('MeetingRoomCtrl', function ($scope, $q, SimpleTable, Restangular) {

        $scope.grid = SimpleTable(Restangular.all('meetingRooms').getList());


    });
