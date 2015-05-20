(function () {
    function logoutController(loginService, $scope, redirectService) {
        $scope.logout = logout;
        $scope.isActive = redirectService.isActive;

        function logout() {
            loginService.logout().success(removeRole);
            function removeRole() {
                redirectService.setRole(null);
            }
        }

    }
    logoutController.$inject = ['loginService','$scope', 'redirectService'];

    angular.module('login').controller("logoutController", logoutController);
})();