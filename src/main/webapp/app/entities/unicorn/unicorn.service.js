(function() {
    'use strict';
    angular
        .module('jhipsterhibernatetutorialApp')
        .factory('Unicorn', Unicorn);

    Unicorn.$inject = ['$resource'];

    function Unicorn ($resource) {
        var resourceUrl =  'api/unicorns/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
