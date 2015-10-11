'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
          .state('refdata', {
              abstract: true,
              url: '/refdata',
              parent: 'site'
          });
        $stateProvider
            .state('entity', {
                parent:'refdata',
                abstract: true,
                url: '/entity'
            });
    });
