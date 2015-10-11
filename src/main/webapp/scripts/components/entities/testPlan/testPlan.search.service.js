'use strict';

angular.module('shikenApp')
    .factory('TestPlanSearch', function ($resource) {
        return $resource('api/_search/testPlans/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
