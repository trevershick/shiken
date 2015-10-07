'use strict';

angular.module('shikenApp').controller('PlatformDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Platform',
        function($scope, $stateParams, $modalInstance, entity, Platform) {

        $scope.platform = entity;
        $scope.load = function(id) {
            Platform.get({id : id}, function(result) {
                $scope.platform = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('shikenApp:platformUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.platform.id != null) {
                Platform.update($scope.platform, onSaveFinished);
            } else {
                Platform.save($scope.platform, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
