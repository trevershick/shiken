'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testPlan', {
                parent: 'entity',
                url: '/testPlans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'shikenApp.testPlan.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testPlan/testPlans.html',
                        controller: 'TestPlanController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testPlan');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testPlan.detail', {
                parent: 'entity',
                url: '/testPlan/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'shikenApp.testPlan.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testPlan/testPlan-detail.html',
                        controller: 'TestPlanDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testPlan');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestPlan', function($stateParams, TestPlan) {
                        return TestPlan.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testPlan.new', {
                parent: 'testPlan',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testPlan/testPlan-dialog.html',
                        controller: 'TestPlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, active: null, apikey: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testPlan', null, { reload: true });
                    }, function() {
                        $state.go('testPlan');
                    })
                }]
            })
            .state('testPlan.edit', {
                parent: 'testPlan',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testPlan/testPlan-dialog.html',
                        controller: 'TestPlanDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestPlan', function(TestPlan) {
                                return TestPlan.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testPlan', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
