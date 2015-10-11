'use strict';

angular.module('shikenApp')
    .factory('TestPlan', function ($resource, DateUtils) {
        return $resource('api/testPlans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
