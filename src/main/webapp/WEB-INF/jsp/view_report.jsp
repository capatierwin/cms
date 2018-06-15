<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Care Group Report</title>
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


                <c:if test="${account.type == 'cluster head'}">
                    <form action="validate_report?reportID=${report.reportID}" method="POST">
                </c:if>
                    <h2 class="text-sm-center">${report.type} Report</h2>
                    <hr>

                    <label class="text-info mt-2">Care Group Leader's Name</label>
                    <p class="ml-4 mb-3">${leaderName}</p>
                    
                    <c:if test="${report.type == 'C2S'}">
                        <label class="text-info mt-2">C2S Leader</label>
                        <p class="ml-4 mb-3">${report.c2sLeader}</p>
                    </c:if>

                    <label class="text-info">Cluster Area</label>
                    <p class="ml-4 mb-3">${report.clusterArea}</p>

                    <label class="text-info">Date and Time</label>
                    <p class="ml-4 mb-3">${dateCG}</p>

                    <label class="text-info">Topic</label>
                    <p class="ml-4 mb-3">${report.topic}</p>


                    <c:if test="${report.type != 'C2S'}">
                        <label class="text-info">Offering</label>
                        <p class="ml-4 mb-3">${report.offering}</p>
                    </c:if>

                    <label class="text-info mb-0">Attendance</label>
                    <p class="text-muted mb-0">Present:</p>
                    <ol>
                        <c:forEach var="check" items="${present}">
                            <c:if test="${report.type == 'C2S'}">
                                <li>${check}</li>
                            </c:if>
                            <c:if test="${report.type != 'C2S'}">
                                <li>${check.name}
                                    <c:if test="${!check.active}"><span class="text-danger">*</span></c:if>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ol>

                    <span class="text-muted mb-0">Absent:</span>
                    <ol>
                        <c:if test="${report.type != 'C2S'}">
                            <c:forEach var="uncheck" items="${absent}">
                                <li>${uncheck.name}
                                    <c:if test="${!uncheck.active}"><span class="text-danger">*</span></c:if>
                                </li>
                            </c:forEach>
                        </c:if>
                        <c:if test="${report.type == 'C2S'}">
                            <c:forEach var="uncheck" items="${absent}">
                                <li>${uncheck}</li>
                            </c:forEach>
                        </c:if>
                    </ol>

                    <label class="text-info">Consolidation Report</label>
                    <p class="ml-4 mb-3">${report.consolidationReport}</p>

                    <label class="text-info">Date Submitted</label>
                    <p class="ml-4 mb-3">${dateSubmitted}</p>

                    <c:if test="${account.type == 'cluster head'}">
                        <div class="form-group">
                            <label class="text-info" for="comment">Comment</label>
                            <textarea id="comment" name="comment" class="form-control"></textarea>
                        </div>
                        <div class="form-group w-100 text-center">
                            <button type="submit" class="btn btn-primary m-auto">Submit</button>
                        </div>
                    </form>
                    </c:if>
                    
                    <c:if test="${account.type == 'admin'}">
                        <label class="text-info">Date Verified</label>
                        <p class="ml-4 mb-3">${dateVerified}</p>

                        <label class="text-info">Cluster Head Comment</label>
                        <p class="ml-4 mb-3">${report.comment}</p>

                        <div class="form-group w-100 text-center">
                            <a href="check_report?report_id=${report.reportID}" class="set-leader text-white btn btn-success mt-3">Check</a>
                        </div>
                    </c:if>

            </div>
        </div>
    </div>
</div>
<script src="../../static/js/vendor.min.js"></script>

</body>

</html>
