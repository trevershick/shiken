'use strict';
(function(){

  function userMenuDirective() {
    return {
      restrict: 'E',
      replace:true,
      templateUrl: 'scripts/components/navbar/userMenu.html'
    };
  }

  function currentUserBlockDirective() {
    return {
      replace:true,
      restrict: 'E',
      templateUrl: 'scripts/components/navbar/userPanel.html'
    };
  }

  function CurrentUserBlockController($scope, Principal) {
    this.onIdChanged = this.onIdChanged.bind(this);

    this.__scope = $scope;
    this.__principal = Principal;
    this.__scope.fullName = null;
    this.__scope.profileImageUrl = null;
    this.__scope.$watch(Principal.id, this.onIdChanged);

    return this;
  }
  CurrentUserBlockController.$inject = ['$scope','Principal'];


  CurrentUserBlockController.prototype.onIdChanged = function() {
    this.__scope.fullName = this.__principal.fullName();
    this.__scope.profileImageUrl = this.__principal.gravatarImageUrl();
  };
/*
  function activeMenuDirective() {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var language = attrs.activeMenu;
        alert($translate);
        scope.$watch(function() {
          return $translate.use();
        }, function(selectedLanguage) {
          if (language === selectedLanguage) {
            tmhDynamicLocale.set(language);
            element.addClass('active');
          } else {
            element.removeClass('active');
          }
        });
      }
    };
  }
  activeMenuDirective.$inject = ["$translate", "$locale", "tmhDynamicLocale"];
*/

  angular.module('shikenApp')
    .controller('CurrentUserBlockController',CurrentUserBlockController)
    .directive('userMenu', userMenuDirective)
    .directive('currentUserBlock', currentUserBlockDirective);
    /*
    .directive('activeMenu', activeMenuDirective)
      .directive('activeLink', function(location) {
          return {
              restrict: 'A',
              link: function (scope, element, attrs) {
                  var clazz = attrs.activeLink;
                  var path = attrs.href;
                  path = path.substring(1); //hack because path does bot return including hashbang
                  scope.location = location;
                  scope.$watch('location.path()', function(newPath) {
                      if (path === newPath) {
                          element.addClass(clazz);
                      } else {
                          element.removeClass(clazz);
                      }
                  });
              }
          };
      });*/
})();
