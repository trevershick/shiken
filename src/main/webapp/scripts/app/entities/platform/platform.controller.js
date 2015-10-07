'use strict';

angular.module('shikenApp')
    .controller('PlatformController', function ($scope, Platform, PlatformSearch, ParseLinks) {
        $scope.platforms = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Platform.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.platforms.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.platforms = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Platform.get({id: id}, function(result) {
                $scope.platform = result;
                $('#deletePlatformConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Platform.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePlatformConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PlatformSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.platforms = result;
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
            $scope.platform = {name: null, description: null, id: null};
        };
    });
