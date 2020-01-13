angular.module('app', ['ngMaterial', 'ngRoute', 'ngMessages', 'appView', 'appForm'])
.controller('AppController', function AppController($scope, $mdPainLessToast) {
	
	$scope.activeComponent = 'app-view'; 
	
	$scope.routeData = null; 

	$scope.getRouteData = function(){
		return $scope.routeData; 
	}

	$scope.setRouteData = function(data){
		$scope.routeData = data; 
	}

	$scope.setActiveComponent = function(componentName) {
		$scope.activeComponent = componentName;
	};

	$scope.showToast = function(msg) {
		$mdPainLessToast.show(msg);
	};

})
.service('$mdPainLessToast', function($mdToast) {
  return { 
    show: function(content) {
    return $mdToast.show(
      $mdToast.simple()
        .content(content)
        .position('top right')
        .hideDelay(3000)
    )}
  }
})
.service('serverCaller', function ($mdPainLessToast) {

    this.addToScopes = function(newScope, scopes){
        //first scope is root
        let scopeArray = [];
        if (scopes != null){
            for (let index in scopes){
                scopeArray.push(scopes[index]);
            }
        }
        scopeArray.push(newScope);
        return scopeArray;
    };

    this.getFormPreparedData = function(data){
        for (let attrName in data){
            let attribute = data[attrName];
            if (attribute.script != ""){
                let preparedScript = attribute.script;
                let attributeNames = preparedScript.match(new RegExp("{[A-Za-z0-9]+}", "g"));
                attributeNames = attributeNames.map(item => item.substr(1, item.length - 2));
                for (let key in attributeNames){
                    let attrName = attributeNames[key];
                    let preparedValue = null;
                    if (data[attrName]){
                        preparedValue = data[attrName].value;
                    }
                    preparedScript = preparedScript.replace( "{" + attrName + "}", preparedValue);
                }
                let formatFunc = eval("(function(){" + preparedScript + "})");
                attribute.value = formatFunc();
            }
        }
        return data;
    };

    this.jsDateToJava = function(date){
        let day = date.getDate() + "";
        if (day.length < 2) day = "0" + day;
        let month = (date.getMonth() + 1) + "";
        if (month.length < 2) month = "0" + month;
        let year = date.getFullYear();

        return (year + '-' + month + '-' + day);

    }

    this.getErrorText = function(response){
        if (response && response.data){
            let msgArray = response.data.match(new RegExp("<body>.+<\/body>"));
            if (msgArray != null){
                if (msgArray.length > 0){
                    let leftPartSymbols = msgArray[0].indexOf("<body>") + "<body>".length;
                    return msgArray[0].substr(leftPartSymbols, msgArray[0].length - leftPartSymbols - "</body>".length);
                }
            }
        }else{
            if (response && response.name && response.name == "Error"){
                let message = decodeURI(response);
                let leftPartSymbols = message.indexOf("p0=") + "&p1={}".length - "p0=".length;
                return message.substr(leftPartSymbols, message.length - leftPartSymbols - "&p1={}".length);
            }
        }
        return "Возникла неизвестная ошибка. Свяжитесь с Администратором системы";
    };

    this.prepareFormSendData = function(data, sendAttributeNames) {

        let fullAttributeNames = this.getAttributeNames(data);

        let attributeNames = sendAttributeNames;

        if (attributeNames == null){
            attributeNames = fullAttributeNames;
        }
        let firstError = null;
        for (let key in attributeNames){
            let validateError = this.validateAttribute(data, attributeNames[key]);
            if (validateError != null){
                data[attributeNames[key]].isValid = false;
                if (!firstError){
                    firstError = validateError;
                }
            }else{
                data[attributeNames[key]].isValid = true;
            }
        }
        if (firstError){
            $mdPainLessToast.show(firstError);
            return null;
        }
        let formObject = {};

        for (let key in attributeNames){
            formObject[attributeNames[key]] = "";
        }
        for (let key in attributeNames){
            let attribute = data[attributeNames[key]];
            if (attribute.type == "client"){
                formObject[attributeNames[key]] = this.prepareFormSendData(attribute.value);
            }else{
                if (attribute.type == "number"){
                    formObject[attributeNames[key]] = attribute.value.replace(",", ".");
                }else{
                    formObject[attributeNames[key]] = attribute.value;
                }
            }
        }
        return formObject;
    };

    this.getAttributeNames = function(data){
        let arr = [];
        for (let key in data){
            arr.push(data[key].code);
        }
        return arr;
    };

    this.validateAttribute = function(data, attrName){

        let attribute = data[attrName];
        let value = attribute.value;

        if (value instanceof Date){
            value = this.jsDateToJava(value);
        }

        let errText= "Поле '" + attribute.title + "' обязательно для заполнения";

        if (attribute.required){
            if (value instanceof Object){
                if (value.id.value == ""){
                    return errText;
                }
            }else{
                if(value == null || value.trim() == ""){
                    return errText;
                }
            }
        }

        if (attribute.validators != null && attribute.validators.length > 0){
            for (let key in attribute.validators){
                let validator = attribute.validators[key];
                if (attribute.required || !validator.mask){

                    if (validator.type == "regexp"){
                        let matcher = value.match(new RegExp(validator.regexp));
                        if (matcher == null || matcher.length == 0){
                            return validator.message;
                        }
                    }else if (validator.type == "javascript"){
                        let preparedScript = validator.script;
                        let attributeNames = preparedScript.match(new RegExp("{[A-Za-z0-9.]+}", "g"));
                        attributeNames = attributeNames.map(item => item.substr(1, item.length - 2));
                        for (let key in attributeNames){
                            let attrName = attributeNames[key];
                            let preparedValue = null;
                            if (data[attrName]){
                                preparedValue = data[attrName].value;
                            }
                            if (attrName.indexOf(".") >= 0){
                                let attrNames = attrName.split(".").reverse();
                                let attrName1 = null;
                                while (attrNames.length > 0){
                                    attrName1 = attrNames.pop();
                                    if (preparedValue == null){
                                        preparedValue = data[attrName1];
                                    }else{
                                        preparedValue = preparedValue[attrName1];
                                    }
                                }
                            }
                            if (attribute.type == "number"){
                                preparedValue = preparedValue.replace(",", ".");
                            }
                            preparedScript = preparedScript.replace( "{" + attrName + "}", preparedValue);
                        }
                        let validateFunc = eval("(function(){" + preparedScript + "})");
                        if (!validateFunc()){
                            return validator.message;
                        }
                    }

                }
            }
        }
        return null;
    };

})