'use strict';

angular.module('shikenApp').controller('TestProjectDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TestProject',
        function($scope, $stateParams, $modalInstance, entity, TestProject) {

        $scope.testProject = entity;
        $scope.load = function(id) {
            TestProject.get({id : id}, function(result) {
                $scope.testProject = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('shikenApp:testProjectUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.testProject.id !== null) {
                TestProject.update($scope.testProject, onSaveFinished);
            } else {
                TestProject.save($scope.testProject, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
