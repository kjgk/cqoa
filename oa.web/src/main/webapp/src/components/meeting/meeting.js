'use strict';

angular.module('meeting', ['base'])
    .config(function ($stateProvider, RestangularProvider) {
        $stateProvider
            .state('meeting', {
                url: '/meeting',
                template: '<div ui-view></div>',
                abstract: true
            })
            .state('meeting.meetingroom', {
                url: '/meetingroom',
                templateUrl: 'components/meeting/meetingroom-list.html',
                controller: 'MeetingroomCtrl'
            });

    })
;