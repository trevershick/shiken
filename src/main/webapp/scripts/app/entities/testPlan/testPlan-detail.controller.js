'use strict';

angular.module('shikenApp')
    .controller('TestPlanDetailController', function ($scope, $rootScope, $stateParams, entity, TestPlan) {
        $scope.testPlan = entity;
        $scope.load = function (id) {
            TestPlan.get({id: id}, function(result) {
                $scope.testPlan = result;
            });
        };
        var unsubscribe = $rootScope.$on('shikenApp:testPlanUpdate', function(event, result) {
            $scope.testPlan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    });
