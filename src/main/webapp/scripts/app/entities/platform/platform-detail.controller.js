'use strict';

angular.module('shikenApp')
    .controller('PlatformDetailController', function ($scope, $rootScope, $stateParams, entity, Platform) {
        $scope.platform = entity;
        $scope.load = function (id) {
            Platform.get({id: id}, function(result) {
                $scope.platform = result;
            });
        };
        $rootScope.$on('shikenApp:platformUpdate', function(event, result) {
            $scope.platform = result;
        });
    });
