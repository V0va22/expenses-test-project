(function () {
    function usersController(usersService, $modal, redirectService) {

        var vm = this;
        vm.openDialog = openDialog;
        vm.updateUser = updateUser;
        vm.delete = deleteUser;
        init();
        function init() {
            usersService.getUsers().success(showUsers).error(redirectService.onError);
        }
        function showUsers (data){
            vm.users = data;
        }
        function openDialog(){
            $modal.open({
                animation: true,
                templateUrl: 'views/addUser.html',
                controller: 'addUserController as vm',
                size: 'medium'
            }).result.then(init);
        }
        function deleteUser(user){
            vm.users.splice(vm.users.indexOf(user), 1);
            usersService.deleteUser(user.id);
        }
        function updateUser(user){
            usersService.update(user);
        }
    }




    usersController.$inject = ['usersService', '$modal', 'redirectService'];

    angular.module('users').controller("usersController", usersController);
})();

