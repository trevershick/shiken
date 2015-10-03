'use strict';

angular.module('shikenApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
