'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('admin', {
              url:'/admin',
                abstract: true,
                parent: 'site'
            });
    });
