'use strict';

var app = angular.module('shikenApp');

app.factory('Operation', function ($resource, DateUtils) {
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
});
