'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('account', {
                abstract: true,
                parent: 'site'
            });
        $stateProvider
            .state('my', {
                url: '/my',
                abstract: true,
                parent: 'site'
            });
    });
