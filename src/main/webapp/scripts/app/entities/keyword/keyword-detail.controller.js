'use strict';

angular.module('shikenApp')
    .controller('KeywordDetailController', function ($scope, $rootScope, $stateParams, entity, Keyword) {
        $scope.keyword = entity;
        $scope.load = function (id) {
            Keyword.get({id: id}, function(result) {
                $scope.keyword = result;
            });
        };
        var unsubscribe = $rootScope.$on('shikenApp:keywordUpdate', function(event, result) {
            $scope.keyword = result;
        });
        $scope.$on('$destroy', unsubscribe);
    });
