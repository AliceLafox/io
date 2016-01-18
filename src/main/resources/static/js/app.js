/**
 * Created by Alice Lafox <alice@lafox.net> on 03.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
angular.module('science', ['ngFileUpload']);

angular.module('science')

    .constant('serviceUrl', 'http://localhost:8081')

    .value('initToken', {
        siteName: 'angular-test',
        ownerName: 'item',
        ownerId: '1024',
        readToken: '',
        writeToken: ''
    });




