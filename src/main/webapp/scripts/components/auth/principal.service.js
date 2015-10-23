'use strict';

angular.module('shikenApp')
    .factory('Principal', function Principal($q, Account, Tracker, ROLE_ISUSER) {
        var _identity,
            _authenticated = false;
        return {
            id: function() {
              if (!_authenticated || !_identity) {
                return null;
              }
              return _identity.login;
            },
            gravatarImageUrl: function() {
              if (!_authenticated || !_identity) {
                return null;
              }
              return _identity.gravatarImageUrl;
            },
            isUser: function() {
              return this.isInRole(ROLE_ISUSER);
            },
            fullName: function() {
              if (!_authenticated || !_identity) {
                return null;
              }
              return _identity.firstName + ' ' + _identity.lastName;
            },
            isIdentityResolved: function () {
                return angular.isDefined(_identity);
            },
            isAuthenticated: function () {
                return _authenticated;
            },
            getRoles: function() {
              if (!_authenticated || !_identity || !_identity.roles) {
                return [];
              }
              return _identity.roles;
            },
            isInRole: function (role) {
              if (!_authenticated || !_identity || !_identity.roles) {
                return false;
              }

              var eq = function(r) {
                return r === role;
              };
              if (role.indexOf('*') > -1) {
                role = role.replace('*', '.*');
                eq = function(r) {
                  return !!r.match(role);
                };
              }

              return _.some(_identity.roles, eq);
            },
            isInAnyRole: function (roles) {
              return (roles.length === 0) || _.some(roles, this.isInRole);
            },
            authenticate: function (identity) {
                _identity = identity;
                _authenticated = identity !== null;
            },
            identity: function (force) {
                var deferred = $q.defer();

                if (force === true) {
                    _identity = undefined;
                }

                // check and see if we have retrieved the identity data from the server.
                // if we have, reuse it by immediately resolving
                if (angular.isDefined(_identity)) {
                    deferred.resolve(_identity);

                    return deferred.promise;
                }

                // retrieve the identity data from the server, update the identity object, and then resolve.
                Account.get().$promise
                    .then(function (account) {
                        _identity = account.data;
                        _authenticated = true;
                        deferred.resolve(_identity);
                        Tracker.connect();
                    })
                    .catch(function() {
                        _identity = null;
                        _authenticated = false;
                        deferred.resolve(_identity);
                    });
                return deferred.promise;
            }
        };
    });
