/**
 * Created by Alice Lafox <alice@lafox.net> on 18.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

angular.module('science')

    .directive('imageUploader', function () {
        return {
            restrict: 'E',
            templateUrl: '/templates/image-uploader.html',
            controller: "ImageController",
            controllerAs: 'imageCtrl'
        };
    })

    .directive('imageEditGallery', function(){
        return {
            restrict: 'E',
            templateUrl: '/templates/image-edit-gallery.html',
            controller: "ImageController",
            controllerAs: 'imageCtrl'
        };
    })

    .directive('thumbnail', function(){
        return {
            restrict: 'E',
            scope: {image: '='},
            template: '<img width=100 height="100" ng-src="http://localhost:8081/{{image.id}}-w100-h100-oc-q50-v{{image.v}}.jpg"/>'
        };
    })

    .directive('tokenRegister', function(){
        return {
            restrict: 'E',
            templateUrl: '/templates/token-register.html',
            controller: "ImageController",
            controllerAs: 'imageCtrl'
        };
    })
    .directive('h3Text', function(){
        return {
            restrict: 'E',
            scope: {text: '@'},
            template: '<h3>{{text}}</h3>'
        };
    })

;


