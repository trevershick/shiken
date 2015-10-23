'use strict';


var app = angular.module('shikenApp');
app.config(function ($stateProvider) {
        $stateProvider
            .state('keyword', {
                parent: 'entity',
                url: '/keywords',
                data: {
                    roles: ['OP_KEYWORD_*'],
                    pageTitle: 'shikenApp.keyword.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/keyword/keywords.html',
                        controller: 'KeywordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('keyword');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('keyword.detail', {
                parent: 'entity',
                url: '/keyword/{id}',
                data: {
                    roles: ['OP_KEYWORD_VW'],
                    pageTitle: 'shikenApp.keyword.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/keyword/keyword-detail.html',
                        controller: 'KeywordDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('keyword');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Keyword', function($stateParams, Keyword) {
                        return Keyword.get({id : $stateParams.id});
                    }]
                }
            })
            .state('keyword.new', {
                parent: 'keyword',
                url: '/new',
                data: {
                    roles: ['OP_KEYWORD_CR'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/keyword/keyword-dialog.html',
                        controller: 'KeywordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function() {
                        $state.go('keyword', null, { reload: true });
                    }, function() {
                        $state.go('keyword');
                    });
                }]
            })
            .state('keyword.edit', {
                parent: 'keyword',
                url: '/{id}/edit',
                data: {
                    roles: ['OP_KEYWORD_ED'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/keyword/keyword-dialog.html',
                        controller: 'KeywordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Keyword', function(Keyword) {
                                return Keyword.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('keyword', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('keyword.delete', {
                parent: 'keyword',
                url: '/{id}/delete',
                data: {
                    roles: ['OP_KEYWORD_DL'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/keyword/keyword-delete.html',
                        controller: 'KeywordDialogController',
                        size: 'sm',
                        resolve: {
                            entity: ['Keyword', function(Keyword) {
                                return Keyword.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('keyword', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
