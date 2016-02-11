/**
 * Created by Alice Lafox <alice@lafox.net> on 06.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

angular.module('image')
    .controller('ImageController', ['$http', '$scope', 'Upload', 'serviceUrl', 'initToken', 'imageService', 'tokenService', function (
            $http, 
            $scope, 
            Upload, 
            serviceUrl, 
            initToken,
            imageService,
            tokenService ) {
        
        $scope.token = initToken;
        var imageCtrl = this;

        $scope.getToken = function () {
            tokenService.getToken($scope.token).then(function (response) {
                $scope.token.readToken = response.data.readToken;
                $scope.token.writeToken = response.data.writeToken;
                $scope.loadImages();
            });

        };

        $scope.loadImages = function () {
            imageService.loadImages($scope.token.readToken).then(function (response) {
                    $scope.images = response.data.images;
            });
        };

        imageCtrl.deleteImage = function (imageId) {
            imageService.delete(imageId, $scope.token.writeToken)
                .then(function (response) {
                    $scope.loadImages();
                    imageCtrl.cleanCurrentImage();
                });
        };

        $scope.uploadFiles = function (files) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    Upload.upload({
                        url: serviceUrl + '/image/upload',
                        data: {data: files[i], token: $scope.token.writeToken}
                    }).success(function () {
                        $scope.loadImages();
                    }).then(function (resp) {
                        console.log('Success ' + resp.config.data.data.name + ' uploaded. Response: ' + resp.data);
                    });
                }
            }
        };

        this.submit = function () {
            $scope.uploadFiles($scope.files);
            $scope.files = {};
        };


        imageCtrl.cleanCurrentImage = function() {
            imageCtrl.currentImage = null;
            imageCtrl.editedImage = {};
            imageCtrl.showUpdateTitleDesc=false;

        }

        imageCtrl.cleanCurrentImage();

        imageCtrl.setCurrentImage = function(image) {
            imageCtrl.currentImage = image;
            imageCtrl.editedImage = angular.copy(imageCtrl.currentImage);
            imageCtrl.showUpdateTitleDesc=true;
        };


        imageCtrl.updateTitle = function(){
            imageService.updateTitle(imageCtrl.editedImage.id, imageCtrl.editedImage.title, $scope.token.writeToken)
                .then(function successCallback (response) {
                    $scope.loadImages();
                }, function errorCallback (response) {
                    console.log(response);
                });
        };

        imageCtrl.updateDescription = function(){
            imageService.updateDescription(imageCtrl.editedImage.id, imageCtrl.editedImage.description, $scope.token.writeToken)
                .then(function successCallback (response) {
                    $scope.loadImages();
                }, function errorCallback (response) {
                    console.log(response);
                });
        };


}]);
