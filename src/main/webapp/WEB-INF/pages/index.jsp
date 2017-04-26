

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HIDE微信管理中心</title>
    <link href="/hiden/css/common.css" rel="stylesheet">
    <link href="/hiden/css/page/index.css" rel="stylesheet">

    <link href="/hiden/js/datepicker/foundation-datepicker.css" rel="stylesheet">
</head>
<body status=${status} data-id=${id} defaultName=${defaultName} >
<header class="grid">
    <div class="headPicCol">
        <div id="headerimg" class="titleHeader" title="在线">
            <div class="headImg">
                <div class="img-circle-50">
                    <img src="/hiden/images/top_home.png" />
                </div>

            </div>
            <div id="login_user" class="login_user_title col">
                <p>
                    欢迎光临HIDE微信管理中心
                </p>
            </div>
        </div>
    </div>
    <div class="rightHead">
    	<a href="/hiden/pc/m/1.0/logOut.htm">
    	 <span class="logout" title="退出"></span>
    	</a>
    </div>
</header>
<div class="container grid">
    <div class="leftNav">
        <ul>
            <li id="allUser" class="allUser show" ><i></i><p>全部设备</p></li>
            <li id="allTask" class="allTask" ><i></i><p>全部任务</p></li>
            <li id="unTask" class="unTask" ><i></i><p>未被执行</p></li>
            <%--<li id="TaskError" class="TaskError" ><i></i><p>执行异常</p></li>--%>
            <li id="TaskFail" class="TaskFail" ><i></i><p>执行失败</p></li>
            <li id="TaskSuccess" class="TaskSuccess" ><i></i><p>执行成功</p></li>
            <li id="allTagEnd" class="allTagEnd" ><i></i><p>微信用户</p></li>
            <li id="addressList" class="addressList" ><i></i><p>通讯录</p></li>
        </ul>
        </div>
    <div class="rightContainer">
        <div class="wrap">
            <div class="wrapper grid">

            </div>
            <footer>
               <p>copyright © 2015-2016  深圳米酷科技有限公司<span>All rights Reserved.</span><small class="downloadAPPBtn">下载app</small></p>
            </footer>
        </div>
    </div>
</div>
<script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<script src="/hiden/js/datepicker/foundation-datepicker.js"></script>
<!-- <script src="/hiden/js/datepicker/foundation-datepicker.zh-CN.js"></script> -->
<script src="/hiden/js/page/index.js"></script>
</body>
</html>