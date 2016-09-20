(function () {
    'use strict';

    angular
        .module('jhipsterhibernatetutorialApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
