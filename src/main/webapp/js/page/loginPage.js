/**
 * 专家端登录
 * Created by xiuxiu on 2016/7/13.
 */
require([
    'jquery',
    'h5/js/common/url',
    'h5/js/common/data'
], function($,URL, Data) {

    function init(){
        bindEvent()
    }


    function bindEvent(){
        $(body).on('click','.icheck',function(){
            $(this).find('label').toggleClass('active');
        }).on('click','#mysubmit',function(){
            var mysubmit= $('#mysubmit'),
                password= $('#password'),
                username=$('#username'),
                vusername =  username.val().trim(),
                vpassword = password.val().trim();
                if(!(vusername && vpassword))
                {
                    bainx.broadcast('用户名和密码不能为空');
                    return false;
                }


        })
    }



    init();
})