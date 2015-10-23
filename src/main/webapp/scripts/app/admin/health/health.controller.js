'use strict';

angular.module('shikenApp')
    .controller('HealthController', function ($scope, MonitoringService, $modal, $timeout) {
        $scope.updatingHealth = true;
        $scope.separator = '.';

        $scope.doneUpdating = function() {
          $scope.__t = $timeout(function() {
            $scope.updatingHealth = false;
          }, 500);
        };
        $scope.startUpdating = function() {
          $timeout.cancel($scope.__t);
            $scope.updatingHealth = true;
        };
        $scope.refresh = function () {
          $scope.startUpdating();
            MonitoringService.checkHealth().then(function (response) {
                $scope.healthData = $scope.transformHealthData(response);
            }, function (response) {
                $scope.healthData =  $scope.transformHealthData(response.data);
            }).finally($scope.doneUpdating);
        };

        $scope.refresh();

        $scope.transformHealthData = function (data) {
            var response = [];
            $scope.flattenHealthData(response, null, data);
            return response;
        };

        $scope.flattenHealthData = function (result, path, data) {
            angular.forEach(data, function (value, key) {
                if ($scope.isHealthObject(value)) {
                    if ($scope.hasSubSystem(value)) {
                        $scope.addHealthObject(result, false, value, $scope.getModuleName(path, key));
                        $scope.flattenHealthData(result, $scope.getModuleName(path, key), value);
                    } else {
                        $scope.addHealthObject(result, true, value, $scope.getModuleName(path, key));
                    }
                }
            });
            return result;
        };

        $scope.getModuleName = function (path, name) {
            var result;
            if (path && name) {
                result = path + $scope.separator + name;
            }  else if (path) {
                result = path;
            } else if (name) {
                result = name;
            } else {
                result = '';
            }
            return result;
        };


        $scope.showHealth = function(health) {
            $modal.open({
                templateUrl: 'scripts/app/admin/health/health.modal.html',
                controller: 'HealthModalController',
                size: 'lg',
                resolve: {
                    currentHealth: function() {
                        return health;
                    },
                    baseName: function() {
                        return $scope.baseName;
                    },
                    subSystemName: function() {
                        return $scope.subSystemName;
                    }

                }
            });
        };

        $scope.addHealthObject = function (result, isLeaf, healthObject, name) {

            var healthData = {
                'name': name
            };
            var details = {};
            var hasDetails = false;

            angular.forEach(healthObject, function (value, key) {
                if (key === 'status' || key === 'error') {
                    healthData[key] = value;
                } else {
                    if (!$scope.isHealthObject(value)) {
                        details[key] = value;
                        hasDetails = true;
                    }
                }
            });

            // Add the of the details
            if (hasDetails) {
                angular.extend(healthData, { 'details': details});
            }

            // Only add nodes if they provide additional information
            if (isLeaf || hasDetails || healthData.error) {
                result.push(healthData);
            }
            return healthData;
        };

        $scope.hasSubSystem = function (healthObject) {
            var result = false;
            angular.forEach(healthObject, function (value) {
                if (value && value.status) {
                    result = true;
                }
            });
            return result;
        };

        $scope.isHealthObject = function (healthObject) {
            var result = false;
            angular.forEach(healthObject, function (value, key) {
                if (key === 'status') {
                    result = true;
                }
            });
            return result;
        };

        $scope.baseName = function (name) {
            if (name) {
              var split = name.split('.');
              return split[0];
            }
        };

        $scope.subSystemName = function (name) {
            if (name) {
              var split = name.split('.');
              split.splice(0, 1);
              var remainder = split.join('.');
              return remainder ? ' - ' + remainder : '';
            }
        };
    });
