/**
 * Created by Alice Lafox <alice@lafox.net> on 03.01.2016
 * Lafox.Net Software developers Team http://dev.lafox.net
 */
angular.module('image', ['ngFileUpload']);

angular.module('image')

    .constant('serviceUrl', 'http://localhost:8081')

    .value('initToken', {
        siteName: 'test-domain',
        ownerName: 'item',
        ownerId: '1024',
        readToken: '',
        writeToken: ''
    });




