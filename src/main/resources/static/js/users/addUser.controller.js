(function () {
    function addUserController(usersService, $modalInstance, redirectService) {
        var vm = this;
        vm.addUser = addUser;
        vm.cancel = cancel;
        vm.user= {};

        function addUser (){
            if (!vm.user.id
            || !vm.user.password
            || !vm.user.repassword){
                vm.errorMessage = "All fields should be filled";
            } else if(vm.user.password != vm.user.repassword) {
                vm.errorMessage = "Passwords are mot match";
            } else {
                usersService.store(vm.user).success(close).error(redirectService.onError);
            }
        }
        function close() {
            $modalInstance.close('close');
        }
        function cancel() {
            $modalInstance.dismiss('cancel');
        }
    }


    addUserController.$inject = ['usersService', '$modalInstance', 'redirectService'];

    angular.module('expenses').controller("addUserController", addUserController);
})();