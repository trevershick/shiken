'use strict';

angular.module('shikenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('platform', {
                parent: 'entity',
                url: '/platforms',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'shikenApp.platform.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/platform/platforms.html',
                        controller: 'PlatformController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('platform');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('platform.detail', {
                parent: 'entity',
                url: '/platform/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'shikenApp.platform.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/platform/platform-detail.html',
                        controller: 'PlatformDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('platform');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Platform', function($stateParams, Platform) {
                        return Platform.get({id : $stateParams.id});
                    }]
                }
            })
            .state('platform.new', {
                parent: 'platform',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/platform/platform-dialog.html',
                        controller: 'PlatformDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('platform', null, { reload: true });
                    }, function() {
                        $state.go('platform');
                    })
                }]
            })
            .state('platform.edit', {
                parent: 'platform',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/platform/platform-dialog.html',
                        controller: 'PlatformDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Platform', function(Platform) {
                                return Platform.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('platform', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
