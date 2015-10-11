'use strict';

angular.module('shikenApp')
    .controller('TestPlanController', function ($scope, TestPlan, TestPlanSearch, ParseLinks) {
        $scope.testPlans = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            TestPlan.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.testPlans = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TestPlan.get({id: id}, function(result) {
                $scope.testPlan = result;
                $('#deleteTestPlanConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TestPlan.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTestPlanConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TestPlanSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.testPlans = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.testPlan = {name: null, description: null, active: null, apikey: null, id: null};
        };
    });
