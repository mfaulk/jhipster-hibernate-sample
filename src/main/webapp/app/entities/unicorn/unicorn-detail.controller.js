(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .controller('UnicornDetailController', UnicornDetailController);

    UnicornDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Unicorn'];

    function UnicornDetailController($scope, $rootScope, $stateParams, previousState, entity, Unicorn) {
        var vm = this;

        vm.unicorn = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterhibernatetutorialApp:unicornUpdate', function(event, result) {
            vm.unicorn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
