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
                <a class="px-4 nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="px-4 nav-link active" href="member?id=${account.id}">Profile</a>
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
                    <h2>
                        <c:if test="${worker.isLeader}">
                            Leader's Data
                        </c:if>
                        <c:if test="${!worker.isLeader}">
                            Member's Data
                        </c:if>
                    </h2>
                    <hr>
                    <ul class="pl-3 pl-md-5 members-data">
                        <li><b>Name: </b>${worker.name}</li>
                        <c:if test="${leader != null}">
                            <li><b>Leader's Name: </b><a href="member?id=${leader.id}">${leader.name}</a></li>
                        </c:if>
                        <li><b>Cluster Area: </b>${worker.clusterArea}</li>
                        <li><b>Regular: </b>
                            <c:if test="${worker.active}">Yes</c:if>
                            <c:if test="${!worker.active}">No</c:if>
                        </li>
                        <li><b>Address: </b>${worker.address}</li>
                        <li><b>Birthday: </b>${bday}</li>
                        <li><b>Age: </b>${age}</li>
                        <li><b>Contact number: </b>${worker.contactNumber}</li>

                        <li><b>Group age: </b>${worker.groupAge}</li>
                        <li><b>Journey: </b>${worker.journey}</li>
                        <c:if test="${worker.cldp != null}">
                            <li><b>CLDP: </b>${worker.cldp}</li>
                        </c:if>
                        <c:if test="${worker.isLeader}">
                            <li>
                                <b>Members:</b>
                                <c:forEach var="area" items="${areas}">
                                    <ul class="pl-3">
                                        <span class="text-info">${area} Cluster Area</span>
                                        <ol class="pl-4 ml-1">
                                            <c:forEach var="member" items="${members}">
                                                <li>
                                                    <a href="member?id=${member.id}">${member.name}</a>
                                                    <c:if test="${!member.active}"><span class="text-danger">*</span></c:if>
                                                </li>
                                            </c:forEach>
                                        </ol>
                                    </ul>
                                </c:forEach>


                            </li>
                            <li><b>Regular  :</b> ${regular}</li>
                            <li><b>Irregular:</b> ${irregular}</li>
                        </c:if>
                    </ul>
                    <c:if test="${account.id == worker.leaderID}">
                        <div class="text-center">
                            <a class="btn btn-info mx-auto mt-4" href="update_member?id=${worker.id}&leaderID=${leader.id}">Update Member's Data</a>
                        </div>

                        <c:if test="${!worker.isLeader}">
                            <div class="text-center">
                                <a href="set_credentials" class="set-leader text-white btn btn-warning mt-3">Set as Leader</a>
                            </div>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
<script src="../../static/js/vendor.min.js"></script>


</body>

</html>
