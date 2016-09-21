(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .controller('CarDetailController', CarDetailController);

    CarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Car', 'Owner'];

    function CarDetailController($scope, $rootScope, $stateParams, previousState, entity, Car, Owner) {
        var vm = this;

        vm.car = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterhibernatetutorialApp:carUpdate', function(event, result) {
            vm.car = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
