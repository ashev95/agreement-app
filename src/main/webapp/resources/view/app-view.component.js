angular.module('appView', ['ngMaterial', 'ngRoute'])
  .component('appView', {
    templateUrl: 'resources/view/app-view.template.html',
    controller: function AppViewController($scope, $http) {
      $scope.table = {};

      $scope.rowIndex - 1;

      $scope.getParentRouteData = function () {
        return $scope.$parent.getRouteData();
      };

      $scope.setParentRouteData = function (data) {
        $scope.$parent.setRouteData(data);
      };

      $scope.createAgreement = function () {
        $scope.changeComponent('app-form', { id: 0 });
      };

      $scope.openAgreement = function () {
        if ($scope.rowIndex == undefined || $scope.rowIndex == -1) {
          $scope.showToast('Выберите договор');
          return;
        }
        $scope.changeComponent('app-form', { id: $scope.table.data[$scope.rowIndex].id.value });
      };

        $scope.openAgreementByDblClick = function (event) {
            if (event && event.srcElement && event.srcElement.parentElement && event.srcElement.parentElement.id){
                let rowIndex = event.srcElement.parentElement.id.substr("row".length);
                $scope.rowIndex = rowIndex;
            }
            $scope.openAgreement();
        };

      $scope.selectRow = function (index) {
        let updateRow = true;
        if ($scope.rowIndex != undefined && $scope.rowIndex != -1) {
          let rowElement = angular.element(document.querySelector('#row' + $scope.rowIndex));
          if ($scope.rowIndex == index) {
            updateRow = false;
          }
          if (updateRow) {
            rowElement.removeClass('table-tr-data-selected');
            rowElement.addClass('table-tr-data');
          }
        }
        if (updateRow) {
          $scope.rowIndex = index;
          let rowElement = angular.element(document.querySelector('#row' + $scope.rowIndex));
          rowElement.removeClass('table-tr-data');
          rowElement.addClass('table-tr-data-selected');
        }
      };

      $scope.showToast = function (msg) {
        $scope.$parent.showToast(msg);
      };

      $scope.changeComponent = function (componentName, params) {
        $scope.rowIndex = -1;
        $scope.$parent.setRouteData(params);
        $scope.$parent.setActiveComponent(componentName);
      };

      $http({
        method: 'GET',
        url: 'api/view/agreement/all'
      }).then(function (response) {
        $scope.table = response.data;
      }, function (error) {
        $scope.showToast('Возникла ошибка. Пожалуйста, свяжитесь с администратором системы');
      });

      $scope.setParentRouteData(null);

    }
  })