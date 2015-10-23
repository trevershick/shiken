/* globals window,SockJS,Stomp */
'use strict';

angular.module('shikenApp')
    .factory('Tracker', function ($rootScope, $cookies, $http, $q, localStorageService) {
        var stompClient = null;
        var subscriber = null;
        var listener = $q.defer();
        var connected = $q.defer();
        var alreadyConnectedOnce = false;
        function sendActivity() {
            if (stompClient !== null && stompClient.connected) {
                stompClient
                    .send('/topic/activity',
                    {},
                    JSON.stringify({'page': $rootScope.toState.name}));
            }
        }
        return {
            connect: function () {

              var loc = window.location;
              var webSocketUrl = '//' + loc.host + '/websocket/tracker';
              var token = localStorageService.get('token');
              if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                  webSocketUrl += '?access_token=' + token.access_token;
              } else {
                  webSocketUrl += '?access_token=no token';
              }
              var socket = new SockJS(webSocketUrl);
              stompClient = Stomp.over(socket);

                // //building absolute path so that websocket doesnt fail when deploying with a context path
                // var loc = window.location;
                // // var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
                // var url = '//' + loc.host + '/websocket/tracker';
                // var socket = new SockJS(url);
                stompClient = Stomp.over(socket);
                var headers = {};
                // headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
                stompClient.connect(headers, function() {
                    connected.resolve('success');
                    sendActivity();
                    if (!alreadyConnectedOnce) {
                        $rootScope.$on('$stateChangeStart', function () {
                            sendActivity();
                        });
                        alreadyConnectedOnce = true;
                    }
                });
            },
            subscribe: function() {
                connected.promise.then(function() {
                    subscriber = stompClient.subscribe('/topic/tracker', function(data) {
                        listener.notify(JSON.parse(data.body));
                    });
                }, null, null);
            },
            unsubscribe: function() {
                if (subscriber !== null) {
                    subscriber.unsubscribe();
                }
            },
            receive: function() {
                return listener.promise;
            },
            sendActivity: function () {
                if (stompClient !== null) {
                    sendActivity();
                }
            },
            disconnect: function() {
                if (stompClient !== null) {
                    stompClient.disconnect();
                    stompClient = null;
                }
            }
        };
    });
