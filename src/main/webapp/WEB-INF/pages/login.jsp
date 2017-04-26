

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link href="/hiden/css/common.css" rel="stylesheet">
    <link href="/hiden/css/page/loginPage.css" rel="stylesheet">
</head>
<body>
<div class="login-box">
    <div class="login-logo">
        <b>欢迎光临HIDE微信管理中心</b>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg">登录</p>
        
        <form action="/hiden/pc/h/1.0/login.htm" method="post">
        <div class="form-group">
            <!-- 13189071314   123456-->
            <input type="text" name="mobile" class="form-control" placeholder="用户名" id="username" value="">
            <span class="icon user-icon"></span>

        </div>
        <div class="form-group last">
            <input type="password" name="password" class="form-control" placeholder="密码" id="password"  value="">
            <span class="icon pswd-icon"></span>
            <p class="tips">${errorMsg }</p>
        </div>
        <div class="checkbox icheck ">
            <label class="active">记住密码</label>
            <span class="forgetpsd rightTxt">忘记密码</span>

        </div>
        <button class="btn" id="mysubmit">登录</button>
        <%--<a href="/hiden/pc/h/1.0/register.htm" class="reg rightTxt">点此注册</a>--%>
        </form>
    </div>
</div>
<script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
<script>
    $(function(){
    	
    	var data = localStorage.getItem('saveUser');
        data = JSON.parse(data);
        if(data &&  data.length > 0){
            $('#username').val(data[0]);
            $('#password').val(data[1])
        }
        $('.icheck label').click(function(){
            $(this).toggleClass('active');
        })
    	
    	
    	
    	
    	
    	
        $('#mysubmit').click(function(){
            var $username = $.trim($('#username').val()),
                $password = $.trim($('#password').val());
            if(!$username){
               $('.tips').show().text('请输入帐号');
                return false
            }

            if(!$password){
                $('.tips').show().text('请输入密码');
                return false
            }
            
            
            if($('.icheck label').hasClass('active')){
                var saveUser = [$username,$password,1];
                localStorage.setItem('saveUser',JSON.stringify(saveUser));
            }
            else{
                localStorage.removeItem('saveUser');
            }

        })
        $('#username,#password').keyup(function(){
            $('.tips').hide();
        })
    })
</script>
</body>
</html>