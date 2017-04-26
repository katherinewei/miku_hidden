/**
 * Created by xiuxiu on 2016/11/21.
 */

bindEvents();

function showTips(msg){
    $('body').append('<div class="tbtx-broadcast" >'+msg+'</div>');
    setTimeout(function(){
        $('.tbtx-broadcast').remove();
    },3000)
}

//ajax请求
function jsAjax(data,url,callbackSuccess,failmsg){
    $.ajax({
        data:data,
        type : 'POST',
        //dataType:'jsonp',
        dataType:'json',
        url:url,
        //jsonpCallback: 'Callback',
        success:function(res){
            callbackSuccess(res.result);
        },
        error:function(res){
           // showTips(failmsg);
            callbackFail(res);
        }
    })
}

function bindEvents(){
    $('body')
        .on('click','.imgVerCode img',function(){
            $(this).attr('src','/hiden/api/m/1.0/getMobileVerificationCode.json?r='+Math.random());
        })
        
        .on('keyup', 'input', function(event){
            $('.tips').hide();
        })
        .on('click', '#mysubmit', function(event){
            event.preventDefault();
            submit($(this));
        })
        .on('click', '.icheck', function(event){
                $(this).find('label').toggleClass('active');
        })

}



function submit( btn){
    var mobile = $('#username'),
        vercode = $('#picCode'),
        psd =  $('#password'),
        psd2 =  $('#password2'),
        tips = $('.tips'),
        data = {
            mobile: $.trim(mobile.val()),
            checkNO: $.trim(vercode.val()),
            password: $.trim(psd.val()),
        };
    tips.show();
    if (!data.mobile) {
       
        tips.text(mobile.attr('placeholder'));
        return;
    } else if (!/^[\d]{11}$/gi.test(data.mobile)) {
       
        tips.text('请输入正确的手机号码！');

        return;
    }
    if (!data.password) {
        tips.text(psd.attr('placeholder'));
        return
    }
    else if(data.password != $.trim(psd2.val())){
        tips.text('两次输入的密码不一致');
        return
    }

    if (!data.checkNO) {
        tips.text(vercode.attr('placeholder'));
        //bainx.broadcast(vercode.attr('placeholder'));
        return;
    } else if (data.checkNO.length !== 4) {
        tips.text('请输入4位数字验证码');
        //bainx.broadcast('请输入4位数字验证码');
        return;
    }
    if(!$('.icheck').find('label').hasClass('active')){
    	 tips.text('请同意对应的协议');
        return;
    }
    tips.hide();
    btn.addClass('disable').text('用户注册中.');
    
    var done = function(res){
    	// switch(res.flag){
    	
    	// case '1':
    	// 	showTips('注册成功！');
    		
    	// 	setTimeout(function(){
    	// 		location.href = '/hiden/pc/h/1.0/login.htm'
    	// 	},2000)
    		
    	// 	break;
    	// case '0':
    	// case '2':   		
    	// case '3':
    		
    	// 	tips.text(res.msg).show();
    	// 	btn.removeClass('disable').text('注册');
    	// 	break;
    	// default:
    	// 	break;
    	// }
        showTips(res.msg);
        btn.removeClass('disable').text('用户注册');
    },
    fail = function(res){
    	showTips('请求失败');
    }
    
    
    
    jsAjax(data,'/hiden/api/m/1.0/deviceRegister.json',done,fail)
    
}