/**
 * Created by alice on 09.02.16.
 */

angular.module('image')
    .service('imageService', ['$http','serviceUrl', function ($http, serviceUrl) {
        var service = this;

        service.updateTitle = function (imageId, title, token) {
            if (imageId == null) return;
            return $http({
                method: 'POST',
                url: serviceUrl + '/image/title/' + imageId,
                params: {title: title, token: token}
            });
        };

        service.updateDescription = function (imageId, value, token) {
            if (imageId == null) return;
            return $http({
                method: 'POST',
                url: serviceUrl + '/image/description/' + imageId,
                params: {description: value, token: token}
            });
        };

        service.delete = function (imageId, token) {
            if (imageId == null) return;
           return $http.delete(serviceUrl + '/image/delete/' + imageId, {params: {token: token}}
            )
        };

        service.loadImages = function (readToken) {
          return  $http.get(serviceUrl + '/image/list/' + readToken);
        }

    }]);

angular.module('image')
    .service('tokenService', ['$http','serviceUrl', function ($http, serviceUrl) {
        var service = this;
        service.getToken = function (token){
            return  $http({
            method: 'POST',
            url: serviceUrl+'/api/token/add',
            params: token
        })
        }
    }]);

