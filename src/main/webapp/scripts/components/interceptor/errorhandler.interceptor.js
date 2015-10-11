'use strict';

var ignoreHealth503 = function(response) {
  return (response.data && response.data.status === 'DOWN' && response.status === 503);
};
var ignoreAccount401 = function(response) {
  return (response.status == 401 && response.data.path.indexOf("/api/account") == 0);
};
var ignoreHttpErrors = [ ignoreHealth503, ignoreAccount401 ];

angular.module('shikenApp')
.factory('errorHandlerInterceptor', function ($q, $rootScope) {
  return {
    'responseError': function (response) {
      var ignore = _.some(ignoreHttpErrors, function(predicate) { return predicate(response); });
      if (!ignore) {
        $rootScope.$emit('shikenApp.httpError', response);
      }
      return $q.reject(response);
    }
  };
});
