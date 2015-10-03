'use strict';

angular.module('shikenApp')
    .controller('TestProjectController', function ($scope, TestProject, TestProjectSearch, ParseLinks) {
        $scope.testProjects = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestProject.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.testProjects.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.testProjects = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestProject.get({id: id}, function(result) {
                $scope.testProject = result;
                $('#deleteTestProjectConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestProject.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTestProjectConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestProjectSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.testProjects = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.testProject = {name: null, description: null, prefix: null, active: null, id: null};
        };
    });
