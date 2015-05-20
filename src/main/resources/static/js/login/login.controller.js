(function () {
    function loginController(loginService, redirectService) {
        var vm = this;
        vm.login = login;
        vm.register = register;

        function login() {
            loginService.login({username: vm.username, password: vm.password}).success(storeRole).error(errorMessage("wrong credentials"));
        }

        function register() {
            loginService.register({id: vm.username, password: vm.password}).success(storeRole).error(errorMessage("registration failed try another username"));
        }

        function storeRole(data) {
            redirectService.setRole(data);
            redirectService.toHomePage();
        }
        function errorMessage(message) {
            return function(){
                vm.errorMessage = message;
            }
        }

    }
    loginController.$inject = ['loginService', 'redirectService'];

    angular.module('login').controller("loginController", loginController);
})();