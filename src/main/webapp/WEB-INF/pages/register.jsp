

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link href="/hiden/css/common.css" rel="stylesheet">
    <link href="/hiden/css/page/loginPage.css" rel="stylesheet">
    <style type="text/css">
        body{
                background-color: #e1e2e6;
        }
    </style>
</head>
<body>
<div class="login-box register-box">
   <!--  <div class="login-logo">
        <b>欢迎光临HIDE微信管理中心</b>
    </div> -->
    <div class="login-box-body">
        <p class="login-box-msg">注册</p>
        <div class="form-group">
            <input type="text" name="username" class="form-control" placeholder="请输入常用用户名" id="username">

        </div>
        <div class="form-group">
            <input type="text" name="picCode" class="form-control" placeholder="验证码" id="picCode">
            <div class="rightInput"><img src="/hiden/api/m/1.0/getMobileVerificationCode.json"/> </div>
        </div>
        <!--  <div class="form-group">
            <input type="text" name="username" class="form-control" placeholder="请输入短信验证码" id="msgCode">
            <div class="rightInput"><span class="sendvercode">获取验证码</span> </div>
        </div> -->
        <div class="form-group">
            <input type="password" name="password" class="form-control" placeholder="请输入密码" id="password">

        </div>
        <div class="form-group last">
            <input type="password" name="password" class="form-control" placeholder="确认密码" id="password2">
            <p class="tips"></p>
        </div>
        <div class="checkbox icheck ">
            <label class="active">我已经阅读并同意<a href="" class="colorY">《HIDE》用户协议</a> </label>
        </div>
        <button class="btn" id="mysubmit">注册</button>
    <!--     <a href="/hiden/pc/h/1.0/login.htm" class="reg rightTxt">点此帐号登录</a> -->
    </div>

</div>
<script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<script src="/hiden/js/page/register.js"></script>
</body>
</html>