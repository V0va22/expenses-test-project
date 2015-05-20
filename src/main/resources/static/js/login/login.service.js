(function () {
    loginService.$inject = ['$http'];

    angular.module('login').factory('loginService', loginService);

    function loginService($http) {
        return {
            checkLogin : checkRole,
            login : login,
            logout: logout,
            register: register
        };

        function login(user) {
            return $http.post('log-in', user);
        }

        function checkRole() {
            return $http.get('log-in');
        }

        function logout() {
            return $http.get('log-out');
        }
        function register(user) {
            return $http.post('register', user);
        }


    }
})();