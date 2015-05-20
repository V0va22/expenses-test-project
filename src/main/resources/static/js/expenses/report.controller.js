(function () {
    function reportController(expensesService, redirectService) {
        var vm = this;

        vm.start = weekAgo(new Date());
        vm.end = new Date();
        vm.generateReport = generateReport;
        vm.export = exportTable;
        function exportTable(type) {
            var type = type ? type : 'csv';
            $('#report-table').tableExport({type:type,escape:'false'});;
        }
        function generateReport() {
            expensesService.generateReport(vm.start, vm.end).success(showReport).error(redirectService.onError);
        }
        function showReport (data){
            vm.report = data;
        }

        function weekAgo(date) {
            date.setDate(date.getDate() - 7);
            return date;
        }
    }

    reportController.$inject = ['expensesService', 'redirectService'];

    angular.module('expenses').controller("reportController", reportController);
})();