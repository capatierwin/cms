<%@ page import="com.cogclarkview.reporting.models.Account" %>
<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Care Group Reporting</title>
    <link rel="shortcut icon" href="../../static/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../static/stylesheets/vendor.css">
    <link rel="stylesheet" href="../../static/stylesheets/styles.css">

</head>

<body>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
    if (session.getAttribute("account") == null) {
        response.sendRedirect("login");
    }
%>

<nav class="navbar navbar-expand-lg bg-primary">
    <a class="navbar-brand">Reporting</a>
    <button class="navbar-toggler navbar-light" type="button" data-toggle="collapse"
            data-target="#navbarText" aria-controls="navbarText"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="px-4 nav-link" href="create">Add</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link active" href="change">Update</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link" href="login">Logout</a>
            </li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="row justify-content-center align-items-center">
        <div class="col-md-6">
            <div class="content-holder mt-5 mb-5">
                <h3 class="text-center">Update Account's Credentials</h3>
                <hr>
                <form action="save_update" method="POST">
                    <div class="form-group">
                        <label class="text-info">ID</label>
                        <input type="number" name="id" placeholder="---" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label class="text-info">Username</label>
                        <input type="text" name="username" placeholder="---" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label class="text-info">Password</label>
                        <input type="password" class="form-control" name="password" required>
                    </div>
                    <div class="form-group text-center mt-4 mb-0">
                        <button type="submit" class="btn btn-primary mx-auto" name="button">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="../../static/js/vendor.min.js"></script>

</body>

</html>
