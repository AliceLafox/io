/**
 * Created by Alice Lafox <alice@lafox.net> on 06.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

angular.module('science')
    .controller('ImageController', ['$http', '$scope', 'Upload', 'serviceUrl', 'initToken', function ($http, $scope, Upload, serviceUrl, initToken) {
        $scope.token = initToken;

        $scope.getToken = function () {
            $http({
                method: 'POST',
                url: serviceUrl+'/api/token/add',
                params: $scope.token
            }).success(function (data) {
                $scope.token.readToken = data.readToken;
                $scope.token.writeToken = data.writeToken;
                $scope.loadImages();
            });

        };

        $scope.loadImages = function () {
            $http.get(serviceUrl + '/image/list/' + $scope.token.readToken).success(function (data) {
                $scope.images = data.images;
            });
        };

        $scope.deleteImage = function (imageId) {
            $http.delete(serviceUrl + '/image/delete/' + imageId, {params: {token: $scope.token.writeToken}})
                .success(function (data) {
                    $scope.loadImages();
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

}]);
