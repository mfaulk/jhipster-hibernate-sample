(function() {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .controller('UnicornDialogController', UnicornDialogController);

    UnicornDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Unicorn'];

    function UnicornDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Unicorn) {
        var vm = this;

        vm.unicorn = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.unicorn.id !== null) {
                Unicorn.update(vm.unicorn, onSaveSuccess, onSaveError);
            } else {
                Unicorn.save(vm.unicorn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterhibernatetutorialApp:unicornUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
