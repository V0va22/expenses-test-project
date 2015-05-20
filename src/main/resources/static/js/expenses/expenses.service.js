(function () {
    expensesService.$inject = ['$http'];

    angular.module('expenses').factory('expensesService', expensesService);

    function expensesService($http) {
        return {
            getExpenses: getExpenses,
            addExpense: addExpense,
            deleteExpense: deleteExpense,
            generateReport: generateReport
        };

        function getExpenses() {
            return $http.get('expense');
        }
        function deleteExpense(id) {
            return $http.delete('expense/' + id);
        }
        function addExpense(expense) {
            return $http.post('expense', expense);

        }
        function generateReport(start, end) {
            return $http.get('report', {
                params:{
                    start: start.getTime(),
                    end: end.getTime()
                }
            });
        }
    }
})();