(function () {
    redirectService.$inject = ['$http', '$location', '$rootScope'];

    angular.module('login').factory('redirectService', redirectService);

    function redirectService($http, $location, $rootScope) {
        return {
            checkLogin : checkRole,
            toHomePage : redirectToPage,
            onError: onError,
            setRole: setRole,
            isActive: isActive
        };
        function setRole(role) {
            $rootScope.role = role;
        }
        function checkRole() {
            return $http.get('log-in').success(selectHomePage).error(onError);
        }
        function onError() {
            $rootScope.role = null;
            $location.path("/login")
        }
        function selectHomePage(data) {
            $rootScope.role = data;
            redirectToPage();
        }
        function redirectToPage() {
            if ($rootScope.role == "USER"){
                $location.path("/expenses")
            } else if ($rootScope.role == "ADMIN"){
                $location.path("/users")
            } else {
                $location.path("/login")
            }
        }

        function isActive(route) {
            return route === $location.path();
        }
    }
})();