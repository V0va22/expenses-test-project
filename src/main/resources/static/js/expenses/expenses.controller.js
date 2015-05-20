(function () {
    function expensesController(expensesService, $modal, $filter, ngTableParams, redirectService) {
        var vm = this;
        vm.addDialog = addDialog;
        vm.editDialog = openDialog;
        vm.delete = deleteExpense;
        vm.expenses = [];
        init();
        vm.tableParams = new ngTableParams({
            page: 1,
            count: 25,
            sorting: {
                date: 'asc'
            }
        }, {
            total: vm.expenses.length, // length of data
            getData: function($defer, params) {
                // use build-in angular filter
                var orderedData = params.sorting() ?
                    $filter('orderBy')(vm.expenses, params.orderBy()) :
                    vm.expenses;
                orderedData = params.filter() ?
                    $filter('filter')(orderedData, params.filter()) :
                    orderedData;

                orderedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData);
            }
        });
        function init() {
            expensesService.getExpenses().success(showExpenses).error(redirectService.onError);
        }
        function showExpenses (data){
            vm.expenses = data;
            vm.tableParams.reload();
        }

        function addDialog(){
            openDialog({date: new Date()})
        }

        function openDialog(expense){
            var date = new Date(expense.date);
            var time = new Date(0);
            time.setHours(date.getHours());
            time.setMinutes(date.getMinutes());
            time.setSeconds(0);
            time.setMilliseconds(0);

            expense.date = date;
            expense.time = time;

            $modal.open({
                animation: true,
                templateUrl: 'views/addExpense.html',
                controller: 'addExpenseController as vm',
                size: 'medium',
                resolve: {
                    expense:function(){
                        return expense
                    }}
                }
            ).result.then(init);1
        }

        function deleteExpense(expense){
            vm.expenses.splice(vm.expenses.indexOf(expense), 1);
            expensesService.deleteExpense(expense.id).error(redirectService.onError);
            vm.tableParams.reload();
        }
    }


    expensesController.$inject = ['expensesService', '$modal', '$filter', 'ngTableParams', 'redirectService'];

    angular.module('expenses').controller("expensesController", expensesController);
})();