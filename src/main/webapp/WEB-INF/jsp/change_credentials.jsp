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
    <a class="navbar-brand" href="/">
        <img class="img-fluid img-holder" src="../../static/images/brand.png" alt="">
    </a>
    <button class="navbar-toggler navbar-light" type="button" data-toggle="collapse"
            data-target="#navbarText" aria-controls="navbarText"
            aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="px-4 nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link" href="member?id=${account.id}">Profile</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link active" href="change_credentials">Change Credentials</a>
            </li>
            <c:if test="${account.type == 'admin'}">
                <li class="nav-item">
                    <a class="px-4 nav-link" href="cg_list">Care Groups</a>
                </li>
                <li class="nav-item">
                    <a class="px-4 nav-link" href="verified_reports">Reports
                        <c:if test="${totalReport != null && totalReport > 0}">
                            <span class="no-of-reports ml-1 p-1">${totalReport}</span>
                        </c:if>
                    </a>
                </li>
            </c:if>
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
                <div class="text-center">
                    <h3 >Change Credentials</h3>
                    <c:if test="${message != null}">
                        <span class="text-danger">${message}</span>
                    </c:if>
                    <hr>
                </div>

                <form action="save_credentials" method="POST">
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
