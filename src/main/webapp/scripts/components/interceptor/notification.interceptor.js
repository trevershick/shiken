 'use strict';

angular.module('shikenApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-shikenApp-alert');
                if (angular.isString(alertKey)) {
                  var params = { param : response.headers('X-shikenApp-params')};
                  if (response.data) {
                    params = response.data;
                  }
                  AlertService.success(alertKey, params);
                }
                return response;
            },
        };
    });
