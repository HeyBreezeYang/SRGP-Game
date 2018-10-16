<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>平台管理人员账号列表</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title"> <span class="icon"> <i class="icon-th"></i> </span>
                <h5>查询结果</h5>
            </div>
            <div class="widget-content nopadding">
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>GM</th>
                            <th>root</th>
                            <th>创建时间</th>
                        </tr>
                    </thead>
                    <tbody class="myBody">
                        <c:if test="${! empty userList}">
                            <c:forEach items="${userList}" var="bean">
                                <tr>
                                    <td>${bean.id}</td>
                                    <td>${bean.name}</td>
                                    <td>${bean.root==1}</td>
                                    <td>${bean.createTimeMsg}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>