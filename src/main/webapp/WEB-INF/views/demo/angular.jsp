<%--
 * Created by Alice Lafox <alice@lafox.net> on 07.01.16
 * Lafox.Net Software developers Team http://dev.lafox.net
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <link rel="stylesheet" href="/css/app.css">
  <title>Image REST project with AngularJS demo</title>
</head>
<body ng-app="image">
<div class="page-header">
  <h1>Image REST project
    <small>{{"AngularJS Demo HTML page" | uppercase }}</small>
  </h1>
<h3-text text='${message}'></h3-text>
</div>


<token-register></token-register>
<image-uploader></image-uploader>

<image-edit-gallery></image-edit-gallery>
<update-title-description></update-title-description>


<script src="/js/head.load.min.js"></script>
<script src="/js/boot.js"></script>
</body>
</html>
