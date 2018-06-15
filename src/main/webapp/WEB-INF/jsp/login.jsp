<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html class="h-100">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Care Group Reporting</title>
    <link rel="shortcut icon" href="../../static/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../static/stylesheets/vendor.css">
    <link rel="stylesheet" href="../../static/stylesheets/styles.css">
</head>

<body class="h-100">

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>

<div class="container h-100 form-holder">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-md-6">
            <div class="account-wall">
                <div id="my-tab-content" class="tab-content">
                    <div id="login">
                        <div class="text-center">
                            <img class="logo-holder img-fluid profile-img" src="../../static/images/login.png" alt="">
                        </div>
                        <form class="form-signin" action="/" method="POST">
                            <input type="text" name="username" class="form-control mb-2" placeholder="Username" required="required" autofocus="autofocus">
                            <input type="password" name="password" class="form-control" placeholder="Password" required="required">
                            <input type="submit" class="btn btn-lg btn-info btn-block" value="Sign In" />
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>
