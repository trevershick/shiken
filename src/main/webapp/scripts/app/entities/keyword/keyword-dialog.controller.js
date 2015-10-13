'use strict';

angular.module('shikenApp').controller('KeywordDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Keyword',
        function($scope, $stateParams, $modalInstance, entity, Keyword) {

        $scope.keyword = entity;
        $scope.load = function(id) {
            Keyword.get({id : id}, function(result) {
                $scope.keyword = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('shikenApp:keywordUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.keyword.id != null) {
                Keyword.update($scope.keyword, onSaveFinished);
            } else {
                Keyword.save($scope.keyword, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };


                $scope.confirmDelete = function (id) {
                    Keyword.delete({id: id},
                        function () {
                            $scope.reset();
                            $('#deleteKeywordConfirmation').modal('hide');
                            $scope.clear();
                        });
                };
}]);
