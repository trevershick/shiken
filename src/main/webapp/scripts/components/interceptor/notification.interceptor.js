 'use strict';

angular.module('shikenApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-shikenApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-shikenApp-params')});
                }
                return response;
            },
        };
    });