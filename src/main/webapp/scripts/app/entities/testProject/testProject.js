'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('testProject', {
                parent: 'entity',
                url: '/testProjects',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'shikenApp.testProject.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testProject/testProjects.html',
                        controller: 'TestProjectController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testProject');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('testProject.detail', {
                parent: 'entity',
                url: '/testProject/{id}',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'shikenApp.testProject.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/testProject/testProject-detail.html',
                        controller: 'TestProjectDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('testProject');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TestProject', function($stateParams, TestProject) {
                        return TestProject.get({id : $stateParams.id});
                    }]
                }
            })
            .state('testProject.new', {
                parent: 'testProject',
                url: '/new',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testProject/testProject-dialog.html',
                        controller: 'TestProjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, prefix: null, active: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('testProject', null, { reload: true });
                    }, function() {
                        $state.go('testProject');
                    })
                }]
            })
            .state('testProject.edit', {
                parent: 'testProject',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/testProject/testProject-dialog.html',
                        controller: 'TestProjectDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TestProject', function(TestProject) {
                                return TestProject.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('testProject', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
