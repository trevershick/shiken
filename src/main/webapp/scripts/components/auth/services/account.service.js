'use strict';

angular.module('shikenApp')
  .factory('AccountService',[ '$http', function($http) {
      function AccountService() {
        this.refresh = this.refresh.bind(this);
        this.user = null;
      }
      AccountService.prototype.refresh = function(data) {
        return $http.get("api/account").then(function(response) {
          this.user = response.data;
          this.user.fullName = this.user.firstName + " " + this.user.lastName;
        }.bind(this));
      };
      return AccountService;
  }])
  .service('accountService', ['AccountService', function(AccountService){
    return new AccountService();
  }])
    .factory('Account', function Account($resource) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    });
