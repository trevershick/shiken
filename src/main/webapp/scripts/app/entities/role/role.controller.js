'use strict';

var app = angular.module('shikenApp');
app.controller('RoleController', function ($scope, Role, ParseLinks) {
        $scope.roles = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Role.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.roles.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.roles = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Role.get({id: id}, function(result) {
                $scope.role = result;
                $('#deleteRoleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Role.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteRoleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.role = {name: null, description: null, id: null};
        };
    });
app.controller('RoleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Role', 'allOperations',
        function($scope, $stateParams, $modalInstance, entity, Role, allOperations) {

        $scope.role = entity;
        $scope.allOperations = allOperations;

        $scope.groups = [];
        $scope.groupedOperations = {};

        $scope.allOperations.$promise.then(function(ops) {
          $scope.groupedOperations = _.groupBy(ops, "groupName");
          $scope.groups = _(ops).pluck("groupName").unique().sort().value();
        });
        $scope.selectedCount = function(group) {
          return _($scope.role.operations).filter({groupName: group}).size();
        };
        $scope.hasOperation = function(op) {
          return _.some($scope.role.operations, {name: op.name});
        };
        $scope.addOperation = function(op) {
          $scope.role.operations.push(_.clone(op));
        };
        $scope.removeOperation = function(op) {
          _.remove($scope.role.operations, {name: op.name});
        };

        $scope.load = function(id) {
            Role.get({id : id}, function(result) {
                $scope.role = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('shikenApp:roleUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.role.id != null) {
                Role.update($scope.role, onSaveFinished);
            } else {
                Role.save($scope.role, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);

app.controller('RoleDetailController', function ($scope, $rootScope, $stateParams, entity, allOperations, Role) {
    $scope.role = entity;
    $scope.load = function (id) {
        Role.get({id: id}, function(result) {
            $scope.role = result;
        });
    };
    $scope.allOperations = allOperations;

    $scope.groups = [];
    $scope.groupedOperations = {};

    $scope.allOperations.$promise.then(function(ops) {
      $scope.groupedOperations = _.groupBy(ops, "groupName");
      $scope.groups = _(ops).pluck("groupName").unique().sort().value();
    });
    $scope.selectedCount = function(group) {
      return _($scope.role.operations).filter({groupName: group}).size();
    };
    $scope.hasOperation = function(op) {
      return _.some($scope.role.operations, {name: op.name});
    };
    $rootScope.$on('shikenApp:roleUpdate', function(event, result) {
        $scope.role = result;
    });
});
