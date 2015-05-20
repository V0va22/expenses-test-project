(function(){
    function configure($routeProvider) {
        $routeProvider
            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'loginController as vm'})
            .when('/expenses', {
                templateUrl: 'views/expenses.html',
                controller: 'expensesController as vm'})
            .when('/users', {
                templateUrl: 'views/users.html',
                controller: 'usersController as vm'})
            .when('/report', {
                templateUrl: 'views/report.html',
                controller: 'reportController as vm'})
            .when('/home', {
                templateUrl: 'views/nocontent.html',
                controller: 'homeController as vm'})
            .otherwise({
                redirectTo: '/home'
            });
    }
    configure.$inject = ['$routeProvider'];
    angular.module('app').config(configure);
})();