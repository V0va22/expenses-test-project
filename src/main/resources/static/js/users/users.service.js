(function () {
    usersService.$inject = ['$http'];

    angular.module('users').factory('usersService', usersService);

    function usersService($http) {
        return {
            getUsers: getUsers,
            store: store,
            update: update,
            deleteUser: deleteUser
        };

        function getUsers() {
            return $http.get('user');
        }

        function deleteUser(id) {
            return $http.delete('user/' + id);
        }

        function store(user) {
            return $http.put('user', user);

        }
        function update(user) {
            return $http.post('user', user);

        }

    }
})();