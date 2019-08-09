var consoleControllers = angular.module('consoleControllers', [])

.controller('MainCtrl', ['$scope', '$rootScope', '$location', 'DataService', 
  function ($scope, $rootScope, $location, DataService) {
    DataService.getProfile().then(function(p){
    	$scope.profile = p;
    });
    
    $scope.publishApp = function() {
    	$scope.errorText = '';
    	$scope.successText = '';
    	DataService.publishApp().then(function(res){
        	$scope.errorText = '';
        	$scope.successText = 'App published!';
        	$scope.profile = res;
    	},
    	function(e) {
        	$scope.errorText = 'Failed publishing app';
        	$scope.successText = '';
    	});
    };
    $scope.publishType = function(type) {
    	DataService.publishType(type).then(function(res){
        	$scope.errorText = '';
        	$scope.successText = 'Type published!';
        	$scope.profile = res;
    	},
    	function(e) {
        	$scope.errorText = 'Failed publishing type';
        	$scope.successText = '';
    	});
    };
    
  }]);
  