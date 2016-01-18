<%--
 * Created by Alice Lafox <alice@lafox.net> on 07.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <%--<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">--%>
  <link rel="stylesheet" href="/css/bootstrap.min.css">
  <link rel="stylesheet" href="/css/app.css">
  <%--<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.15/angular.min.js"></script>--%>
  <script src="/js/angular.min.js"></script>

  <%--<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>--%>
  <%--<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>--%>

  <script src="/js/ng-file-upload.min.js"></script>
  <script src="/js/app.js"></script>
  <script src="/js/app-directives.js"></script>
  <script src="/js/image-controller.js"></script>

  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <title></title>
</head>
<body ng-app="science">
<div class="page-header">
  <h1>Image REST project
    <small>{{"AngularJS Demo HTML page" | uppercase }}</small>
  </h1>
<h3-text text='${message}'></h3-text>
</div>



<token-register></token-register>
<image-edit-gallery></image-edit-gallery>
<image-uploader></image-uploader>

</body>
</html>
