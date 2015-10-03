'use strict';

angular.module('shikenApp')
    .controller('TestProjectDetailController', function ($scope, $rootScope, $stateParams, entity, TestProject) {
        $scope.testProject = entity;
        $scope.load = function (id) {
            TestProject.get({id: id}, function(result) {
                $scope.testProject = result;
            });
        };
        $rootScope.$on('shikenApp:testProjectUpdate', function(event, result) {
            $scope.testProject = result;
        });
    });
