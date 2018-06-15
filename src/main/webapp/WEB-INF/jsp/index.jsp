<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

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
                <a class="px-4 nav-link active" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link" href="member?id=${account.id}">Profile</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link" href="change_credentials">Change Credentials</a>
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
                    <h4>Hi! ${account.name}</h4>
                    <c:if test="${message != null}">
                        <h6 class="text-primary text-center">${message}</h6>
                    </c:if>

                    <hr>

                    <ul class="pl-3 pl-md-4">
                        <c:choose>
                            <c:when test="${account.type == 'cluster head'}">
                                <li>
                                    <span id="cluster-reports" data-toggle="collapse" data-target="#reportsCollapse" aria-expanded="false" aria-controls="reportsCollapse">
                                            ${account.headClusterArea} Cluster Reports
                                        <c:if test="${totalReport != null && totalReport > 0}">
                                            <span class="no-of-reports ml-1 p-1">${totalReport}</span>
                                        </c:if>
                                    </span>
                                    <div class="collapse" id="reportsCollapse">
                                        <ul class="pl-3 pl-md-4">
                                            <li><a href="reports?type=sunday" class="text-primary">Sunday Reports</a>
                                                <c:if test="${noOfSundayReports != null && noOfSundayReports > 0}">
                                                    <span class="no-of-reports ml-1 p-1">${noOfSundayReports}</span>
                                                </c:if>
                                            </li>
                                            <li><a href="reports?type=cg" class="text-primary">Care Group Reports</a>
                                                <c:if test="${noOfCGReports != null && noOfCGReports > 0}">
                                                    <span class="no-of-reports ml-1 p-1">${noOfCGReports}</span>
                                                </c:if>
                                            </li>
                                            <li><a href="reports?type=c2s" class="text-primary">C2S Reports</a>
                                                <c:if test="${noOfC2SReports != null && noOfC2SReports > 0}">
                                                    <span class="no-of-reports ml-1 p-1">${noOfC2SReports}</span>
                                                </c:if>
                                            </li>
                                        </ul>
                                    </div>
                                </li>
                                <hr width="70%" class="ml-0">
                            </c:when>
                        </c:choose>
                        <li>
                            <span id="cg-members" data-toggle="collapse" data-target="#membersCollapse" aria-expanded="false" aria-controls="membersCollapse">
                                Care Group members
                            </span>
                            <div class="collapse" id="membersCollapse">
                                <ul id="members" class="mt-2 pl-2">
                                    <c:forEach var="area" items="${areas}">
                                        <span class="text-info">${area} Cluster Area</span>
                                        <ol class="pl-4">
                                        <c:forEach var="member" items="${members}">
                                            <li>
                                                <a href="member?id=${member.id}" class="text-primary"> ${member.name}</a>
                                                <c:if test="${!member.active}"><span class="text-danger">*</span></c:if>
                                            </li>
                                        </c:forEach>
                                        </ol>
                                    </c:forEach>
                                    <li id="add-member"><a href="add_member" class="text-info">Add member</a></li>
                                </ul>
                            </div>
                        </li>
                        <hr width="70%" class="ml-0">
                        <li><a href="report">Care Group Report</a></li>
                        <hr width="70%" class="ml-0">
                        <c:if test="${reports.size() > 0}">
                            <li><span id="reports">Submitted Reports</span>
                                <c:forEach var="report" items="${reports}">
                                    <ul class="pl-3">
                                        <li>
                                            <a href="delete_report?report_id=${report.reportID}" class="mr-2"> x</a>
                                            <a href="update_report?report_id=${report.reportID}">
                                                ${report.type} Attendance
                                            </a>
                                        </li>
                                    </ul>
                                </c:forEach>
                            </li>
                            <hr width="70%" class="ml-0">
                        </c:if>
                    </ul>
                    <c:if test="${account.type == 'admin'}">
                        <div class="form-group text-center mt-4 mb-0">
                            <a class="btn btn-primary mx-auto" name="button" href="/dl_cg">
                                Download Care Groups
                            </a>
                        </div>
                        <div class="form-group text-center mt-4 mb-0">
                            <a class="btn btn-primary mx-auto" name="button" href="/dl_reports">
                                Download Reports
                            </a>
                        </div>
                    </c:if>

                </div>
            </div>
        </div>
    </div>
    <script src="../../static/js/vendor.min.js"></script>

</body>

</html>
