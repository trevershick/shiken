'use strict';

angular.module('shikenApp')
.factory('Operation', [ '$resource', function ($resource) {
    return $resource('api/operations/:id', {}, {
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
}]);
