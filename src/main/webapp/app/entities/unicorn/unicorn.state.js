(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('unicorn', {
            parent: 'entity',
            url: '/unicorn',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Unicorns'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unicorn/unicorns.html',
                    controller: 'UnicornController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('unicorn-detail', {
            parent: 'entity',
            url: '/unicorn/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Unicorn'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unicorn/unicorn-detail.html',
                    controller: 'UnicornDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Unicorn', function($stateParams, Unicorn) {
                    return Unicorn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'unicorn',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('unicorn-detail.edit', {
            parent: 'unicorn-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unicorn/unicorn-dialog.html',
                    controller: 'UnicornDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unicorn', function(Unicorn) {
                            return Unicorn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unicorn.new', {
            parent: 'unicorn',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unicorn/unicorn-dialog.html',
                    controller: 'UnicornDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                color: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('unicorn', null, { reload: 'unicorn' });
                }, function() {
                    $state.go('unicorn');
                });
            }]
        })
        .state('unicorn.edit', {
            parent: 'unicorn',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unicorn/unicorn-dialog.html',
                    controller: 'UnicornDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Unicorn', function(Unicorn) {
                            return Unicorn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unicorn', null, { reload: 'unicorn' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unicorn.delete', {
            parent: 'unicorn',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unicorn/unicorn-delete-dialog.html',
                    controller: 'UnicornDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Unicorn', function(Unicorn) {
                            return Unicorn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unicorn', null, { reload: 'unicorn' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
