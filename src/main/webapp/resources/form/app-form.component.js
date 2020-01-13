angular.module('appForm', ['ngMaterial', 'ngRoute', 'ngMessages'])
  .component('appForm', {
    templateUrl: 'resources/form/app-form.template.html',
    controller: function AppFormController($scope, $http, $mdDialog, serverCaller) {

      $scope.data = {};

      $scope.clientFullName = "";

      $scope.rowIndex = -1;

      $scope.getParentRouteData = () => $scope.$parent.getRouteData();

      $scope.setParentRouteData = (data) => {$scope.$parent.setRouteData(data)}

      $scope.showToast = (msg) => {$scope.$parent.showToast(msg)}

      $scope.changeComponent = (componentName, params) => {
        $scope.$parent.setRouteData(params);
        $scope.$parent.setActiveComponent(componentName);
      }

      $http({
        method: 'GET',
        url: 'api/form/agreement/' + $scope.getParentRouteData().id
      }).then(function (response) {
        $scope.data = serverCaller.getFormPreparedData(response.data);
          $scope.updateClientFullName(true);
      }, function (error) {
          $scope.showToast(serverCaller.getErrorText(error));
      });

      function CreateClientDialogController($scope, $http, $mdDialog, parentScopes) {

        $scope.data = {};

        $scope.cancel = () => {$mdDialog.cancel()};

        $scope.save = function () {
            let sendData = serverCaller.prepareFormSendData($scope.data, null);
            if (sendData == null){
                return;
            }
            $http({
                method: 'PUT',
                data: sendData,
                url: 'api/form/client/'
            }).then(function (response) {
                sendData.id = response.data;
                $mdDialog.hide(sendData);
            }, function (error) {
                parentScopes[0].showToast(serverCaller.getErrorText(error));
            });
        };

          $http({
              method: 'GET',
              url: 'api/form/client/0'
          }).then(function (response) {
              $scope.data = serverCaller.getFormPreparedData(response.data);;
          }, function (error) {
              $scope.showToast(serverCaller.getErrorText(error));
          });

      }

      function EditClientDialogController($scope, $http, $mdDialog, parentScopes, clientId) {

        $scope.data = {};

        $scope.cancel = function () {
          $mdDialog.cancel();
        };

        $scope.save = function () {
            let sendData = serverCaller.prepareFormSendData($scope.data, null);
            if (sendData == null){
                return;
            }
            $http({
                method: 'POST',
                data: sendData,
                url: 'api/form/client-edit/'
            }).then(function (response) {
                $mdDialog.hide(sendData);
            }, function (error) {
                parentScopes[0].showToast(serverCaller.getErrorText(error));
            });
        };

          $http({
              method: 'GET',
              url: 'api/form/client-edit/' + clientId
          }).then(function (response) {
              $scope.data = serverCaller.getFormPreparedData(response.data);;
          }, function (error) {
              $scope.showToast(serverCaller.getErrorText(error));
          });

      }

      function SelectClientDialogController($scope, $http, $mdDialog, parentScopes) {

          $scope.parentScope = parentScopes[parentScopes.length - 1];

        $scope.table = {};

          $scope.lName = "";

          $scope.fName = "";

          $scope.pName = "";

        $scope.searchIsEmpty = false;

        $scope.showList = false;

        $scope.selectRow = function (index) {
          let updateRow = true;
          if ($scope.parentScope.rowIndex != undefined && $scope.parentScope.rowIndex != -1) {
            let rowElement = angular.element(document.querySelector('#row' + $scope.parentScope.rowIndex));
            if ($scope.parentScope.rowIndex == index) {
              updateRow = false;
            }
            if (updateRow) {
              rowElement.removeClass('table-tr-data-selected');
              rowElement.addClass('table-tr-data');
            }
          }
          if (updateRow) {
              $scope.parentScope.rowIndex = index;
            let rowElement = angular.element(document.querySelector('#row' + $scope.parentScope.rowIndex));
            rowElement.removeClass('table-tr-data');
            rowElement.addClass('table-tr-data-selected');
          }
        };

        $scope.cancel = function () {
            $scope.parentScope.rowIndex = -1;
          $mdDialog.cancel();
        };

        $scope.select = function (result) {
          if ($scope.parentScope.rowIndex == undefined || $scope.parentScope.rowIndex == -1) {
              $scope.parentScope.showToast('Выберите клиента');
                return;
          }
          let result1 = {data: {id: $scope.table.data[$scope.parentScope.rowIndex].id}};
          $scope.parentScope.rowIndex = -1;
          $mdDialog.hide(result1);
        };

        $scope.showToast = function (msg) {
            $scope.parentScope.showToast(msg);
        };

        $scope.create = function () {

          $mdDialog.show({
              controller: CreateClientDialogController,
              templateUrl: 'resources/dialog/create-client.html',
              parent: angular.element(document.body),
              clickOutsideToClose: true,
              fullscreen: $scope.customFullscreen,
              locals: {
                  parentScopes: serverCaller.addToScopes($scope, parentScopes)
              }
          }).then(function (result) {
              parentScopes[0].data.client.value.id.value = result.id;
              parentScopes[0].data.client.value.lName.value = result.lName;
              parentScopes[0].data.client.value.fName.value = result.fName;
              parentScopes[0].data.client.value.pName.value = result.pName;
              parentScopes[0].data.client.value.birthday.value = result.birthday;
              parentScopes[0].data.clientBirthday.value = result.birthday;
              parentScopes[0].updateClientFullName();
              }, function (result) {

              });
        };

        $scope.search = function () {

          $http({
            method: 'GET',
            url: 'api/view/client/search?lName=' + $scope.lName + '&fName=' + $scope.fName + '&pName=' + $scope.pName
          }).then(function (response) {
            $scope.table = response.data;
              $scope.searchIsEmpty = ($scope.table.data.length == 0);
              $scope.showList = ($scope.table.data.length > 0);
              $scope.parentScope.rowIndex = -1;
          }, function (error) {
              parentScopes[0].showToast(serverCaller.getErrorText(error));
          });

        };

      }

      $scope.selectClient = function () {
        $mdDialog.show({
          controller: SelectClientDialogController,
          templateUrl: 'resources/dialog/select-client.html',
          parent: angular.element(document.body),
          clickOutsideToClose: true,
          fullscreen: $scope.customFullscreen,
          locals: {
              parentScopes: serverCaller.addToScopes($scope)
          }
        }).then(function (result) {
            $http({
                method: 'GET',
                url: 'api/form/client/' + result.data.id.value
            }).then(function (response) {
                let data = response.data;
                $scope.data.client.value.id.value = data.id.value;
                $scope.data.client.value.lName.value = data.lName.value;
                $scope.data.client.value.fName.value = data.fName.value;
                $scope.data.client.value.pName.value = data.pName.value;
                $scope.data.client.value.birthday.value = data.birthday.value;
                $scope.data.clientBirthday.value = data.birthday.value;
                $scope.data.clientPassportSerial.value = data.passportSerial.value;
                $scope.data.clientPassportNumber.value = data.passportNumber.value;
                $scope.updateClientFullName();
            }, function (error) {
                $scope.showToast(serverCaller.getErrorText(error));
            });

          }, function (result) {

          });
      };

      $scope.editClient = function () {

        $mdDialog.show({
          controller: EditClientDialogController,
          templateUrl: 'resources/dialog/edit-client.html',
          parent: angular.element(document.body),
          clickOutsideToClose: true,
          fullscreen: $scope.customFullscreen,
          locals: {
              clientId: $scope.data.client.value.id.value,
              parentScopes: serverCaller.addToScopes($scope)
          }
        }).then(function (result) {
            $scope.data.client.value.id.value = result.id;
            $scope.data.client.value.lName.value = result.lName;
            $scope.data.client.value.fName.value = result.fName;
            $scope.data.client.value.pName.value = result.pName;
            $scope.data.client.value.birthday.value = result.birthday;
            $scope.data.clientBirthday.value = result.birthday;
            $scope.data.clientPassportSerial.value = result.passportSerial;
            $scope.data.clientPassportNumber.value = result.passportNumber;
            $scope.updateClientFullName();
          }, function (result) {

          });
      };

      $scope.calc = function(){
          let sendData = serverCaller.prepareFormSendData($scope.data, "insuranceAmount|limitDateStart|limitDateEnd|typeOfProperty|yearOfConstruction|area".split("|"));
          if (sendData == null){
            return;
          }
          $http({
              method: 'POST',
              data: sendData,
              url: 'api/form/agreement/calculate'
          }).then(function (response) {
              for (let key in serverCaller.getFormPreparedData(response.data)){
                  let attribute = response.data[key];
                  $scope.data[attribute.code] = attribute;
              }
          }, function (error) {
              $scope.showToast(serverCaller.getErrorText(error));
          });
      };

      $scope.save = function(){

          let sendData = serverCaller.prepareFormSendData($scope.data, null);
          if (sendData == null){
              return;
          }
          $http({
              method: (sendData.id > 0 ? 'POST' : 'PUT'),
              data: sendData,
              url: 'api/form/agreement/'
          }).then(function (response) {
              if (sendData.id == 0){
                  $scope.data.id.value = response.data;
              }
              $scope.showToast("Данные успешно сохранены");
          }, function (error) {
              $scope.showToast(serverCaller.getErrorText(error));
          });
      };

      $scope.updateClientFullName = function(init){
          $scope.clientFullName = $scope.clientFullName = $scope.data.client.value.lName.value + " " +
              $scope.data.client.value.fName.value + " " +
              $scope.data.client.value.pName.value;
          if (init){
              return;
          }
          let attrNames = ["client", "clientBirthday", "clientPassportSerial", "clientPassportNumber"];
          for (let key in attrNames){
              $scope.data[attrNames[key]].isValid = (serverCaller.validateAttribute($scope.data, attrNames[key]) == null);
          }
        };

      $scope.setParentRouteData(null);

    }
  })