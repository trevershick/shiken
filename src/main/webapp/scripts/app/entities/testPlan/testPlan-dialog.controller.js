'use strict';

angular.module('shikenApp').controller('TestPlanDialogController',
['$scope', '$stateParams', '$modalInstance', 'entity', 'TestPlan', 'TestProject',
function($scope, $stateParams, $modalInstance, entity, TestPlan, TestProject) {

  $scope.testPlan = entity;
  $scope.testprojects = TestProject.query();
  $scope.load = function(id) {
    TestPlan.get({id : id}, function(result) {
      $scope.testPlan = result;
    });
  };

  var onSaveFinished = function (result) {
    $scope.$emit('shikenApp:testPlanUpdate', result);
    $modalInstance.close(result);
  };

  $scope.save = function () {
    if ($scope.testPlan.id != null) {
      TestPlan.update($scope.testPlan, onSaveFinished);
    } else {
      TestPlan.save($scope.testPlan, onSaveFinished);
    }
  };

  $scope.clear = function() {
    $modalInstance.dismiss('cancel');
  };
}]);
