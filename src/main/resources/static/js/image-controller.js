/**
 * Created by Alice Lafox <alice@lafox.net> on 06.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */

var wUrl = 'http://localhost:8081/image/';

angular.module('science')
    .controller('ImageController', ['$http', '$scope', 'Upload', function ($http, $scope, Upload) {

    $scope.loadImages = function (token) {
        $http.get(wUrl + 'list/' + token).success(function (data) {
            $scope.images = data.images;
        });

    };

    $scope.loadImages('readToken');

    $scope.deleteImage = function (imageId, token) {
        $http.delete(wUrl + 'delete/' + imageId, {params: {token: token}})
            .success(function (data) {
                $scope.loadImages('readToken');
            });
    };

    $scope.uploadFiles = function (files, token) {
        if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                Upload.upload({
                    url: wUrl + 'upload',
                    data: {data: files[i], token: token}
                }).success(function () {
                    $scope.loadImages('readToken');
                }).then(function (resp) {
                    console.log('Success ' + resp.config.data.data.name + ' uploaded. Response: ' + resp.data);
                });
            }
        }
    };

    $scope.submit = function (token) {
        $scope.uploadFiles($scope.files, token);
        $scope.files={};
    };

}]);