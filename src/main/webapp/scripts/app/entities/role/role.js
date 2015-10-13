'use strict';

var app = angular.module('shikenApp');

app.config(function ($stateProvider) {
  $stateProvider
  .state('role', {
    parent: 'entity',
    url: '/roles',
    data: {
      roles: ['OP_MG_ROLES'],
      pageTitle: 'Roles'
    },
    views: {
      'content@': {
        templateUrl: 'scripts/app/entities/role/roles.html',
        controller: 'RoleController'
      }
    },
    resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            // $translatePartialLoader.addPart('role');
            $translatePartialLoader.addPart('global');
            return $translate.refresh();
        }]
    }
  })
  .state('role.detail', {
      parent: 'entity',
      url: '/role/{id}',
      data: {
          roles: ['OP_MG_ROLES'],
          pageTitle: 'Role Details'
      },
      views: {
          'content@': {
              templateUrl: 'scripts/app/entities/role/role-detail.html',
              controller: 'RoleDetailController'
          }
      },
      resolve: {
          entity: ['$stateParams', 'Role', function($stateParams, Role) {
              return Role.get({id : $stateParams.id});
          }],
          allOperations: ['Operation', function(Operation) {
            return Operation.query({per_page:-1});
          }]
      }
  })
  .state('role.new', {
      parent: 'role',
      url: '/new',
      data: {
          roles: ['OP_MG_ROLES'],
      },
      onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
          $modal.open({
              templateUrl: 'scripts/app/entities/role/role-dialog.html',
              controller: 'RoleDialogController',
              size: 'lg',
              resolve: {
                  entity: function () {
                      return {$new:true, title:null, name: null, description: null, operations:[]};
                  },
                  allOperations: ['Operation', function(Operation) {
                    return Operation.query({per_page:-1});
                  }]
              }
          }).result.then(function(result) {
              $state.go('role', null, { reload: true });
          }, function() {
              $state.go('role');
          })
      }]
  })
  .state('role.edit', {
      parent: 'role',
      url: '/{id}/edit',
      data: {
          roles: ['OP_MG_ROLES'],
      },
      onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
          $modal.open({
              templateUrl: 'scripts/app/entities/role/role-dialog.html',
              controller: 'RoleDialogController',
              size: 'lg',
              resolve: {
                  entity: ['Role', function(Role) {
                      return Role.get({id : $stateParams.id});
                  }],
                  allOperations: ['Operation', function(Operation) {
                    return Operation.query({per_page:-1});
                  }]
              }
          }).result.then(function(result) {
              $state.go('role', null, { reload: true });
          }, function() {
              $state.go('^');
          })
      }]
  });
});

app.factory('Role', function ($resource, DateUtils) {
    return $resource('api/roles/:id', {}, {
        'query': { method: 'GET', isArray: true },
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        },
        'update': { method:'PUT' }
    });
});
