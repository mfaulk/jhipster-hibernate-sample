(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .controller('UnicornController', UnicornController);

    UnicornController.$inject = ['$scope', '$state', 'Unicorn'];

    function UnicornController ($scope, $state, Unicorn) {
        var vm = this;
        
        vm.unicorns = [];

        loadAll();

        function loadAll() {
            Unicorn.query(function(result) {
                vm.unicorns = result;
            });
        }
    }
})();
