'use strict';

angular.module('shikenApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


