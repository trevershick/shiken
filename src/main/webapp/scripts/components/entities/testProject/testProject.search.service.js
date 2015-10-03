'use strict';

angular.module('shikenApp')
    .factory('TestProjectSearch', function ($resource) {
        return $resource('api/_search/testProjects/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
