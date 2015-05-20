(function () {
    function homeController(redirectService) {
        redirectService.checkLogin();

    }
    homeController.$inject = [ 'redirectService'];

    angular.module('login').controller("homeController", homeController);
})();