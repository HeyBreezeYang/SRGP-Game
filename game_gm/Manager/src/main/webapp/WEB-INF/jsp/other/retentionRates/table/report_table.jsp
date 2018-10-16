<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="description" content="Xenon Boostrap Admin Panel" />
    <meta name="author" content="" />
    <title>${tableName}</title>
    <link rel="stylesheet" href="${GmBase}/assets/css/fonts/linecons/css/linecons.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/fonts/fontawesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/xenon-core.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/xenon-forms.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/xenon-components.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/xenon-skins.css">
    <link rel="stylesheet" href="${GmBase}/assets/css/custom.css">
    <script src="${GmBase}/assets/js/jquery-1.11.1.min.js"></script>
</head>
<body class="page-body">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="table-responsive" data-pattern="priority-columns" data-focus-btn-icon="fa-asterisk" data-sticky-table-header="true" data-add-display-all-btn="true" data-add-focus-btn="true">
                        <table cellspacing="0" class="table table-small-font table-bordered table-striped">
                            <thead>
                                <tr>
                                    <c:forEach items="${tableHead}" var="headMsg">
                                       <%-- <th <c:if test="${headMsg[1]==1}">data-priority="${headMsg[2]}"</c:if> >${headMsg[0]}</th>--%>
                                        <th>${headMsg}</th>
                                    </c:forEach>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${!empty set}">
                                    <c:forEach items="${set['everyDay']}" var="day" varStatus="status">
                                        <tr>
                                            <td>${set["sid"]}</td>
                                            <td>${set["channel"]}</td>
                                            <td>${day}</td>
                                            <td>${set[day]}</td>
                                            <c:forEach items="${RRMsg}" var="msg">
                                                <c:forEach items="${msg[day]}" var="rr">
                                                    <td>${rr}</td>
                                                </c:forEach>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <script type="text/javascript">
                        jQuery(document).ready(function($)
                        {
                            setTimeout(function()
                            {
                                $(".checkbox-row input").addClass('cbr');
                                cbr_replace();
                            }, 0);
                        });
                    </script>
                </div>
            </div>
        </div>
    </div>
    <script src="${GmBase}/assets/js/bootstrap.min.js"></script>
    <script src="${GmBase}/assets/js/resizeable.js"></script>
    <script src="${GmBase}/assets/js/xenon-api.js"></script>
    <script src="${GmBase}/assets/js/xenon-toggles.js"></script>
    <script src="${GmBase}/assets/js/rwd-table/js/rwd-table.min.js"></script>
    <script src="${GmBase}/assets/js/xenon-custom.js"></script>
    </body>
</html>
