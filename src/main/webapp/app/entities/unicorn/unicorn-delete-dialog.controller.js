(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .controller('UnicornDeleteController',UnicornDeleteController);

    UnicornDeleteController.$inject = ['$uibModalInstance', 'entity', 'Unicorn'];

    function UnicornDeleteController($uibModalInstance, entity, Unicorn) {
        var vm = this;

        vm.unicorn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Unicorn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
