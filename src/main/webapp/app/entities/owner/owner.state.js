(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('owner', {
            parent: 'entity',
            url: '/owner',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Owners'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/owner/owners.html',
                    controller: 'OwnerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('owner-detail', {
            parent: 'entity',
            url: '/owner/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Owner'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/owner/owner-detail.html',
                    controller: 'OwnerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Owner', function($stateParams, Owner) {
                    return Owner.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'owner',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('owner-detail.edit', {
            parent: 'owner-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/owner/owner-dialog.html',
                    controller: 'OwnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Owner', function(Owner) {
                            return Owner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('owner.new', {
            parent: 'owner',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/owner/owner-dialog.html',
                    controller: 'OwnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('owner', null, { reload: 'owner' });
                }, function() {
                    $state.go('owner');
                });
            }]
        })
        .state('owner.edit', {
            parent: 'owner',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/owner/owner-dialog.html',
                    controller: 'OwnerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Owner', function(Owner) {
                            return Owner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('owner', null, { reload: 'owner' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('owner.delete', {
            parent: 'owner',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/owner/owner-delete-dialog.html',
                    controller: 'OwnerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Owner', function(Owner) {
                            return Owner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('owner', null, { reload: 'owner' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
