(function () {
    function addExpenseController(expensesService, $modalInstance, redirectService, expense) {
        var vm = this;
        vm.addExpense = addExpense;
        vm.cancel = cancel;
        vm.expense= expense;

        function addExpense (){
            if (vm.expense.description
                && vm.expense.comment
                && vm.expense.category
                && vm.expense.amount
                && vm.expense.date
                && vm.expense.time
            ){
                vm.expense.date.setHours(vm.expense.time.getHours());
                vm.expense.date.setMinutes(vm.expense.time.getMinutes());

                expensesService.addExpense(vm.expense).success(close).error(redirectService.onError);
            } else {
                vm.errorMessage = "all fields should be filled"
            }
        }
        function close() {
            $modalInstance.close('close');
        }
        function cancel() {
            $modalInstance.dismiss('cancel');
        }
    }


    addExpenseController.$inject = ['expensesService', '$modalInstance', 'redirectService', 'expense'];

    angular.module('expenses').controller("addExpenseController", addExpenseController);
})();