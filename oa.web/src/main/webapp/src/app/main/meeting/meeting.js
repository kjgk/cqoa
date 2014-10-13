'use strict';

angular.module('app.sms')
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('sms.meeting', {
                url: '/meeting',
                template: '<div ui-view></div>',
                abstract: true
            })
            .state('sms.meeting.meetingroom', {
                url: '/meetingroom',
                templateUrl: 'components/sms/meeting/meeting-room-list.html',
                controller: 'MeetingRoomCtrl'
            })
        ;

    })

;