'use strict';

angular.module('shikenApp')
    .factory('PlatformSearch', function ($resource) {
        return $resource('api/_search/platforms/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
