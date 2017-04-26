/**
 *
 * Created by xiuxiu on 2016/11/21.
 */
var  Url= '',
//Url= '/hiden/pc/h/1.0/',
    imgPath = '/hiden/images/',
//Url= 'http://192.168.1.250:8080/hiden/pc/h/1.0/',
//imgPath = '../images/',
    profileId = $('body').data('id'),
    emojiU = [];
var emojiD = emoji(),
    emojiUcode={},
 emojiUc = {};


$.each(emojiD,function(i,item){
    var ucode = item.classN.replace("emoji","");
    emojiU.push(item.classN);
    emojiUc[item.wechat]=item.classN;

})
emojiUcode.map = emojiUc;

//表情转化
var parseEmoji=function (msg) {
    $.each(emojiUcode.map,function(i,face){
        if (emojiUcode.map.hasOwnProperty(i)) {
           // console.log(emojiUcode.map[i]);
            while (msg.indexOf(i) > -1) {
                msg = msg.replace(i,  '<span class="emoji '+face+'"></span>');
            }
        }
    })
        //for (var face in emojiUcode.map) {
        //
        //}
        return msg;
};

init();

function init(){
    //getAddrList(0);
    getAllTagEnd(0);
    bindEvent();

    //是否显示注册
    var _state  = $('body').attr('status');
    if(_state == '2'){
        $('.leftNav ul').append('<li id="registerUser" class="registerUser" ><i></i><p>注册用户</p></li>');

    }
}


function showTips(msg){

    if( $('.tbtx-broadcast').length == 1){
        $('.tbtx-broadcast').remove();
    }
    $('body').append('<div class="tbtx-broadcast" >'+msg+'</div>');
    setTimeout(function(){
        $('.tbtx-broadcast').remove();
    },3000)
}

//ajax请求
function jsAjax(data,url,callbackSuccess){
    $.ajax({
        data:data,
        type : 'POST',
        //dataType:'jsonp',
        dataType:'json',
        url:url,
        //jsonpCallback: 'Callback',
        success:function(res){
            if(res.code == 6){
                showTips('程序报错，请联系后台管理员！');
            }
            if(res.code == 1){
                showTips('登录超时！请重新登录');
                location.href=Url+'login.htm';
            }
            if(res.status == 0){
                //showTips();
                location.href=Url+'login.htm';
                return
            }
            callbackSuccess(res.result);
        },
        error:function(res){
            var code = JSON.parse(res.responseText);
            code = code.code;
            if(code == 6){
                showTips('程序报错，请联系后台管理员！');
            }
            if(code == 1){
                showTips('登录超时！请重新登录');
                location.href=Url+'login.htm';
            }

            //callbackFail(res);
        }
    })
}


//显示全部用户
function  getAllTagEnd(pageNo){
    var pz = 34,
        done = function(res){
            var list = res.data;

            var
                html=[];
            $.each(list,function(index,item){
                var autoTxt = item.autoStatus ? '已' : '未',
                    addCTxt = item.contactFlag ? '' : '不',
                    isAuto = item.autoStatus ? 'active' : '',
                    isService = item.isService == 1 ? 'active' : '',
                    isAutoChat = item.autoTalk == 1 ? 'active' : '',
                    autoChatTxt = item.autoTalk == 1 ? '已' : '未',
                    contactFlag = item.contactFlag == 1 ? 'active' : '',
                    wxname = item.wxLemonName ? item.wxLemonName : '';
                html.push('<li class="row userItem" data-userid="'+item.userId+'" data-id="'+item.id+'" data-wxNum="'+item.wxnum+'" data-wxopendid="'+item.wxOpendId+'" data-wxid="'+item.wxid+'"><div class="fb fvc fac imgDiv"><img src="'+item.wxPic+'"/></div><div class="col col-15 txtDiv"><p class="name ellipsis">昵称:<span>'+wxname+'</span></p><p class="ellipsis">手机版本：<span>'+item.deviceName+'</span></p><p class="ellipsis">手机IMEI：<span>'+item.deviceInfo+'</span></p><p>联系信息：<span>'+item.wxNum+'</span></p><div class="iconGroup "><span title="'+autoTxt+'开启自动回复" class="autoReplyLit '+isAuto+'"></span><span class="addInContactListLit '+contactFlag+'" title="'+addCTxt+'可添加到通讯录"></span><span title="'+autoChatTxt+'开启自动互聊" class="autoChat '+isAutoChat+'"></span><!--<span class="after-saleServiceLit '+isService+'"></span>--></div> </div><span class="deleteOneDivc" ></span> </li>');
            })
            $('.wrapper').html('<div class="title row"><div class="col col-5 h3">全部设备</div><div class="col col-15 fb far fvc"><div class="allFunction"><div class="cancelGroup"><span class="cancelAutoReplyB active" data-status="1">恢复自动回复</span><span class="addInContactListB active" data-status="1">可添加到通讯录</span><span class="cancelChatB active" data-status="1">恢复自动互聊</span><!--<span class="cancelServiceB active">恢复售后服务</span>--></div><span class="getUserListMsgBtn functionBtn">获取微信用户列表信息</span> <span class="sendCircleFriendsBtn functionBtn">发朋友圈</span><span class="autoReplyBtn functionBtn">自动回复</span><span class="after-saleServiceBtn functionBtn">售后服务</span></div> </div> </div><div class="content"><ul class="allUserList"><li class="allUserBtn row"><div class="fb fvc fac imgDiv"><img src="'+imgPath+'icon_weixin.png"/></div><div class="col col-15 fb fvc"><span>全部微信</span></div> </li>'+html.join('')+'</ul></div>');

            pagina(res.totalSize,pageNo,pz,'pgDeviceBtn','','');

        }
    var data = {
        profileId:profileId,
        pageSize:pz,
        pageNo:pageNo
    }


    jsAjax(data,Url+'getdeviceListByUserId.json',done,'请求失败！');

}

//显示任务
function getTask(status,title,pageNo){

    var pz = 10;

    var data = {
            userId:profileId,
            pageNo:pageNo,
            status:status,
            pageSize:pz,
        },
        done = function(res){
            console.log(res);

            var list = res.list,hmtl=[];

            if(list.length > 0){
                $.each(list,function(i,item){
                    var job = item.job,
                        ilist= item.list,
                        successTpl = '',
                        failTpl = '',
                        unstartTpl = '',
                        //wxLemonName=item.dNames.split(';'),
                       // wxPic = item.wxPic ? item.wxPic.split(';') : [],
                        taskPic=job.taskPic ? job.taskPic.split(';') : [],
                        comments = job.comment ? job.comment.split('#') : [],
                        commentsTpl = '',
                        commentsTplC = '',
                        hide = '',more='',
                        addr =  job.address ? '<p class="taskAddr">发布地址：<span class="addr">'+job.address+'</span></p>' : ''; //地址
                    //执行状态统计
                    var  devicesHtml = [],
                        successList = [],failList = [],unStartList = [],taskIdsItem=[];

                    $.each(ilist,function(j,iitem){
                        if(j >= 5){
                            hide = 'style="display:none"';
                             more = '<span class="more">》》</span>';
                        }
                        var tpl = '<dd class="taskItem" '+hide+'data-id="'+iitem.taskId+'"><img src="'+iitem.wxPic+'"/><p class="wxName ellipsis">'+iitem.wxLemonName+'</p><p class="wxDeviceInfo">'+iitem.deviceInfo+'</p> </dd>';
                        switch (iitem.deviceStatus){
                            case -1://失败
                                failList.push(tpl);
                                break;
                            case 0: //未开始
                                unStartList.push(tpl);
                                break;
                            case 1://成功
                                successList.push(tpl);
                                break;
                        }
                        taskIdsItem.push(iitem.taskId);
                    })
                    taskIdsItem = taskIdsItem.join(';');



                    //微信第一个人的头像
                    //wxPic = wxPic[0];
                    ////微信名
                    //if(wxLemonName.length > 3){
                    //    // hide = 'hide';
                    //    var newwxLemonName = [],
                    //        b;
                    //    wxLemonName.forEach(function(item, index) {
                    //        var a = Math.floor(index / 3);
                    //        if (b !== a) {
                    //            b = a;
                    //            newwxLemonName[a] = [];
                    //        }
                    //        newwxLemonName[a].push(item);
                    //    });
                    //    var otherName = '';
                    //    $.each(newwxLemonName,function(i,item){
                    //        if(i>0){
                    //            otherName +='、'+item.join('、');
                    //        }
                    //    })
                    //    wxLemonName = newwxLemonName[0].join('、') + '<span class="otherName hide">'+otherName+'等'+wxLemonName.length+'人</span>';
                    //}else{
                    //    wxLemonName = wxLemonName.join('、');
                    //    hide = 'hide';
                    //}
                    //任务图片
                    var pics = [];
                    $.each(taskPic,function(j,pic){
                        pics.push('<img src="'+pic+'"/>');
                    })

                    //朋友圈的评论
                    if(comments.length > 0){
                        var commentsHtml = [],commentsHtmlC = [];
                        $.each(comments,function(j,comment){
                           // var $valTxt = ;
                            commentsHtml.push('<p>'+parseEmoji(comment)+'</p>');
                            commentsHtmlC.push('<p>'+comment+'</p>');
                        })
                        commentsTpl = '<div class="commentListTask"><h4>评论内容：</h4><div class="commentListTaskContent"> '+commentsHtml.join('') +'</div></div>' ;
                        commentsTplC = '<div class="commentListTaskCode hide"><div class="commentListTaskContent"> '+commentsHtmlC.join('') +'</div></div>' ;
                    }



                    //执行状态
                    var statusTxt = '',color='',isHide= '',hideDel = '';
                    switch (job.deviceStatus){
                        case -1://失败
                            statusTxt = '执行失败';
                            break;
                        case 0://未开始
                            statusTxt = '未开始';
                            isHide = 'hide';
                            break;
                        case 1://成功
                            statusTxt = '执行成功';
                            isHide = 'hide';
                            color = 'gray';
                            break;
                        case 2://异常
                            statusTxt = '执行异常';
                            break;
                        case -2://删除
                            statusTxt = '已删除';
                            isHide = 'hide';
                            hideDel = 'hide';
                            break;
                        default:
                            break;
                    }
                    //时间
                    var time=formatDate(new Date(job.lastUpdated)),
                        value = job.taskValue ? parseEmoji(job.taskValue) : '',
                        valueC = job.taskValue ? job.taskValue : '',
                        startTime = job.startTime ? '<span class="time ">设置时间：<span class="settingTime">'+formatDate(new Date(job.lastUpdated)) +'</span></span>': '',
                        executeTime = ilist.deviceStatus != 0 && ilist.startTime ? '<span class="time">执行时间：'+formatDate(new Date(item.lastUpdated)) + '</span>': '',
                        canEditTpl = job.type == 2 && unStartList.length > 0 ? '<span class="editTask ">编辑</span>': '';
                    if(failList.length > 0){
                        failTpl = '<div class="executeStatus execuFail"><p class="excuTitle">执行失败：'+executeTime+'<span class="deleteTask '+hideDel+'">删除</span><span class="startTask">立即执行</span></p><dl class="devicePerson">'+failList.join('')+'</dl>'+more+'</div>';
                    }
                    if(unStartList.length > 0){
                        unstartTpl = '<div class="executeStatus execuUnstart"><p class="excuTitle">尚未开始：<span class="deleteTask '+hideDel+'">删除</span></p><dl class="devicePerson">'+unStartList.join('')+'</dl>'+more+'</div>';
                    }

                    if(successList.length > 0){
                        successTpl = '<div class="executeStatus execuSuccess"><p class="excuTitle">执行成功：'+executeTime+'<span class="deleteTask '+hideDel+'">删除</span></p><dl class="devicePerson">'+successList.join('')+'</dl>'+more+'<p class="btnGroupOper"></div>';
                    }

                    hmtl.push('<li class="row" data-id="'+taskIdsItem+'" data-jobid="'+job.id+'"><div class="taskHeadPic"><b class="checkBox selected"></b></div><div class="col col-15 taskContent">'+successTpl+failTpl+unstartTpl+'<p class="sendType" data-type="'+job.type+'">任务类型：'+job.taskName+'</p><p class="detCon">任务内容：'+value+'</p><p  class="detConCode hide">'+valueC+'</p><dl class="listThumb"><dd>'+pics.join('')+'</dd></dl>'+addr+commentsTpl+commentsTplC+'<div class="itemFooter"><span class="time createTime">'+time+'</span>'+canEditTpl+startTime+executeTime+'</div> </div> </li>');
                })
                $('.wrapper').html('<div class="title row"><div class="col col-5 h3">'+title+'任务</div><div class="col col-15 fb far fvc"><div class="allFunction"><label><b class="checkBox selected allSelectd"></b>全选</label><span class="deleteBtn deleteAllBtn">删除</span></div> </div> </div> <div class="content"> <ul class="TaskList">'+hmtl.join('')+' </ul></div>');
                pagina(res.totalsize,pageNo,pz,'pgTaskBtn',status,title);
            }
            else{
                $('.wrapper').html('<div class="noTask">暂无'+title+'任务！</div>');
            }
        }

    jsAjax(data,Url+'selectfinalJobList.json',done,'');

}

//表情
function showEmoji(obj){
    //var top=obj[0].offsetTop,
    //    left = obj[0].offsetLeft;
    if(obj.find('.dialogEmoji').length == 0){
        var html=[];
        $.each(emojiD,function(i,item){
            //var ucode = item.classN.replace('emoji','');
            html.push('<dd data-wechat="'+item.wechat+'" data-code = "'+item.classN+'" class="emoji '+item.classN+'"></dd>');
        })
        obj.append('<div class="dialogEmoji"><dl>'+html.join('')+'</dl></div>');
    }
    else{
        obj.find('.dialogEmoji').show();
    }


}

//分页
function pagina(totalsize,pageNo,pz,typeClass,status,title){
    //页码
    var pgHtml = [],
        pgTpl;
    if(totalsize > pz) {

        for (var i = 1; i < totalsize + 1; i++) {
            if (i % pz == 1) {
                var pg = Math.ceil(i / pz);

                if (pg == pageNo + 1) {
                    pgTpl = '<span class="currentStep">' + pg + '</span>';
                } else {
                    pgTpl = '<a  class="step '+typeClass+'" data-pg="' + (pg - 1) + '" data-txt="' + title + '" data-status="' + status + '">' + pg + '</a>';
                }

                pgHtml.push(pgTpl);
            }
        }

        $('.content').append('<div class="pagination pull-right"> <a class="prevLink '+typeClass+'" data-status="'+status+'" data-txt="'+title+'" >上一页</a>'+pgHtml.join('')+'<a class="nextLink '+typeClass+'" data-status="'+status+'" data-txt="'+title+'" >下一页</a> </div>');

        //if(totalsize < pz){
        //    $('.nextLink,.prevLink').css('display','none');
        //    return
        //}

        //上 && 下一页
        //第一页隐藏上一页
        if(pageNo == 0){
            $('.prevLink').hide();
            $('.nextLink').attr('data-pg',pageNo+1);
            if(totalsize <= pz){
                $('.nextLink').hide();
            }

        }
        //最后一页隐藏下一页   后面的是刚好整条的判断
        else if(Math.floor(totalsize / pz) == pageNo || (totalsize % pz == 0 && Math.floor(totalsize / pz) - 1 == pageNo)){
            $('.nextLink').hide();
            $('.prevLink').attr('data-pg',pageNo-1);
        }
        else{
            $('.prevLink').attr('data-pg',pageNo-1);
            $('.nextLink').attr('data-pg',pageNo+1);
            $('.nextLink,.prevLink').css('display','inline-block');
        }
    }
}

function isShowCancelBtnItem(){
    var allSelectUser = $('.allUserList').find('.userItem.active'),
        cancelGroup = $('.cancelGroup');
    cancelGroup.addClass('show');
    //分为4种情况，1、至少有一个是有自动回复的，2、至少有一个是有售后服务的；3、没有一个是有自动回复的；4、没有一个是有售后服务的
    var isCancelAuto = false,isCancelSer = false,isCancelAdd = false,isCancelChat=false;

    allSelectUser.each(function(){
        if($(this).find('.autoReplyLit').hasClass('active')){
            isCancelAuto = true;
        }
        if($(this).find('.autoChat').hasClass('active')){
            isCancelChat = true;
        }
        if($(this).find('.addInContactListLit').hasClass('active')){
            isCancelAdd = true;
        }

        //判断完成跳出循环
        if(isCancelAuto && isCancelAdd){
            return false
        }
    })
    if(isCancelAuto){
        $('.cancelAutoReplyB').text('取消自动回复').attr('data-status',0);
    }
    else{
        $('.cancelAutoReplyB').text('恢复自动回复').attr('data-status',1);
    }
    if(isCancelAdd){
        $('.addInContactListB').text('不可添加到通讯录').attr('data-status',0);
    }
    else{
        $('.addInContactListB').text('可添加到通讯录').attr('data-status',1);
    }
    if(isCancelChat){
        $('.cancelChatB').text('取消自动互聊').attr('data-status',0);
    }
    else{
        $('.cancelChatB').text('恢复自动互聊').attr('data-status',1);
    }
    //if(isCancelSer){
    //    $('.cancelAutoReplyB').text('取消售后服务');
    //}
}

//是否显示自动回复或售后服务 或修改为为恢复
function isShowCancelBtn($this){
    var allSelectUser = $('.allUserList').find('.userItem.active'),
        cancelGroup = $('.cancelGroup');
    if($this.hasClass('active')){
        isShowCancelBtnItem();
    }
    else{
        if(allSelectUser.length  == 0){  //一个都没选
            cancelGroup.removeClass('show');
        }
        else{
            isShowCancelBtnItem();
        }
    }
}

function bindEvent(){
    $('body')

        .on('click','.downloadAPPBtn',function(){
            if($('.downloadAPP').length == 0){
                $('body').append('<div class="downloadAPP"><div class="con"><img src="'+imgPath+'ewm.jpg" /><p>该APP仅支持安卓手机及微信6.3.30以上</p></div>  </div>');
            }
            else{
                $('.downloadAPP').show();
            }
        })
        .on('click','.downloadAPP',function(){
            $('.downloadAPP').hide();
        })
        .on('click','.allUserBtn',function(){
            var $this = $(this);
            var allUser = $('.allUserList').find('li');
            if($this.hasClass('active')){
                allUser.removeClass('active').find('.deleteOneDivc').hide();
            }
            else{
                allUser.addClass('active').find('.deleteOneDivc').show();
            }
            isShowCancelBtn($this);
        })
        .on('click','.userItem',function(){
            var $this = $(this);
            $this.toggleClass('active');
            if(!$this.hasClass('active')){
                $this.find('.deleteOneDivc').hide();
                $('.allUserBtn').removeClass('active');
            }
            else{
                $this.find('.deleteOneDivc').show();
            }
            isShowCancelBtn($this);
        })
        .on('click','.leftNav li',function(){
            var target = $(this),
                id = target.attr('id');
            target.addClass('show').siblings().removeClass('show');
            switch (id){
                case 'allUser':
                    getAllTagEnd(0);
                    break;
                case 'allTask':
                    getTask('','全部',0);
                    break;
                case 'unTask':
                    getTask(0,'未被执行',0);
                    break;
                //case 'TaskError':
                //    getTask(2,'执行异常',0);
                //    break;
                case 'TaskFail':
                    getTask(-1,'执行失败',0);
                    break;
                case 'TaskSuccess':
                    getTask(1,'执行成功',0);
                    break;
                case 'allTagEnd':
                    getAllUser(0);
                    break;
                case 'addressList':
                    getAddrList(0);
                    break;
                case 'registerUser':
                    $('.wrapper').html('<div class="title row"><div class=" h3">注册用户</div></div><div class="content"><iframe src="/hiden/pc/h/1.0/register.htm"></iframe></div>');
                    break;    

            }

        })


        .on('click','.functionBtn',function(){
            var selectUser = $('.allUserList').find('.userItem.active'),that = $(this);
            if(selectUser.length > 0){

                var done = function(res){
                        if(res.flag == 1){
                            //售后服务
                            if(that.hasClass('after-saleServiceBtn')){
                                if($('.saleService').length > 0){
                                    $('.saleService').show();
                                }else{
                                    $('body').append('<section class="telDialog wl-trans-dialog translate-viewport saleService" data-widget-cid="widget-0" style="display: block;"><div class="IframeBoxContent grid"><h3>售后服务</h3><div class="dialogContent"><div class="row"><div class="dialogLeft"> <label>编辑售后服务</label></div><div class="dialogRight col"> <textarea id="serviceContent"></textarea></div></div> </div><div class="dialogFooter"><div class="btnGroup"><span class="full submitBtn dialogBtn">确定</span><span class="cancelBtn dialogBtn">取消</span></div> </div> </div></section>');
                                }

                            }
                            //自动回复
                            if(that.hasClass('autoReplyBtn')){


                                $('body').append('<section class="telDialog wl-trans-dialog translate-viewport autoReply" data-widget-cid="widget-0" style="display: block;"><div class="IframeBoxContent grid"><h3>自动回复</h3><div class="dialogContent"><div class="row"><div class="dialogLeft"> <label><span class="txt_reply">添加</span>关键字/词/语句</label></div><div class="dialogRight col"> <input type="text" id="keyWord" /></div></div><div class="row"><div class="dialogLeft"> <label><span class="txt_reply">添加</span>自动回复语句</label></div><div class="dialogRight col"> <span  class="containEmoji"><textarea id="replyContent" class="inputVal"></textarea><input type="hidden" class="CValH" /><i class="emojiBtn"></i></span></div></div><!--<div class="addNextBtn">点击添加下一个>> </div>--><dl class="replyList"></dl> </div><div class="dialogFooter"><div class="btnGroup"><span class="full submitBtn dialogBtn">确定</span><span class="cancelBtn dialogBtn">取消</span></div> </div> </div></section>');

                                getStatement(0);


                            }
                            //朋友圈
                            if(that.hasClass('sendCircleFriendsBtn')){
                                //获取已选用户
                                var html=[];
                                selectUser.each(function(i){

                                    var hide = i == 0 ? 'theFirst' : 'hide other',
                                        pic = $(this).find('img').attr('src'),
                                        name = $(this).find('.name span').text();
                                    html.push('<dd class="'+hide+'"><img src="'+pic+'"/><span class="name ellipsis">'+name+'</span> </dd>');
                                })

                                addOrEditFriendTask('',false,html,selectUser)
                            }

                            //获取微信用户列表信息
                            if(that.hasClass('getUserListMsgBtn')){
                                //获取选中用户
                                var ids = [];
                                $('.userItem.active').each(function(){
                                    ids.push($(this).data('id'));
                                })
                                ids = ids.join(';');
                                addTask(ids,1);
                            }
                        }
                    else{
                            showTips('登录超时！请重新登录');
                            setTimeout(function(){
                                location.href=Url+'login.htm';
                            },2000)
                        }
                    }

                jsAjax('',Url+'enableUserId.json',done,'');

            }
            else{
                showTips('请选择微信用户！');
            }

        })

        //查看更多自动回复
        .on('click','.viewMore',function(){
            var pg = parseInt($(this).attr('data-pg')) + 1;
            getStatement(pg);
        })

        //编辑&&删除自动回复
        .on('click','.opateBtn',function(){
            var parent = $(this).parents('dd');
            $('.replyList dd').removeClass('curReply');
            parent.addClass('curReply');
            //编辑
            if($(this).hasClass('editBtn')){
                $('.txt_reply').text('编辑');
                $('#keyWord').val(parent.find('.kw-item').text());
                $('#replyContent').val(parent.find('.reply-item').text());
                //flag = 1
            }
            //删除
            else{
                editOrDeleteStatment(0);
            }

        })

        //未开始的任务可编辑
        .on('click','.editTask',function(){

            var parentLi = $(this).parents('li'),
                parent = $(this).parents('.col'),
                imgs = [],
                content = parent.find('.detConCode').text();
            parent.find('.listThumb').find('img').each(function(){
                imgs.push($(this).attr('src'));
            })
            parentLi.addClass('curEditItem').siblings().removeClass('curEditItem');
            var data = {
                content:content,
                imgs:imgs.join(';'),
                id:parentLi.data('jobid')
            };
            var commentL =  parent.find('.commentListTaskCode').find('p'),comments=[];
            if(commentL.length > 0){
                commentL.each(function(){
                    comments.push($(this).html());
                })
                data.comment = comments.join('#');
            }
            var addr = parent.find('.addr').text();
            if(addr){
                data.address = addr;
            }

            if(parent.find('.settingTime').length > 0 && !parent.find('.settingTime').parent().is(':hidden')){
                var time = parent.find('.settingTime').text();
                data.time = time;
            }

            addOrEditFriendTask(data,true,'');
        })

        //确定操作
        .on('click','.dialogBtn',function(){
            var done,failed,data = {},ids = [];
            //获取选中用户
            $('.userItem.active').each(function(){
                ids.push($(this).data('id'));
            })
            ids = ids.join(';');

            if($(this).parents('.saleService').length > 0){         //售后服务
                if($(this).hasClass('submitBtn')) {
                    addTask(ids,3,$('#serviceContent').val(),$('.saleService'))
                }
                else{
                    $('.saleService,.dialogEmoji').hide();

                }
            }

            if($(this).parents('.autoReply').length > 0){       //自动回复

                if($(this).hasClass('submitBtn')){
                    var　kw = $('#keyWord').val(),
                        content = $('#replyContent').val();

                    if(!kw){
                        showTips('请填写关键字');
                        return false
                    }
                    if(!content){
                        showTips('请填写回复内容');
                        return false
                    }

                    //编辑
                    if($('.curReply').length > 0){
                        editOrDeleteStatment(1);
                    }

                    //var
                    //    str = '{'+$('#keyWord').val()+':"'+$('#replyContent').val()+'"}',
                    //    con = eval("("+str+")");
                    //
                    //console.log(con,ids);
                    else{
                        done = function(res){
                            console.log(res);
                            showTips('添加成功！');
                            var item = res.data;
                            $('#keyWord,#replyContent').val('');
                            $('.replyList').prepend('<dd class="row" data-id="'+item.ids+'"><div class="dialogLeft"><span class="kw-item">'+item.keyword+'</span></div><div class="dialogRight col"><p class="ellipsis words reply-item">'+parseEmoji(item.replyContent)+'</p><p class="words reply-item hide">'+item.replyContent+'</p><span class="opateBtn editBtn"></span><span class="opateBtn deleteBtn"></span></div></dd>')

                            //$('.autoReply').remove();
                        },

                            data = {
                                keyword:kw,
                                content:content,
                                profileId:profileId,
                                deviceIds:ids
                            }
                        jsAjax(data,Url+'insertManyProfileStatemts.json',done,'');
                    }


                }
                else{
                    $('.autoReply').remove();
                    getAllTagEnd(0);
                }


            }
            if($(this).parents('.sendCircleFriends').length > 0){           //朋友圈
                if($(this).hasClass('submitBtn')) {

                    var obj = $('.sendCircleFriends')

                    if($(this).parents('.sendCircleFriends').hasClass('eidtFriendTask')){
                        obj = $('.eidtFriendTask')
                    }

                    addTask(ids,2,$('#friendContent').val(),obj);



                }
                else{
                    $('.sendCircleFriends').remove();

                }
                $('.datepicker').remove();
                $('.dialogEmoji').hide();

            }

        })
        //添加图片
        .on('change','.file',function(){
            var picTar = $('.uploadPicDiv .active img');
            if(picTar.length >= 9){
                showTips('最多只能上传9张图片');
                return false;
            }
            uploadImages('#my_form', Url+'upYunUploadPics.json',this).done(function(res) {
                //alert('上传图片成功');
                if(res.status == 0){
                    showTips('上传图片失败！');
                    //删除预览图
                    //$('#my_form').find('.active').each(function(){
                    //    var imgSrc = $(this).find('img').attr('src');
                    //    if(imgSrc.indexOf('data:image') > -1){
                    //        $(this).remove();
                    //    }
                    //})
                }
                else{
                    showTips('上传图片成功！');
                    var picUrls = res.result.picUrls,
                        imgListUrl = [];
                    picUrls = picUrls.split(';');
                    //删除预览图
                    //$('#my_form').find('.active').each(function(){
                    //    var imgSrc = $(this).find('img').attr('src');
                    //    if(imgSrc.indexOf('data:image') > -1){
                    //        $(this).remove();
                    //    }
                    //})
                    $.each(picUrls,function(index,item){
                        if(index >= 9){
                            return false
                        }
                        imgListUrl.push('<dd class="active"><div class="file-panel"><span class="deleteImg cancel">删除</span><span class="rotateLeft">向左移动</span><span class="rotateRight">向右移动</span></div><img src="'+ item+'"  alt=""><span class=" success"></span></dd>');
                    })
                    $('.addPic').before(imgListUrl.join(''));
                    //$('.addPic').prev().find('img').attr('src',res.result.picUrls)//设置上传后的路径
                }


            }).fail(function() {
                $(this).val('');
                alert('上传图片失败！');
            });
        })

        //添加评论
        .on('click','.addComment',function(){
            var $val = $.trim($('#commentVal').val());

            if(!$val){
                showTips('请输入评论内容');
                return false;
            }
            if($val.indexOf('#')>=0){
                showTips('评论内容不允许出现“#”');
                return false;
            }
            //var $valTxt = parseEmoji($val);
            $('.commentList').append('<div class="commentItem"><span contenteditable="true">'+$val+'</span><i title="删除" class="deleteComment">×</i></div>');
            //$('.commentListHide').append('<div class="commentItemHide"><span contenteditable="true">'+$val+'</span><i title="删除" class="deleteComment">×</i></div>');
            $('#commentVal').val('');
        })
        //删除评论
        .on('click','.deleteComment',function(){
            $(this).parent().remove();
        })
        //删除图片
        .on('click','.deleteImg',function(){
            var thatP =  $(this).parents('dd'),
                data={
                    filePath:thatP.find('img').attr('src')
            },
                done = function(){
                    thatP.remove();
                }
            jsAjax(data,Url+'upyunDeleteFile.json',done,'删除失败')
        })

        //图片向左移动
        .on('click','.rotateRight',function(){

            var parent = $(this).parents('dd');
            if(parent.prev().length == 0){
                return
            }
            parent.prev().before(parent.clone());
            parent.remove();
        })
        //图片向右移动
        .on('click','.rotateLeft',function(){
            var parent = $(this).parents('dd');
            if(parent.next().hasClass('addPic')){
                return
            }
            parent.next().after(parent.clone());
            parent.remove();

        })
        //全选
        .on('click','.checkBox,.allSelectd',function(){
            $(this).toggleClass('selected');
            if($(this).hasClass('allSelectd')){
                $('.TaskList').find('.checkBox').toggleClass('selected');
            }
            //
        })

        //展开查看全部信息
        .on('click','.viewBtnS',function(){
            //发朋友圈的展开
            var target = $(this);
            if(target.hasClass('retract')){
                target.parents('.choseWX').find('.other').addClass('hide');
                target.text('展开查看全部').addClass('viewAllSelectd').removeClass('retract').prev().show();
                return false;
            }
            if(target.hasClass('viewAllSelectd')){
                target.parents('.choseWX').find('.other').removeClass('hide');
                target.text('收起').addClass('retract').removeClass('viewAllSelectd').prev().hide();

            }
        })

        //退出
        //    .on('click','.logout',function(){
        //        location.href = 'login.html';
        //    })
        //任务展开人名
        .on('click','.more',function(){
            $(this).prev('.devicePerson').find('.taskItem').css('display','inline-block');
            $(this).addClass('hide');
        })

        //修改任务状态 立即执行（变为未开始）|| 删除
        .on('click','.deleteTask,.startTask,.deleteAllBtn',function(){
            var target = $(this),type = -2;
            if(target.hasClass('startTask')){
                type = 0;
            }

            if(target.hasClass('deleteAllBtn')){
                if($('.TaskList').find('.checkBox.selected').length == 0){
                    showTips('请选择要删除的任务！');
                    return false;
                }

            }
            changeTaskStatus(type,target)


        })


        //取消||恢复 自动回复  || 添加到通讯录  ||自动互聊
        .on('click','.cancelAutoReplyB,.addInContactListB,.cancelChatB  ',function(){

            var $this = $(this),
                status = $this.attr('data-status'),
                data = {},
                isAutoR = $this.hasClass('cancelAutoReplyB'),//自动回复
                isAutoC =  $this.hasClass('cancelChatB'),//互聊
                isAddPhone = $this.hasClass('addInContactListB');//添加通讯录


            if(isAutoR){
                    data.autoStatus = status;
            }
            else if(isAutoC){
                data.talkStatus = status;
            }
            else{
                data.status = status;
            }
            var done = function(res){
                    if(res.status != -1 && isAutoR){
                        showTips('操作失败！');
                        return false
                    }
                    //if(res.status != -1){
                        showTips('设置成功！');
                        var txt,txt2;
                        var funTxt = '互聊',funObj ='autoChat';
                        if(isAutoR){
                            funTxt = '回复';
                            funObj ='autoReplyLit';
                        }
                        if(status == 1){
                            if(!isAddPhone){
                                txt = '取消自动'+funTxt;
                                txt2 = '已开启自动'+funTxt;
                                $('.userItem.active').find('.'+funObj).addClass('active')
                            }
                            else{
                                txt = '不可添加到通讯录';
                                txt2 = txt;
                                $('.userItem.active').find('.addInContactListLit').addClass('active')
                            }

                            $this.attr({'data-status':0,'title':'未开启自动回复'});

                        }
                        else{
                            if(!isAddPhone){
                                txt = '回复自动'+funTxt;
                                txt2 = '未开启自动'+funTxt;
                                $('.userItem.active').find('.'+funObj).removeClass('active')
                            }
                            else{
                                txt = '可添加到通讯录';
                                txt2 = txt;
                                $('.userItem.active').find('.addInContactListLit').removeClass('active')
                            }
                            $this.attr('data-status',1);
                        }
                        $this.text(txt).attr({'title':txt2});
                   // }
                   // else{
                       // showTips('操作失败！');
                   // }


                    //$('.userItem.active').each(function(){
                    //    var autolit = $(this).find('.autoReplyLit'),
                    //        addContact = $(this).find('.addInContactListLit');
                    //    if(autolit.hasClass('active')){
                    //        autolit.removeClass('active').attr('title','未开启自动回复');
                    //
                    //    }else {
                    //        autolit.addClass('active').attr('title','已开启自动回复');
                    //    }
                    //    if(addContact.hasClass('active')){
                    //        addContact.removeClass('active').attr('title','不可添加到通讯录');
                    //
                    //    }else {
                    //        addContact.addClass('active').attr('title','可添加到通讯录');
                    //    }
                    //})
                }
            //,
            //failed = function(){};

            //if($('.allUserBtn').hasClass('active')){        //选择全部微信
            //
            //    data.profileId=profileId;
            //
            //
            //    jsAjax(data,Url+'changePersonAuto.json',done,'');
            //}
            //else{                           //部分微信
                var ids = [];
                $('.userItem.active').each(function(){
                    //if($(this).find('.autoReplyLit').hasClass('active')){
                    ids.push($(this).data('id'));
                    //  }
                })
                ids = ids.join(';');
                data.deviceIds=ids;
                console.log(data);
                var urlX = isAutoR ? 'changeDeviceRelyAuto.json' : 'changeDevicePhoneFlag.json';
                jsAjax(data,urlX,done,'操作失败');
          //  }
        })

        //任务 分页
        .on('click','.pgTaskBtn',function(){
            var status = $(this).attr('data-status'),
                txt = $(this).attr('data-txt'),
                pg = parseInt($(this).attr('data-pg'));
            getTask(status,txt,pg);
        })

        //全部设备 || 全部用户 分页
        .on('click','.pgDeviceBtn,.pgUserBtn,.pgAddrListBtn',function(){
            var pg = parseInt($(this).attr('data-pg'));
            if($(this).hasClass('pgDeviceBtn')){
                getAllTagEnd(pg);
            }
            if($(this).hasClass('pgUserBtn')){
                getAllUser(pg);
            }
            if($(this).hasClass('pgAddrListBtn')){
                getAddrList(pg);
            }

        })

        //弹窗 点击空白去掉弹窗
        .on('click','.telDialog',function(){
            $(this).remove();
            getAllTagEnd(0);
        })
        //阻止触发父元素事件
        .on('click','.IframeBoxContent',function(e){
            e.stopPropagation();

        })

        //获取终端联系人列表
        .on('click','.tagEndItem',function(){
            var list = $(this).find('.contactList');
            if(list.children().length == 0 ){
                getUserContact($(this),0);
            }
            else if(list.is(':hidden')){
                list.show();
            }
            else{
                list.hide();
            }


        })

        //删除终端
        .on('click','.deleteOneDivc',function() {
            var $parent = $(this).parent(),
                id  = $parent.data('wxid'),
                done = function(res){
                    showTips('删除成功！');
                    $parent.remove();

                },
            //var failed = function(res){
            //    showTips('删除失败！');
            //}
                data = {
                    userId:profileId,
                    wxId:id
                }

            jsAjax(data,Url+'deleteOneDivcByWxId.json',done,'删除失败！');
        })
        //阻止触发父元素事件
        .on('click','.box',function(e){
            e.stopPropagation();
        })
        //表情
        .on('click','.emojiBtn',function(e){
            $('.dialogEmoji').hide();
            $(this).find('.dialogEmoji').show();
            //$(this).parent().find('.inputVal').focus();
            showEmoji($(this));

        })
        .on('click','.dialogEmoji .emoji',function(e){
            var $val,$valCode;
            //if($(this).parents('.containEmoji').find('#friendContent').length >0){
            //    $val = $('#friendContent');
            //    //$valCode = $('.FCValH');
            //}
            //if($(this).parents('.containEmoji').find('#commentVal').length >0){
            //    $val = $('#commentVal');
            //   // $valCode = $('.CValH');
            //}
            //if($(this).parents('.containEmoji').find('#commentVal').length >0){
            //    $val = $('#commentVal');
            //    // $valCode = $('.CValH');
            //}
            $val = $(this).parents('.containEmoji').find('.inputVal');

            var wechat=$(this).data('wechat');
           // $val.val($val.val()+wechat);

            //$val.val(insert_flg($val.val(),wechat));

            Button4_onclick($val,$val.val(),wechat)

            //$valCode.val($valCode.val()+$(this).data('code'));
        })
        .on('focus','input,textarea',function(e){
            $('.dialogEmoji').hide();
            $(this).parent().find('.containEmoji').show();
        })
        //.on('keyup','input,textarea',function(e){
        //   $(this).next().val($(this).val())
        //})
        .on('change','.file_excel',function(e){
            uploadImages('#excel_form', Url+'wxuploadExcel.json',this,true).done(function(res) {
                //alert('上传图片成功');
               showTips('导入成功！');
                $(this).val('');
                getAddrList(0);
            });
        })

    //修改对应的默认值内容
        .on('click','.defaultNameBtn',function(e){
            var $val = $.trim($('.defaultNameVal').val());
            if(!$val){
                showTips('请输入内容！');
                return
            }
            var data = {
                defaultName:$val
            },
            done = function(res){
                showTips('修改成功！');
                $('body').attr('defaultname',$val);
            };
            jsAjax(data,Url+'changeDefualtName.json',done,'');
        })


    //修改通讯录   =---====
        .on('click','.btnContact',function(e){
            var that = $(this);
                var status = 0,done,parent = that.parents('li'),phoneIds = parent.data('id');
                if(that.hasClass('deleteContact')){//删除
                    status = -1;
                    done = function(){
                        parent.remove();
                    }
                }
                if(that.hasClass('reRead')){//重新读取

                    done = function(){
                        that.remove();
                        parent.find('.isRead').text('否');
                    }
                }
                var data = {
                    phoneIds:phoneIds,
                    status:status
                }
                jsAjax(data,Url+'changePhoneListStatus.json',done,'操作失败')
        })

        .on('mouseover','.viewDetail',function(e){
            var that = $(this),parent = that.parents('li'),det = that.parent().find('.phoneDet');
            if(det.length == 0){
                var done = function(res){
                        that.parent().append('<p class="phoneDet showIt">'+res.data.fromWxName+'加'+res.data.wxName+'</p>');
                    },

                    phoneId = parent.data('id'),
                    data = {
                        phoneId:phoneId
                    };
                jsAjax(data,Url+'getPhoneDetailContent.json',done,'操作失败')
            }
            else{
                det.addClass('showIt');
            }

        })
        .on('mouseout','.viewDetail',function(e){
            $(this).parent().find('.phoneDet').removeClass('showIt');
        })


}

//记住光标的位置
function Button4_onclick(obj,oldStr,insertStr) {
    var oTxt1 = obj[0];
    var cursurPosition=-1;
    if(oTxt1.selectionStart){//非IE浏览器
        cursurPosition= oTxt1.selectionStart;
    }else{//IE
        var range = document.selection.createRange();
        range.moveStart("character",-oTxt1.value.length);
        cursurPosition=range.text.length;
    }
    //alert(cursurPosition);
    var newstr="";
    var tmp1=oldStr.substring(0, cursurPosition);
    var tmp2=oldStr.substring(cursurPosition,oldStr.length );
    newstr=tmp1+insertStr+tmp2;

    oTxt1.value =newstr;

   // return newstr;

}


//添加或者编辑朋友圈任务
function addOrEditFriendTask(data,isEdit,html,selectUser){
    var hide = 'style="display:none"',content = '',imgListUrl=[],time='',eidtFriendTask = '',id = '',commentTpl = '',commentTplC = '',address='';
    if(!isEdit){
        var tpl = '';
        if(selectUser.length > 1){
            tpl = '<span class="numTips"><b>等'+selectUser.length+'人</b><i class="viewAllSelectd viewBtnS">展开查看全部</i></span>';
        }
        html = html.join('')+tpl;
        hide = 'style="display:flex"';
    }
    else{
        var picUrls = data.imgs;
        picUrls = picUrls.split(';');
        $.each(picUrls,function(index,item){
            imgListUrl.push('<dd class="active"><img src="'+ item+'"  alt=""><span class="deleteImg"></span></dd>');
        })
        $('.addPic').before(imgListUrl.join(''));
        content = data.content;
        time = data.time ? data.time : '';
        eidtFriendTask = 'eidtFriendTask';
        id = data.id;
        address = data.address?data.address : '';
        if(data.comment){
            var comments = data.comment.split('#'),commentHtml=[],commentHtmlC=[];
            $.each(comments,function(j,cItem){
                commentHtml.push('<div class="commentItem"><span contenteditable="true">'+cItem+'</span><i title="删除" class="deleteComment">×</i></div>')
                commentHtmlC.push('<div class="commentItemHide"><span contenteditable="true">'+commentHtmlC[j]+'</span><i title="删除" class="deleteComment">×</i></div>')
            })
            commentTpl = commentHtml.join('');
            commentTplC = commentHtmlC.join('');
        }

    }
    $('body').append('<section class="telDialog wl-trans-dialog translate-viewport sendCircleFriends '+eidtFriendTask+'" data-widget-cid="widget-0" style="display: block;" data-id="'+id+'"><div class="IframeBoxContent grid"><h3>发朋友圈</h3><div class="dialogContent"><div class="row" '+hide+'><div class="dialogLeft"> <label>已选微信</label></div><div class="dialogRight col"><dl class="choseWX">'+html+'</dl> </div></div> <div class="row"><div class="dialogLeft"> <label>内容</label></div><div class="dialogRight col"><span class="containEmoji"> <textarea id="friendContent"  class="inputVal">'+content+'</textarea><input type="hidden" class="FCValH" /> <i class="emojiBtn"></i></span></div></div><div class="row"><div class="dialogLeft"> <label>照片</label></div><div class="dialogRight col"><form id="my_form" enctype="multipart/form-data"> <dl class="uploadPicDiv">'+imgListUrl.join('')+' <dd class="addPic"><img class="uploadpic" src="'+imgPath+'icon_addition.png"/> <input type="hidden" name="type" value="1"/> <input type="file" enctype="multipart/form-data" class="file" name="file" multiple="multiple"/></dd> </dl><span class="timeTips" style="color: #7c7c7c;">提示：如果图片大小超过1.5M的请尽量分开上传。</span></form></div></div><div class="row"><div class="dialogLeft"> <label>发布地址</label></div><div class="dialogRight col timeRow"> <input type="text" id="address" value="'+address+'" /></div></div><div class="row"><div class="dialogLeft"> <label>添加评论</label></div><div class="dialogRight col commentRow"> <span  class="containEmoji"><input type="text" id="commentVal"  class="inputVal" placeholder="请避免出现“#”" /><input type="hidden" class="CValH" /><i class="emojiBtn"></i></span><button class="addComment">添加</button><!--<div class="commentListHide hide">'+commentTplC+'</div> --><div class="commentList">'+commentTpl+'</div> </div></div><div class="row"><div class="dialogLeft"> <label>发布时间</label></div><div class="dialogRight col timeRow"> <input type="text" id="time" value="'+time+'" /><i class="icon-dateTime"></i> </div></div><div class="row"><div class="dialogLeft"></div><div class="dialogRight col"> <span class="timeTips" style="color: #7c7c7c;">提示：如果未设置时间则立即执行。</span></div></div></div><div class="dialogFooter"><div class="btnGroup"><span class="full submitBtn dialogBtn">确定</span><span class="cancelBtn dialogBtn">取消</span></div> </div> </div></section>');
    //时间控件
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

    $('.icon-dateTime,#time').fdatepicker({
        format: 'yyyy-mm-dd hh:ii',
        pickTime: true,
        leftArrow:'<<',
        rightArrow:'>>',
        disableDblClickSelection: true,
        onRender: function (date) {
            return date.valueOf() < now.valueOf() ? 'disabled' : '';
        }

    })
}

//查看自动回复语句
function getStatement(pg){
    //获取选中用户
    var ids=[];
    $('.userItem.active').each(function(){
        ids.push($(this).data('id'));
    })
    ids = ids.join(';');

    var done = function(res){
            console.log(res);

            var statement = res.statement,htmlStatement = [],viewMore = '';
            $.each(statement,function(i,item){
                //var replyContent = JSON.parse(item.replyContent),
                //    kw = Object.keys(replyContent),replyCon;
                //console.log(replyContent,kw);
                //
                //for(var j in replyContent){
                //    replyCon = replyContent[j]
                //}
                htmlStatement.push('<dd class="row" data-id="'+item.ids+'"><div class="dialogLeft"><span class="kw-item-img">'+parseEmoji(item.keyword)+'</span><span class="kw-item hide">'+item.keyword+'</span></div><div class="dialogRight col"><p class="ellipsis words reply-item-img">'+parseEmoji(item.replyContent)+'</p><p class="words reply-item hide">'+item.replyContent+'</p><span class="opateBtn editBtn"></span><span class="opateBtn deleteBtn"></span></div></dd>');
            })
            if(res.hasNext){

                viewMore = '<dd class="nextPageStatement"><span class="viewMore" data-pg="'+data.pageNo+'">查看更多自动回复记录</span></dd>';
            }
            var next = $('.nextPageStatement');
            if(next.length == 0){
                $('.replyList').append(htmlStatement.join('')+viewMore);
            }
            else{
                next.before(htmlStatement.join(''));
                next.find('.viewMore').attr('data-pg',data.pageNo)
                if(!res.hasNext){
                    next.hide();
                }
            }

        },
    //failed = function(){
    //
    //},
        data = {
            pageNo:pg,
            pageSize:3,
            deviceIds:ids,
            userId:profileId
        }
    if(pg != 0){
        data.pageSize = 10;
        data.pageNo = 10*(pg-1)+3;
    }
    console.log(data);
    jsAjax(data,Url+'selectAutoContents.json',done,'');
}

//编辑或者删除自动回复
function editOrDeleteStatment(flag){
    var data = {
            keyword:$('#keyWord').val(),
            content:$('#replyContent').val(),
            profileId:profileId,
            flag:flag,
            sIds:$('.curReply').data('id')
        },
        done = function(){

            var tip,cur = $('.curReply');
            if(flag ==1){
                tip = '编辑';
                $('.txt_reply').text('添加');
                cur.find('.kw-item').text(data.keyword);
                cur.find('.reply-item').text(data.content);
                cur.find('.kw-item-img').text(data.keyword);
                cur.find('.reply-item-img').html(parseEmoji(data.content));
                $('#keyWord,#replyContent').val('');
                $('.replyList dd').removeClass('curReply');
            }
            else{
                tip = '删除';
                cur.remove();
            }
            showTips(tip+'成功！');

        }
    //,
    //failed = function(){
    //
    //}
    jsAjax(data,Url+'updateManyProfileStatemts.json',done,'');
}


//添加任务
function addTask(ids,type,taskValue,Dialog){
    var done = function(res){
            console.log(res);
            var resDate = res.data;
            showTips('设置成功！');
            if(Dialog){
                Dialog.remove();
            }
            if(Dialog.hasClass('eidtFriendTask')){

                var curItem = $('.curEditItem');
                curItem.find('.detCon').html(parseEmoji(resDate.taskValue));
                curItem.find('.detConCode').html(resDate.taskValue);
                var pics = resDate.taskPic,picHtml=[];
                pics = pics.split(';');
                $.each(pics,function(i,item){
                    picHtml.push('<img src="'+item+'"/>');
                })
                curItem.find('.listThumb dd').html(picHtml.join(''));

                //评论
                var commentListO = curItem.find('.commentListTask');
                if(resDate.comment){
                    var comments = resDate.comment.split('#');
                    var commentsHtml = [],commentsHtmlC=[];
                        $.each(comments,function(j,comment){
                            commentsHtml.push('<p>'+parseEmoji(comment)+'</p>');
                            commentsHtmlC.push('<p>'+comment+'</p>');
                        })
                        //'+commentsHtml.join('') +'</div>' ;
                        if(commentListO.length > 0){
                            curItem.find('.commentListTaskCode').find('.commentListTaskContent').html(commentsHtmlC.join(''));
                            curItem.find('.commentListTask').find('.commentListTaskContent').html(commentsHtml.join(''));
                        }
                        else{
                           var commentsTpl = '<div class="commentListTask"><h4>评论内容：</h4><div class="commentListTaskContent"> '+commentsHtml.join('') +'</div></div>',
                            commentsTplC = '<div class="commentListTaskCode hide"><div class="commentListTaskContent"> '+commentsHtmlC.join('') +'</div></div>' ;
                            curItem.find('.itemFooter').before(commentsTpl+commentsTplC);
                        }

                    }
                else{
                    if(commentListO.length > 0) {
                        curItem.find('.commentListTask').hide();
                    }
                }



                //时间
                if(resDate.startTime){
                    if(curItem.find('.settingTime').length > 0){
                        curItem.find('.settingTime').text(formatDate(new Date(resDate.startTime))).parent().show();
                    }
                    else{
                        curItem.find('.itemFooter').append('<span class="time ">设置时间：<span class="settingTime">'+formatDate(new Date(resDate.startTime))+'</span></span>');
                    }
                }
                else{
                    curItem.find('.settingTime').parent().hide();
                }

                //地址
                var addr = curItem.find('.taskAddr');
                if(resDate.address){
                    if(addr.length > 0){
                        addr.show().find('.addr').text(resDate.address);
                    }
                   else{
                        curItem.find('.listThumb').after('<p class="taskAddr">发布地址：<span class="addr">广州</span></p>');
                    }
                }
                else{
                    if(addr.length > 0){
                        addr.hide().find('.addr').text('');
                    }
                }
            }
        },

    //failed = function(){
    //
    //},
        data = {
            flag:type,
            profileId:profileId,
            deviceIds:ids
        },
        resUrl = Url+'addOneTaskContent.json';

    console.log(data);

    if(type != 1){
        if(!taskValue){
            showTips('有未填内容！');
            return false
        }
        if(taskValue.length > 600){
            showTips('字数上限')
            return false
        }
        data.taskValue=taskValue;
    }
    if(type == 2){
        var time = $('#time').val();
        if(time){
            data.startTime = time + ':00';
        }
        var pic = [],picTar = $('.uploadPicDiv .active img');

        if(picTar.length == 0){
            showTips('请上传图片！');
            return false;
        }
        picTar.each(function(){
            pic.push($(this).attr('src'));
        })
        data.taskPic=pic.join(';');

        var commentList = $('.commentList').find('.commentItem'),comment=[];

        commentList.each(function(){
            comment.push($(this).find('span').text());
        })
        data.comment = comment.join('#');

        var address = $('#address').val();
       if(address){
           data.address = address;
       }

        if(Dialog.hasClass('eidtFriendTask')){
            data.taskId  = Dialog.attr('data-id');
            resUrl = Url+'editOneTaskContent.json';
        }

    }
    jsAjax(data,resUrl,done,'');
}

//修改任务状态
function changeTaskStatus(type,target){
    var taskIds = [];
    if(target.hasClass('deleteAllBtn')){
        //taskIds = [];
        $('.TaskList').find('.selected').each(function(){
           $(this).parents('li').find('.taskItem').each(function(){
                taskIds.push($(this).data('id'));
            })
        })
        taskIds = taskIds.join('#');
        console.log(taskIds);
    }
    else{
        var taskItem = target.parents('.executeStatus').find('.taskItem');
        taskItem.each(function(){
            taskIds.push($(this).data('id'));
        })
        taskIds = taskIds.join('#');

        //taskIds = target.parents('.executeStatus').data('id');
    }
    var done = function(res){
            showTips('执行成功！');

            if(type == -2){
                if(target.hasClass('deleteAllBtn')){
                    $('.TaskList').find('.selected').each(function(){
                        $(this).parents('li').remove();
                    })
                }
                else{
                    var O =  target.parents('.executeStatus');
                    if(O.length == 1){
                        target.parents('li').remove();
                    }
                    O.remove();
                }
            }
            if(type == 0){
                //target.parents('li').remove();
                var failT = target.parents('.execuFail'),
                    tpl = failT.find('dl').html(),
                    unstartT = target.parents('.taskContent').find('.execuUnstart').find('dl');
                if(unstartT.length > 0){
                    unstartT.append(tpl);
                }
                else{
                    var hide='',more='';
                    if(failT.find('dd').length > 6){
                        hide = 'style="display:none"';
                        more = '<span class="more">》》</span>';

                    }
                    target.parents('.taskContent').prepend('<div class="executeStatus execuUnstart">尚未开始：<dl class="devicePerson">'+tpl+'</dl>'+more+'<p class="btnGroupOper"><span class="deleteTask">删除</span></p></div>');
                    target.parents('.taskContent').find('.itemFooter').find('.createTime').after('<span class="editTask ">编辑</span>');
                }

                failT.remove();

            }
        },

    //failed = function(){
    //
    //},
        data = {
            taskIds:taskIds,
            profileId:profileId,
            status:type
        }
    //console.log(data);
    if(type == -2 && confirm('确定要删除？')){
        jsAjax(data,Url+'changeManyTaskStatus.json',done,'');
    }
    if(type == 0){
        jsAjax(data,Url+'changeManyTaskStatus.json',done,'');
    }

}

//读取图片
function readURL(input) {

    if (input.files && input.files[0]) {
        var len = input.files.length > 9 ? 9 : input.files.length;
        for(var i = 0;i< len;i++){
            var reader = new FileReader();
            //判断图片路径
            reader.onload = function (e) {
                if (!/image/.test(e.target.result)) {
                    alert('请上传图片格式...');
                    return false;
                }
                //预览图
                $('.addPic').before('<dd class="active"><img src="'+ e.target.result+'"  alt=""></dd>');

                //删除图片
                //$('.deleteImg').click(function(){
                //    $(this).parent().remove();
                //})
            }
            reader.readAsDataURL(input.files[i]);
        }

    }
}

//上传图片
function uploadImages(form, url,$this,isExcel){
    var pomi = $.Deferred();
    var data = new FormData($(form)[0]);
    //读取图片预览图
    console.log(data);
    if(!isExcel){
        //readURL($this);
    }
    if($('.waitting').length == 0){
        $('body').append('<div class="waitting" style="margin-left: -24px;margin-top: -24px;"> <div class="loading"></div></div>');
    }
    else{
        $('.waitting').show();
    }

    //上传
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        dataType: 'JSON',
        cache: false,
        processData: false,
        contentType: false
    }).done(function(res){
        console.log(res);
        $this.value = '';
        $('.waitting').hide();
        // res = JSON.parse(res);
        pomi.resolve(res);
    }).fail(function() {
        $this.value = '';
        //$(this).val('');
        showTips('上传失败！');
        $('.waitting').hide();
    });
    return pomi.promise();
}


//显示全部终端用户信息
function getAllUser(pageNo){
    var pz = 35,
    done = function(res){
        var list = res.list;
        console.log(list);

        var
            html=[];
        $.each(list,function(index,item){
            var
                wxname = item.wxLemonName ? item.wxLemonName : '';
            html.push('<li class="tagEndItem" data-userid="'+item.userId+'" data-id="'+item.id+'" data-wxNum="'+item.wxnum+'" data-wxopendid="'+item.wxOpendId+'" ><div class="row"> <div class="fb fvc fac imgDiv"><img src="'+item.wxPic+'"/></div><div class="col col-15 txtDiv fb fvc"><p class="name ellipsis">昵称:<span>'+wxname+'</span></p></div></div><div class="contactList"></div></li>');
        })
        $('.wrapper').html('<div class="title row"><div class="col col-5 h3">微信用户</div></div><div class="content"><ul class="allUserList">'+html.join(''));

        pagina(res.totalSize,pageNo,pz,'pgUserBtn','','');

        $('.contactList').scroll(function(){
            var _this=$(this);
            var viewH=_this.height(),
                contentH =_this.get(0).scrollHeight,
                scrollTop = _this.scrollTop();
            if(scrollTop + viewH == contentH){
                if($(this).attr('data-hasnext') == 'true'){
                    var pg = parseInt($(this).attr('data-pg')) + 1;
                    getUserContact($(this).parent(),pg);
                    return false;
                }
            }
        });

    },
        //var failed = function(res){
        //    showTips('请求失败！');
        //}
        data = {
            userId:profileId,
            pageSize:pz,
            pageNo:pageNo
        };

    jsAjax(data,Url+'selectWxListByUserId.json',done,'请求失败！');

}

//获取微信联系人列表
function getUserContact($this,pg){
    var id  = $this.data('id'),
        done = function(res){
            var list = res.list,html=[];
            $.each(list,function(i,item){
                var nickname = item.wechatNick ? item.wechatNick : '',
                    wechatNum = item.wechatNum ? item.wechatNum :　'',
                    wechatPhone = item.wechatPhone ? item.wechatPhone :'',
                    time = formatDate(new Date(item.lastUpdated),'mm-dd h:i');
                html.push('<tr><td>'+nickname+'</td><td>'+wechatNum+'</td><td>'+wechatPhone+'</td><td>'+time+'</td></tr>');
            })

            $('.contactList').hide();
            if(pg == 0 && list.length > 0){
                $this.find('.contactList').append('<div class="box"> <p class="tit">联系人列表：</p><table class="tableList"><tr><th>昵称</th><th>微信号</th><th>手机号</th><th>登录时间</th></tr></table></div>');
            }
            if(!(pg == 0 && list.length == 0)){
                $this.find('.contactList').attr({'data-hasnext':res.hasNext,'data-pg':pg}).show().find('.tableList').append(html.join(''));
            }
            else{
                showTips('暂无联系人列表！')
            }
        }
    //var failed = function(res){
    //    showTips('请求失败！');
    //}
    var data = {
        userId:profileId,
        wxId:id,
        pageNo:pg,
        pageSize:10
    }

    jsAjax(data,Url+'selectFriendListByWxId.json',done,'请求失败！');
}

//通讯录
function getAddrList(pageNo){


    var pz = 15,
    done = function(res){
        var list = res.data;
        console.log(list);


        var html=[];
        if(list.length >0){
            $.each(list,function(index,item){
                var read = item.isRead ? '是' : '否',
                    enabled = item.isEnabled ? '是' : '否',
                    success = item.isSuccess ? '是' : '否',
                    readT = item.isRead ? '<span class="btnContact reRead">重新读取</span>' : '',
                    Detail = item.isSuccess ? '<span class="btnContact viewDetail">详情</span>' : '';
                html.push('<li class="row" data-id="'+item.id+'"><div class="col">'+item.phoneNum+'</div><div class="col isRead">'+read+'</div> <div class="col">'+enabled+'</div> <div class="col">'+success+'</div><div class="col"><div class="opateWrap">'+readT+Detail+'<span class="btnContact deleteContact">删除</span></div></div>  </li>');
            })
        }
        else{
            html.push('<li class="noData"><div>暂无通讯录好友！</div> </li>');
        }

        var DefualtName = $('body').attr('defaultname');
        $('.wrapper').html('<div class="title row"><div class="col col-2 h3">通讯录</div><div class="col col-18 fb fvc far"><div class="changeDefualtName"><span>修改当前的添加好友验证消息：</span><input type="text" class="defaultNameVal" value="'+DefualtName+'" placeholder="" /><input type="button" class="defaultNameBtn" value="修改" />  </div><a class="downloadFile" title="下载模版" href="http://120.24.102.187:8070/hiden/model.xls">下载模版</a><span class="uploadFile"><i  title="导入通讯录">导入通讯录</i><form id="excel_form" enctype="multipart/form-data"> <input type="hidden" name="userId" value="'+profileId+'" /> <input type="file" enctype="multipart/form-data" class="file_excel" name="file"  accept="application/vnd.ms-excel"/><img src="'+imgPath+'template.png"/> </form></span></div> </div><div class="row"><div class="col fb fvc far"> </div> </div> <div class="content grid"><ul class="allAddrList"><li class="row headtr"><div class="col">电话</div><div class="col">是否读取过</div> <div class="col">是否有效好友</div><div class="col">是否添加成功</div> <div class="col">操作</div>  </li>'+html.join('')+'</ul></div>');

        pagina(res.totalsize,pageNo,pz,'pgAddrListBtn','','');



    },
        //var failed = function(res){
        //    showTips('请求失败！');
        //}
        data = {
            userId:profileId,
            pageSize:pz,
            pageNo:pageNo
        };

    jsAjax(data,Url+'getPhoneListData.json',done,'请求失败！');
}



//时间戳转换
function   formatDate(now,format)   {
    var   year=now.getFullYear();
    var   month=now.getMonth()+1;
    var   date=now.getDate();
    var   hour=now.getHours();
    var   minute=now.getMinutes();
    var   second=now.getSeconds();

    var time = year+"-"+p(month)+"-"+p(date)+"   "+p(hour)+":"+p(minute)+":"+p(second);

    switch (format){
        case 'yy-mm':
            time =  year+"-"+p(month);
            break;
        case 'yy-mm-dd':
            time =  year+"-"+p(month)+"-"+p(date);
            break;
        case 'mm-dd h:i':
            time =  p(month)+"-"+p(date)+"   "+p(hour)+":"+p(minute);
            break;
        case 'mm-dd':
            time =  p(month)+"-"+p(date);
            break;
        case 'h:i:s':
            time =  p(hour)+":"+p(minute)+":"+p(second);
            break;
        case 'h:i':
            time = p(hour)+":"+p(minute);
            break;
        default:
            break;

    }
    //if(format){
    //    time = p(month)+"-"+p(date)+"   "+p(hour)+":"+p(minute);
    //}
    return  time
}
function p(s) {
    return s < 10 ? '0' + s : s;
}


//表情
function emoji(){
    var emoji = [
        {
            classN:'emoji2600',
            wechat:'☀'
        },
        {
            classN:'emoji2601',
            wechat:'☁'
        },
        {
            classN:'emoji2614',
            wechat:'☔'
        },
        {
            classN:'emoji26c4',
            wechat:'⛄'
        },
        {
            classN:'emoji26a1',
            wechat:'⚡'
        },
        {
            classN:'emoji1f300',
            wechat:'🌀'
        },
        {
            classN:'emoji1f301',
            wechat:'🌁'
        },
        {
            classN:'emoji1f302',
            wechat:'🌂'
        },
        {
            classN:'emoji1f303',
            wechat:'🌃'
        },
        {
            classN:'emoji1f304',
            wechat:'🌄'
        },
        {
            classN:'emoji1f305',
            wechat:'🌅'
        },
        {
            classN:'emoji1f306',
            wechat:'🌆'
        },
        {
            classN:'emoji1f307',
            wechat:'🌇'
        },
        {
            classN:'emoji1f308',
            wechat:'🌈'
        },
        {
            classN:'emoji2744',
            wechat:'❄'
        },
        {
            classN:'emoji26c5',
            wechat:'⛅'
        },
        {
            classN:'emoji1f309',
            wechat:'🌉'
        },
        {
            classN:'emoji1f30a',
            wechat:'🌊'
        },
        {
            classN:'emoji1f30b',
            wechat:'🌋'
        },
        {
            classN:'emoji1f30c',
            wechat:'🌌'
        },
        {
            classN:'emoji1f30f',
            wechat:'🌏'
        },
        {
            classN:'emoji1f311',
            wechat:'🌑'
        },
        {
            classN:'emoji1f314',
            wechat:'🌔'
        },
        {
            classN:'emoji1f313',
            wechat:'🌓'
        },
        {
            classN:'emoji1f319',
            wechat:'🌙'
        },
        {
            classN:'emoji1f315',
            wechat:'🌕'
        },
        {
            classN:'emoji1f31b',
            wechat:'🌛'
        },
        {
            classN:'emoji1f31f',
            wechat:'🌟'
        },
        {
            classN:'emoji1f320',
            wechat:'🌠'
        },
        {
            classN:'emoji1f550',
            wechat:'🕐'
        },
        {
            classN:'emoji1f551',
            wechat:'🕑'
        },
        {
            classN:'emoji1f552',
            wechat:'🕒'
        },
        {
            classN:'emoji1f553',
            wechat:'🕓'
        },
        {
            classN:'emoji1f554',
            wechat:'🕔'
        },
        {
            classN:'emoji1f555',
            wechat:'🕕'
        },
        {
            classN:'emoji1f556',
            wechat:'🕖'
        },
        {
            classN:'emoji1f557',
            wechat:'🕗'
        },
        {
            classN:'emoji1f558',
            wechat:'🕘'
        },
        {
            classN:'emoji1f559',
            wechat:'🕙'
        },
        {
            classN:'emoji1f55a',
            wechat:'🕚'
        },
        {
            classN:'emoji1f55b',
            wechat:'🕛'
        },
        {
            classN:'emoji231a',
            wechat:'⌚'
        },
        {
            classN:'emoji231b',
            wechat:'⌛'
        },
        {
            classN:'emoji23f0',
            wechat:'⏰'
        },
        {
            classN:'emoji23f3',
            wechat:'⏳'
        },
        {
            classN:'emoji2648',
            wechat:'♈'
        },
        {
            classN:'emoji2649',
            wechat:'♉'
        },
        {
            classN:'emoji264a',
            wechat:'♊'
        },
        {
            classN:'emoji264b',
            wechat:'♋'
        },
        {
            classN:'emoji264c',
            wechat:'♌'
        },
        {
            classN:'emoji264d',
            wechat:'♍'
        },
        {
            classN:'emoji264e',
            wechat:'♎'
        },
        {
            classN:'emoji264f',
            wechat:'♏'
        },
        {
            classN:'emoji2650',
            wechat:'♐'
        },
        {
            classN:'emoji2651',
            wechat:'♑'
        },
        {
            classN:'emoji2652',
            wechat:'♒'
        },
        {
            classN:'emoji2653',
            wechat:'♓'
        },
        {
            classN:'emoji26ce',
            wechat:'⛎'
        },
        {
            classN:'emoji1f340',
            wechat:'🍀'
        },
        {
            classN:'emoji1f337',
            wechat:'🌷'
        },
        {
            classN:'emoji1f331',
            wechat:'🌱'
        },
        {
            classN:'emoji1f341',
            wechat:'🍁'
        },
        {
            classN:'emoji1f338',
            wechat:'🌸'
        },
        {
            classN:'emoji1f339',
            wechat:'🌹'
        },
        {
            classN:'emoji1f342',
            wechat:'🍂'
        },
        {
            classN:'emoji1f343',
            wechat:'🍃'
        },
        {
            classN:'emoji1f33a',
            wechat:'🌺'
        },
        {
            classN:'emoji1f33b',
            wechat:'🌻'
        },
        {
            classN:'emoji1f334',
            wechat:'🌴'
        },
        {
            classN:'emoji1f335',
            wechat:'🌵'
        },
        {
            classN:'emoji1f33e',
            wechat:'🌾'
        },
        {
            classN:'emoji1f33d',
            wechat:'🌽'
        },
        {
            classN:'emoji1f344',
            wechat:'🍄'
        },
        {
            classN:'emoji1f330',
            wechat:'🌰'
        },
        {
            classN:'emoji1f33c',
            wechat:'🌼'
        },
        {
            classN:'emoji1f33f',
            wechat:'🌿'
        },
        {
            classN:'emoji1f352',
            wechat:'🍒'
        },
        {
            classN:'emoji1f34c',
            wechat:'🍌'
        },
        {
            classN:'emoji1f34e',
            wechat:'🍎'
        },
        {
            classN:'emoji1f34a',
            wechat:'🍊'
        },
        {
            classN:'emoji1f353',
            wechat:'🍓'
        },
        {
            classN:'emoji1f349',
            wechat:'🍉'
        },
        {
            classN:'emoji1f345',
            wechat:'🍅'
        },
        {
            classN:'emoji1f346',
            wechat:'🍆'
        },
        {
            classN:'emoji1f348',
            wechat:'🍈'
        },
        {
            classN:'emoji1f34d',
            wechat:'🍍'
        },
        {
            classN:'emoji1f347',
            wechat:'🍇'
        },
        {
            classN:'emoji1f351',
            wechat:'🍑'
        },
        {
            classN:'emoji1f34f',
            wechat:'🍏'
        },
        {
            classN:'emoji1f440',
            wechat:'👀'
        },
        {
            classN:'emoji1f442',
            wechat:'👂'
        },
        {
            classN:'emoji1f443',
            wechat:'👃'
        },
        {
            classN:'emoji1f444',
            wechat:'👄'
        },
        {
            classN:'emoji1f445',
            wechat:'👅'
        },
        {
            classN:'emoji1f484',
            wechat:'💄'
        },
        {
            classN:'emoji1f485',
            wechat:'💅'
        },
        {
            classN:'emoji1f486',
            wechat:'💆'
        },
        {
            classN:'emoji1f487',
            wechat:'💇'
        },
        {
            classN:'emoji1f488',
            wechat:'💈'
        },
        {
            classN:'emoji1f464',
            wechat:'👤'
        },
        {
            classN:'emoji1f466',
            wechat:'👦'
        },
        {
            classN:'emoji1f467',
            wechat:'👧'
        },
        {
            classN:'emoji1f468',
            wechat:'👨'
        },
        {
            classN:'emoji1f469',
            wechat:'👩'
        },
        {
            classN:'emoji1f46a',
            wechat:'👪'
        },
        {
            classN:'emoji1f46b',
            wechat:'👫'
        },
        {
            classN:'emoji1f46e',
            wechat:'👮'
        },
        {
            classN:'emoji1f46f',
            wechat:'👯'
        },
        {
            classN:'emoji1f470',
            wechat:'👰'
        },
        {
            classN:'emoji1f471',
            wechat:'👱'
        },
        {
            classN:'emoji1f472',
            wechat:'👲'
        },
        {
            classN:'emoji1f473',
            wechat:'👳'
        },
        {
            classN:'emoji1f474',
            wechat:'👴'
        },
        {
            classN:'emoji1f475',
            wechat:'👵'
        },
        {
            classN:'emoji1f476',
            wechat:'👶'
        },
        {
            classN:'emoji1f477',
            wechat:'👷'
        },
        {
            classN:'emoji1f478',
            wechat:'👸'
        },
        {
            classN:'emoji1f479',
            wechat:'👹'
        },
        {
            classN:'emoji1f47a',
            wechat:'👺'
        },
        {
            classN:'emoji1f47b',
            wechat:'👻'
        },
        {
            classN:'emoji1f47c',
            wechat:'👼'
        },
        {
            classN:'emoji1f47d',
            wechat:'👽'
        },
        {
            classN:'emoji1f47e',
            wechat:'👾'
        },
        {
            classN:'emoji1f47f',
            wechat:'👿'
        },
        {
            classN:'emoji1f480',
            wechat:'💀'
        },
        {
            classN:'emoji1f481',
            wechat:'💁'
        },
        {
            classN:'emoji1f482',
            wechat:'💂'
        },
        {
            classN:'emoji1f483',
            wechat:'💃'
        },
        {
            classN:'emoji1f40c',
            wechat:'🐌'
        },
        {
            classN:'emoji1f40d',
            wechat:'🐍'
        },
        {
            classN:'emoji1f40e',
            wechat:'🐎'
        },
        {
            classN:'emoji1f414',
            wechat:'🐔'
        },
        {
            classN:'emoji1f417',
            wechat:'🐗'
        },
        {
            classN:'emoji1f42b',
            wechat:'🐫'
        },
        {
            classN:'emoji1f418',
            wechat:'🐘'
        },
        {
            classN:'emoji1f428',
            wechat:'🐨'
        },
        {
            classN:'emoji1f412',
            wechat:'🐒'
        },
        {
            classN:'emoji1f411',
            wechat:'🐑'
        },
        {
            classN:'emoji1f419',
            wechat:'🐙'
        },
        {
            classN:'emoji1f41a',
            wechat:'🐚'
        },
        {
            classN:'emoji1f41b',
            wechat:'🐛'
        },
        {
            classN:'emoji1f41c',
            wechat:'🐜'
        },
        {
            classN:'emoji1f41d',
            wechat:'🐝'
        },
        {
            classN:'emoji1f41e',
            wechat:'🐞'
        },
        {
            classN:'emoji1f420',
            wechat:'🐠'
        },
        {
            classN:'emoji1f421',
            wechat:'🐡'
        },
        {
            classN:'emoji1f422',
            wechat:'🐢'
        },
        {
            classN:'emoji1f424',
            wechat:'🐤'
        },
        {
            classN:'emoji1f425',
            wechat:'🐥'
        },
        {
            classN:'emoji1f426',
            wechat:'🐦'
        },
        {
            classN:'emoji1f423',
            wechat:'🐣'
        },
        {
            classN:'emoji1f427',
            wechat:'🐧'
        },
        {
            classN:'emoji1f429',
            wechat:'🐩'
        },
        {
            classN:'emoji1f41f',
            wechat:'🐟'
        },
        {
            classN:'emoji1f42c',
            wechat:'🐬'
        },
        {
            classN:'emoji1f42d',
            wechat:'🐭'
        },
        {
            classN:'emoji1f42f',
            wechat:'🐯'
        },
        {
            classN:'emoji1f431',
            wechat:'🐱'
        },
        {
            classN:'emoji1f433',
            wechat:'🐳'
        },
        {
            classN:'emoji1f434',
            wechat:'🐴'
        },
        {
            classN:'emoji1f435',
            wechat:'🐵'
        },
        {
            classN:'emoji1f436',
            wechat:'🐶'
        },
        {
            classN:'emoji1f437',
            wechat:'🐷'
        },
        {
            classN:'emoji1f43b',
            wechat:'🐻'
        },
        {
            classN:'emoji1f439',
            wechat:'🐹'
        },
        {
            classN:'emoji1f43a',
            wechat:'🐺'
        },
        {
            classN:'emoji1f42e',
            wechat:'🐮'
        },
        {
            classN:'emoji1f430',
            wechat:'🐰'
        },
        {
            classN:'emoji1f438',
            wechat:'🐸'
        },
        {
            classN:'emoji1f43e',
            wechat:'🐾'
        },
        {
            classN:'emoji1f432',
            wechat:'🐲'
        },
        {
            classN:'emoji1f43c',
            wechat:'🐼'
        },
        {
            classN:'emoji1f43d',
            wechat:'🐽'
        },
        {
            classN:'emoji1f620',
            wechat:'😠'
        },
        {
            classN:'emoji1f629',
            wechat:'😩'
        },
        {
            classN:'emoji1f632',
            wechat:'😲'
        },
        {
            classN:'emoji1f61e',
            wechat:'😞'
        },
        {
            classN:'emoji1f635',
            wechat:'😵'
        },
        {
            classN:'emoji1f630',
            wechat:'😰'
        },
        {
            classN:'emoji1f612',
            wechat:'😒'
        },
        {
            classN:'emoji1f60d',
            wechat:'😍'
        },
        {
            classN:'emoji1f624',
            wechat:'😤'
        },
        {
            classN:'emoji1f61c',
            wechat:'😜'
        },
        {
            classN:'emoji1f61d',
            wechat:'😝'
        },
        {
            classN:'emoji1f60b',
            wechat:'😋'
        },
        {
            classN:'emoji1f618',
            wechat:'😘'
        },
        {
            classN:'emoji1f61a',
            wechat:'😚'
        },
        {
            classN:'emoji1f637',
            wechat:'😷'
        },
        {
            classN:'emoji1f633',
            wechat:'😳'
        },
        {
            classN:'emoji1f603',
            wechat:'😃'
        },
        {
            classN:'emoji1f605',
            wechat:'😅'
        },
        {
            classN:'emoji1f606',
            wechat:'😆'
        },
        {
            classN:'emoji1f601',
            wechat:'😁'
        },
        {
            classN:'emoji1f602',
            wechat:'😂'
        },
        {
            classN:'emoji1f60a',
            wechat:'😊'
        },
        {
            classN:'emoji263a',
            wechat:'☺'
        },
        {
            classN:'emoji1f604',
            wechat:'😄'
        },
        {
            classN:'emoji1f622',
            wechat:'😢'
        },
        {
            classN:'emoji1f62d',
            wechat:'😭'
        },
        {
            classN:'emoji1f628',
            wechat:'😨'
        },
        {
            classN:'emoji1f623',
            wechat:'😣'
        },
        {
            classN:'emoji1f621',
            wechat:'😡'
        },
        {
            classN:'emoji1f60c',
            wechat:'😌'
        },
        {
            classN:'emoji1f616',
            wechat:'😖'
        },
        {
            classN:'emoji1f614',
            wechat:'😔'
        },
        {
            classN:'emoji1f631',
            wechat:'😱'
        },
        {
            classN:'emoji1f62a',
            wechat:'😪'
        },
        {
            classN:'emoji1f60f',
            wechat:'😏'
        },
        {
            classN:'emoji1f613',
            wechat:'😓'
        },
        {
            classN:'emoji1f625',
            wechat:'😥'
        },
        {
            classN:'emoji1f62b',
            wechat:'😫'
        },
        {
            classN:'emoji1f609',
            wechat:'😉'
        },
        {
            classN:'emoji1f63a',
            wechat:'😺'
        },
        {
            classN:'emoji1f638',
            wechat:'😸'
        },
        {
            classN:'emoji1f639',
            wechat:'😹'
        },
        {
            classN:'emoji1f63d',
            wechat:'😽'
        },
        {
            classN:'emoji1f63b',
            wechat:'😻'
        },
        {
            classN:'emoji1f63f',
            wechat:'😿'
        },
        {
            classN:'emoji1f63e',
            wechat:'😾'
        },
        {
            classN:'emoji1f63c',
            wechat:'😼'
        },
        {
            classN:'emoji1f640',
            wechat:'🙀'
        },
        {
            classN:'emoji1f645',
            wechat:'🙅'
        },
        {
            classN:'emoji1f646',
            wechat:'🙆'
        },
        {
            classN:'emoji1f647',
            wechat:'🙇'
        },
        {
            classN:'emoji1f648',
            wechat:'🙈'
        },
        {
            classN:'emoji1f64a',
            wechat:'🙊'
        },
        {
            classN:'emoji1f649',
            wechat:'🙉'
        },
        {
            classN:'emoji1f64b',
            wechat:'🙋'
        },
        {
            classN:'emoji1f64c',
            wechat:'🙌'
        },
        {
            classN:'emoji1f64d',
            wechat:'🙍'
        },
        {
            classN:'emoji1f64e',
            wechat:'🙎'
        },
        {
            classN:'emoji1f64f',
            wechat:'🙏'
        },
        {
            classN:'emoji1f3e0',
            wechat:'🏠'
        },
        {
            classN:'emoji1f3e1',
            wechat:'🏡'
        },
        {
            classN:'emoji1f3e2',
            wechat:'🏢'
        },
        {
            classN:'emoji1f3e3',
            wechat:'🏣'
        },
        {
            classN:'emoji1f3e5',
            wechat:'🏥'
        },
        {
            classN:'emoji1f3e6',
            wechat:'🏦'
        },
        {
            classN:'emoji1f3e7',
            wechat:'🏧'
        },
        {
            classN:'emoji1f3e8',
            wechat:'🏨'
        },
        {
            classN:'emoji1f3e9',
            wechat:'🏩'
        },
        {
            classN:'emoji1f3ea',
            wechat:'🏪'
        },
        {
            classN:'emoji1f3eb',
            wechat:'🏫'
        },
        {
            classN:'emoji26ea',
            wechat:'⛪'
        },
        {
            classN:'emoji26f2',
            wechat:'⛲'
        },
        {
            classN:'emoji1f3ec',
            wechat:'🏬'
        },
        {
            classN:'emoji1f3ef',
            wechat:'🏯'
        },
        {
            classN:'emoji1f3f0',
            wechat:'🏰'
        },
        {
            classN:'emoji1f3ed',
            wechat:'🏭'
        },
        {
            classN:'emoji2693',
            wechat:'⚓'
        },
        {
            classN:'emoji1f3ee',
            wechat:'🏮'
        },
        {
            classN:'emoji1f5fb',
            wechat:'🗻'
        },
        {
            classN:'emoji1f5fc',
            wechat:'🗼'
        },
        {
            classN:'emoji1f5fd',
            wechat:'🗽'
        },
        {
            classN:'emoji1f5fe',
            wechat:'🗾'
        },
        {
            classN:'emoji1f5ff',
            wechat:'🗿'
        },
        {
            classN:'emoji1f45e',
            wechat:'👞'
        },
        {
            classN:'emoji1f45f',
            wechat:'👟'
        },
        {
            classN:'emoji1f460',
            wechat:'👠'
        },
        {
            classN:'emoji1f461',
            wechat:'👡'
        },
        {
            classN:'emoji1f462',
            wechat:'👢'
        },
        {
            classN:'emoji1f463',
            wechat:'👣'
        },
        {
            classN:'emoji1f453',
            wechat:'👓'
        },
        {
            classN:'emoji1f455',
            wechat:'👕'
        },
        {
            classN:'emoji1f456',
            wechat:'👖'
        },
        {
            classN:'emoji1f451',
            wechat:'👑'
        },
        {
            classN:'emoji1f454',
            wechat:'👔'
        },
        {
            classN:'emoji1f452',
            wechat:'👒'
        },
        {
            classN:'emoji1f457',
            wechat:'👗'
        },
        {
            classN:'emoji1f458',
            wechat:'👘'
        },
        {
            classN:'emoji1f459',
            wechat:'👙'
        },
        {
            classN:'emoji1f45a',
            wechat:'👚'
        },
        {
            classN:'emoji1f45b',
            wechat:'👛'
        },
        {
            classN:'emoji1f45c',
            wechat:'👜'
        },
        {
            classN:'emoji1f45d',
            wechat:'👝'
        },
        {
            classN:'emoji1f4b0',
            wechat:'💰'
        },
        {
            classN:'emoji1f4b1',
            wechat:'💱'
        },
        {
            classN:'emoji1f4b9',
            wechat:'💹'
        },
        {
            classN:'emoji1f4b2',
            wechat:'💲'
        },
        {
            classN:'emoji1f4b3',
            wechat:'💳'
        },
        {
            classN:'emoji1f4b4',
            wechat:'💴'
        },
        {
            classN:'emoji1f4b5',
            wechat:'💵'
        },
        {
            classN:'emoji1f4b8',
            wechat:'💸'
        },
        {
            classN:'emoji1f525',
            wechat:'🔥'
        },
        {
            classN:'emoji1f526',
            wechat:'🔦'
        },
        {
            classN:'emoji1f527',
            wechat:'🔧'
        },
        {
            classN:'emoji1f528',
            wechat:'🔨'
        },
        {
            classN:'emoji1f529',
            wechat:'🔩'
        },
        {
            classN:'emoji1f52a',
            wechat:'🔪'
        },
        {
            classN:'emoji1f52b',
            wechat:'🔫'
        },
        {
            classN:'emoji1f52e',
            wechat:'🔮'
        },
        {
            classN:'emoji1f52f',
            wechat:'🔯'
        },
        {
            classN:'emoji1f530',
            wechat:'🔰'
        },
        {
            classN:'emoji1f531',
            wechat:'🔱'
        },
        {
            classN:'emoji1f489',
            wechat:'💉'
        },
        {
            classN:'emoji1f48a',
            wechat:'💊'
        },
        {
            classN:'emoji1f170',
            wechat:'🅰'
        },
        {
            classN:'emoji1f171',
            wechat:'🅱'
        },
        {
            classN:'emoji1f18e',
            wechat:'🆎'
        },
        {
            classN:'emoji1f17e',
            wechat:'🅾'
        },
        {
            classN:'emoji1f380',
            wechat:'🎀'
        },
        {
            classN:'emoji1f381',
            wechat:'🎁'
        },
        {
            classN:'emoji1f382',
            wechat:'🎂'
        },
        {
            classN:'emoji1f384',
            wechat:'🎄'
        },
        {
            classN:'emoji1f385',
            wechat:'🎅'
        },
        {
            classN:'emoji1f38c',
            wechat:'🎌'
        },
        {
            classN:'emoji1f386',
            wechat:'🎆'
        },
        {
            classN:'emoji1f388',
            wechat:'🎈'
        },
        {
            classN:'emoji1f389',
            wechat:'🎉'
        },
        {
            classN:'emoji1f38d',
            wechat:'🎍'
        },
        {
            classN:'emoji1f38e',
            wechat:'🎎'
        },
        {
            classN:'emoji1f393',
            wechat:'🎓'
        },
        {
            classN:'emoji1f392',
            wechat:'🎒'
        },
        {
            classN:'emoji1f38f',
            wechat:'🎏'
        },
        {
            classN:'emoji1f387',
            wechat:'🎇'
        },
        {
            classN:'emoji1f390',
            wechat:'🎐'
        },
        {
            classN:'emoji1f383',
            wechat:'🎃'
        },
        {
            classN:'emoji1f38a',
            wechat:'🎊'
        },
        {
            classN:'emoji1f38b',
            wechat:'🎋'
        },
        {
            classN:'emoji1f391',
            wechat:'🎑'
        },
        {
            classN:'emoji1f4df',
            wechat:'📟'
        },
        {
            classN:'emoji260e',
            wechat:'☎'
        },
        {
            classN:'emoji1f4de',
            wechat:'📞'
        },
        {
            classN:'emoji1f4f1',
            wechat:'📱'
        },
        {
            classN:'emoji1f4f2',
            wechat:'📲'
        },
        {
            classN:'emoji1f4dd',
            wechat:'📝'
        },
        {
            classN:'emoji1f4e0',
            wechat:'📠'
        },
        {
            classN:'emoji2709',
            wechat:'✉'
        },
        {
            classN:'emoji1f4e8',
            wechat:'📨'
        },
        {
            classN:'emoji1f4e9',
            wechat:'📩'
        },
        {
            classN:'emoji1f4ea',
            wechat:'📪'
        },
        {
            classN:'emoji1f4eb',
            wechat:'📫'
        },
        {
            classN:'emoji1f4ee',
            wechat:'📮'
        },
        {
            classN:'emoji1f4f0',
            wechat:'📰'
        },
        {
            classN:'emoji1f4e2',
            wechat:'📢'
        },
        {
            classN:'emoji1f4e3',
            wechat:'📣'
        },
        {
            classN:'emoji1f4e1',
            wechat:'📡'
        },
        {
            classN:'emoji1f4e4',
            wechat:'📤'
        },
        {
            classN:'emoji1f4e5',
            wechat:'📥'
        },
        {
            classN:'emoji1f4e6',
            wechat:'📦'
        },
        {
            classN:'emoji1f4e7',
            wechat:'📧'
        },
        {
            classN:'emoji1f520',
            wechat:'🔠'
        },
        {
            classN:'emoji1f521',
            wechat:'🔡'
        },
        {
            classN:'emoji1f522',
            wechat:'🔢'
        },
        {
            classN:'emoji1f523',
            wechat:'🔣'
        },
        {
            classN:'emoji1f524',
            wechat:'🔤'
        },
        {
            classN:'emoji2712',
            wechat:'✒'
        },
        {
            classN:'emoji1f4ba',
            wechat:'💺'
        },
        {
            classN:'emoji1f4bb',
            wechat:'💻'
        },
        {
            classN:'emoji270f',
            wechat:'✏'
        },
        {
            classN:'emoji1f4ce',
            wechat:'📎'
        },
        {
            classN:'emoji1f4bc',
            wechat:'💼'
        },
        {
            classN:'emoji1f4bd',
            wechat:'💽'
        },
        {
            classN:'emoji1f4be',
            wechat:'💾'
        },
        {
            classN:'emoji1f4bf',
            wechat:'💿'
        },
        {
            classN:'emoji1f4c0',
            wechat:'📀'
        },
        {
            classN:'emoji2702',
            wechat:'✂'
        },
        {
            classN:'emoji1f4cd',
            wechat:'📍'
        },
        {
            classN:'emoji1f4c3',
            wechat:'📃'
        },
        {
            classN:'emoji1f4c4',
            wechat:'📄'
        },
        {
            classN:'emoji1f4c5',
            wechat:'📅'
        },
        {
            classN:'emoji1f4c1',
            wechat:'📁'
        },
        {
            classN:'emoji1f4c2',
            wechat:'📂'
        },
        {
            classN:'emoji1f4d3',
            wechat:'📓'
        },
        {
            classN:'emoji1f4d6',
            wechat:'📖'
        },
        {
            classN:'emoji1f4d4',
            wechat:'📔'
        },
        {
            classN:'emoji1f4d5',
            wechat:'📕'
        },
        {
            classN:'emoji1f4d7',
            wechat:'📗'
        },
        {
            classN:'emoji1f4d8',
            wechat:'📘'
        },
        {
            classN:'emoji1f4d9',
            wechat:'📙'
        },
        {
            classN:'emoji1f4da',
            wechat:'📚'
        },
        {
            classN:'emoji1f4db',
            wechat:'📛'
        },
        {
            classN:'emoji1f4dc',
            wechat:'📜'
        },
        {
            classN:'emoji1f4cb',
            wechat:'📋'
        },
        {
            classN:'emoji1f4c6',
            wechat:'📆'
        },
        {
            classN:'emoji1f4ca',
            wechat:'📊'
        },
        {
            classN:'emoji1f4c8',
            wechat:'📈'
        },
        {
            classN:'emoji1f4c9',
            wechat:'📉'
        },
        {
            classN:'emoji1f4c7',
            wechat:'📇'
        },
        {
            classN:'emoji1f4cc',
            wechat:'📌'
        },
        {
            classN:'emoji1f4d2',
            wechat:'📒'
        },
        {
            classN:'emoji1f4cf',
            wechat:'📏'
        },
        {
            classN:'emoji1f4d0',
            wechat:'📐'
        },
        {
            classN:'emoji1f4d1',
            wechat:'📑'
        },
        {
            classN:'emoji1f3bd',
            wechat:'🎽'
        },
        {
            classN:'emoji26be',
            wechat:'⚾'
        },
        {
            classN:'emoji26f3',
            wechat:'⛳'
        },
        {
            classN:'emoji1f3be',
            wechat:'🎾'
        },
        {
            classN:'emoji26bd',
            wechat:'⚽'
        },
        {
            classN:'emoji1f3bf',
            wechat:'🎿'
        },
        {
            classN:'emoji1f3c0',
            wechat:'🏀'
        },
        {
            classN:'emoji1f3c1',
            wechat:'🏁'
        },
        {
            classN:'emoji1f3c2',
            wechat:'🏂'
        },
        {
            classN:'emoji1f3c3',
            wechat:'🏃'
        },
        {
            classN:'emoji1f3c4',
            wechat:'🏄'
        },
        {
            classN:'emoji1f3c6',
            wechat:'🏆'
        },
        {
            classN:'emoji1f3c8',
            wechat:'🏈'
        },
        {
            classN:'emoji1f3ca',
            wechat:'🏊'
        },
        {
            classN:'emoji1f683',
            wechat:'🚃'
        },
        {
            classN:'emoji1f687',
            wechat:'🚇'
        },
        {
            classN:'emoji24c2',
            wechat:'Ⓜ'
        },
        {
            classN:'emoji1f684',
            wechat:'🚄'
        },
        {
            classN:'emoji1f685',
            wechat:'🚅'
        },
        {
            classN:'emoji1f697',
            wechat:'🚗'
        },
        {
            classN:'emoji1f699',
            wechat:'🚙'
        },
        {
            classN:'emoji1f68c',
            wechat:'🚌'
        },
        {
            classN:'emoji1f68f',
            wechat:'🚏'
        },
        {
            classN:'emoji1f6a2',
            wechat:'🚢'
        },
        {
            classN:'emoji2708',
            wechat:'✈'
        },
        {
            classN:'emoji26f5',
            wechat:'⛵'
        },
        {
            classN:'emoji1f689',
            wechat:'🚉'
        },
        {
            classN:'emoji1f680',
            wechat:'🚀'
        },
        {
            classN:'emoji1f6a4',
            wechat:'🚤'
        },
        {
            classN:'emoji1f695',
            wechat:'🚕'
        },
        {
            classN:'emoji1f69a',
            wechat:'🚚'
        },
        {
            classN:'emoji1f692',
            wechat:'🚒'
        },
        {
            classN:'emoji1f691',
            wechat:'🚑'
        },
        {
            classN:'emoji1f693',
            wechat:'🚓'
        },
        {
            classN:'emoji26fd',
            wechat:'⛽'
        },
        {
            classN:'emoji1f17f',
            wechat:'🅿'
        },
        {
            classN:'emoji1f6a5',
            wechat:'🚥'
        },
        {
            classN:'emoji1f6a7',
            wechat:'🚧'
        },
        {
            classN:'emoji1f6a8',
            wechat:'🚨'
        },
        {
            classN:'emoji2668',
            wechat:'♨'
        },
        {
            classN:'emoji26fa',
            wechat:'⛺'
        },
        {
            classN:'emoji1f3a0',
            wechat:'🎠'
        },
        {
            classN:'emoji1f3a1',
            wechat:'🎡'
        },
        {
            classN:'emoji1f3a2',
            wechat:'🎢'
        },
        {
            classN:'emoji1f3a3',
            wechat:'🎣'
        },
        {
            classN:'emoji1f3a4',
            wechat:'🎤'
        },
        {
            classN:'emoji1f3a5',
            wechat:'🎥'
        },
        {
            classN:'emoji1f3a6',
            wechat:'🎦'
        },
        {
            classN:'emoji1f3a7',
            wechat:'🎧'
        },
        {
            classN:'emoji1f3a8',
            wechat:'🎨'
        },
        {
            classN:'emoji1f3a9',
            wechat:'🎩'
        },
        {
            classN:'emoji1f3aa',
            wechat:'🎪'
        },
        {
            classN:'emoji1f3ab',
            wechat:'🎫'
        },
        {
            classN:'emoji1f3ac',
            wechat:'🎬'
        },
        {
            classN:'emoji1f3ad',
            wechat:'🎭'
        },
        {
            classN:'emoji1f3ae',
            wechat:'🎮'
        },
        {
            classN:'emoji1f004',
            wechat:'🀄'
        },
        {
            classN:'emoji1f3af',
            wechat:'🎯'
        },
        {
            classN:'emoji1f3b0',
            wechat:'🎰'
        },
        {
            classN:'emoji1f3b1',
            wechat:'🎱'
        },
        {
            classN:'emoji1f3b2',
            wechat:'🎲'
        },
        {
            classN:'emoji1f3b3',
            wechat:'🎳'
        },
        {
            classN:'emoji1f3b4',
            wechat:'🎴'
        },
        {
            classN:'emoji1f0cf',
            wechat:'🃏'
        },
        {
            classN:'emoji1f3b5',
            wechat:'🎵'
        },
        {
            classN:'emoji1f3b6',
            wechat:'🎶'
        },
        {
            classN:'emoji1f3b7',
            wechat:'🎷'
        },
        {
            classN:'emoji1f3b8',
            wechat:'🎸'
        },
        {
            classN:'emoji1f3b9',
            wechat:'🎹'
        },
        {
            classN:'emoji1f3ba',
            wechat:'🎺'
        },
        {
            classN:'emoji1f3bb',
            wechat:'🎻'
        },
        {
            classN:'emoji1f3bc',
            wechat:'🎼'
        },
        {
            classN:'emoji303d',
            wechat:'〽'
        },
        {
            classN:'emoji1f4f7',
            wechat:'📷'
        },
        {
            classN:'emoji1f4f9',
            wechat:'📹'
        },
        {
            classN:'emoji1f4fa',
            wechat:'📺'
        },
        {
            classN:'emoji1f4fb',
            wechat:'📻'
        },
        {
            classN:'emoji1f4fc',
            wechat:'📼'
        },
        {
            classN:'emoji1f48b',
            wechat:'💋'
        },
        {
            classN:'emoji1f48c',
            wechat:'💌'
        },
        {
            classN:'emoji1f48d',
            wechat:'💍'
        },
        {
            classN:'emoji1f48e',
            wechat:'💎'
        },
        {
            classN:'emoji1f48f',
            wechat:'💏'
        },
        {
            classN:'emoji1f490',
            wechat:'💐'
        },
        {
            classN:'emoji1f491',
            wechat:'💑'
        },
        {
            classN:'emoji1f492',
            wechat:'💒'
        },
        {
            classN:'emoji1f51e',
            wechat:'🔞'
        },
        {
            classN:'emojia9',
            wechat:'©'
        },
        {
            classN:'emojiae',
            wechat:'®'
        },
        {
            classN:'emoji2122',
            wechat:'™'
        },
        {
            classN:'emoji2139',
            wechat:'ℹ'
        },
        {
            classN:'emoji1f51f',
            wechat:'🔟'
        },
        {
            classN:'emoji1f4f6',
            wechat:'📶'
        },
        {
            classN:'emoji1f4f3',
            wechat:'📳'
        },
        {
            classN:'emoji1f4f4',
            wechat:'📴'
        },
        {
            classN:'emoji1f354',
            wechat:'🍔'
        },
        {
            classN:'emoji1f359',
            wechat:'🍙'
        },
        {
            classN:'emoji1f370',
            wechat:'🍰'
        },
        {
            classN:'emoji1f35c',
            wechat:'🍜'
        },
        {
            classN:'emoji1f35e',
            wechat:'🍞'
        },
        {
            classN:'emoji1f373',
            wechat:'🍳'
        },
        {
            classN:'emoji1f366',
            wechat:'🍦'
        },
        {
            classN:'emoji1f35f',
            wechat:'🍟'
        },
        {
            classN:'emoji1f361',
            wechat:'🍡'
        },
        {
            classN:'emoji1f358',
            wechat:'🍘'
        },
        {
            classN:'emoji1f35a',
            wechat:'🍚'
        },
        {
            classN:'emoji1f35d',
            wechat:'🍝'
        },
        {
            classN:'emoji1f35b',
            wechat:'🍛'
        },
        {
            classN:'emoji1f362',
            wechat:'🍢'
        },
        {
            classN:'emoji1f363',
            wechat:'🍣'
        },
        {
            classN:'emoji1f371',
            wechat:'🍱'
        },
        {
            classN:'emoji1f372',
            wechat:'🍲'
        },
        {
            classN:'emoji1f367',
            wechat:'🍧'
        },
        {
            classN:'emoji1f356',
            wechat:'🍖'
        },
        {
            classN:'emoji1f365',
            wechat:'🍥'
        },
        {
            classN:'emoji1f360',
            wechat:'🍠'
        },
        {
            classN:'emoji1f355',
            wechat:'🍕'
        },
        {
            classN:'emoji1f357',
            wechat:'🍗'
        },
        {
            classN:'emoji1f368',
            wechat:'🍨'
        },
        {
            classN:'emoji1f369',
            wechat:'🍩'
        },
        {
            classN:'emoji1f36a',
            wechat:'🍪'
        },
        {
            classN:'emoji1f36b',
            wechat:'🍫'
        },
        {
            classN:'emoji1f36c',
            wechat:'🍬'
        },
        {
            classN:'emoji1f36d',
            wechat:'🍭'
        },
        {
            classN:'emoji1f36e',
            wechat:'🍮'
        },
        {
            classN:'emoji1f36f',
            wechat:'🍯'
        },
        {
            classN:'emoji1f364',
            wechat:'🍤'
        },
        {
            classN:'emoji1f374',
            wechat:'🍴'
        },
        {
            classN:'emoji2615',
            wechat:'☕'
        },
        {
            classN:'emoji1f378',
            wechat:'🍸'
        },
        {
            classN:'emoji1f37a',
            wechat:'🍺'
        },
        {
            classN:'emoji1f375',
            wechat:'🍵'
        },
        {
            classN:'emoji1f376',
            wechat:'🍶'
        },
        {
            classN:'emoji1f377',
            wechat:'🍷'
        },
        {
            classN:'emoji1f37b',
            wechat:'🍻'
        },
        {
            classN:'emoji1f379',
            wechat:'🍹'
        },
        {
            classN:'emoji2197',
            wechat:'↗'
        },
        {
            classN:'emoji2198',
            wechat:'↘'
        },
        {
            classN:'emoji2196',
            wechat:'↖'
        },
        {
            classN:'emoji2199',
            wechat:'↙'
        },
        {
            classN:'emoji2934',
            wechat:'⤴'
        },
        {
            classN:'emoji2935',
            wechat:'⤵'
        },
        {
            classN:'emoji2194',
            wechat:'↔'
        },
        {
            classN:'emoji2195',
            wechat:'↕'
        },
        {
            classN:'emoji2b06',
            wechat:'⬆'
        },
        {
            classN:'emoji2b07',
            wechat:'⬇'
        },
        {
            classN:'emoji27a1',
            wechat:'➡'
        },
        {
            classN:'emoji2b05',
            wechat:'⬅'
        },
        {
            classN:'emoji25b6',
            wechat:'▶'
        },
        {
            classN:'emoji25c0',
            wechat:'◀'
        },
        {
            classN:'emoji23000000000',
            wechat:'⏩'
        },
        {
            classN:'emoji23ea',
            wechat:'⏪'
        },
        {
            classN:'emoji23eb',
            wechat:'⏫'
        },
        {
            classN:'emoji23ec',
            wechat:'⏬'
        },
        {
            classN:'emoji1f53a',
            wechat:'🔺'
        },
        {
            classN:'emoji1f53b',
            wechat:'🔻'
        },
        {
            classN:'emoji1f53c',
            wechat:'🔼'
        },
        {
            classN:'emoji1f53d',
            wechat:'🔽'
        },
        {
            classN:'emoji2b55',
            wechat:'⭕'
        },
        {
            classN:'emoji274c',
            wechat:'❌'
        },
        {
            classN:'emoji274e',
            wechat:'❎'
        },
        {
            classN:'emoji2757',
            wechat:'❗'
        },
        {
            classN:'emoji2049',
            wechat:'⁉'
        },
        {
            classN:'emoji203c',
            wechat:'‼'
        },
        {
            classN:'emoji2753',
            wechat:'❓'
        },
        {
            classN:'emoji2754',
            wechat:'❔'
        },
        {
            classN:'emoji2755',
            wechat:'❕'
        },
        {
            classN:'emoji3030',
            wechat:'〰'
        },
        {
            classN:'emoji27b0',
            wechat:'➰'
        },

        {
            classN:'emoji2764',
            wechat:'❤'
        },
        {
            classN:'emoji1f493',
            wechat:'💓'
        },
        {
            classN:'emoji1f494',
            wechat:'💔'
        },
        {
            classN:'emoji1f495',
            wechat:'💕'
        },
        {
            classN:'emoji1f496',
            wechat:'💖'
        },
        {
            classN:'emoji1f497',
            wechat:'💗'
        },
        {
            classN:'emoji1f498',
            wechat:'💘'
        },
        {
            classN:'emoji1f499',
            wechat:'💙'
        },
        {
            classN:'emoji1f49a',
            wechat:'💚'
        },
        {
            classN:'emoji1f49b',
            wechat:'💛'
        },
        {
            classN:'emoji1f49c',
            wechat:'💜'
        },
        {
            classN:'emoji1f49d',
            wechat:'💝'
        },
        {
            classN:'emoji1f49e',
            wechat:'💞'
        },
        {
            classN:'emoji1f49f',
            wechat:'💟'
        },
        {
            classN:'emoji2665',
            wechat:'♥'
        },
        {
            classN:'emoji2660',
            wechat:'♠'
        },
        {
            classN:'emoji2666',
            wechat:'♦'
        },
        {
            classN:'emoji2663',
            wechat:'♣'
        },
        {
            classN:'emoji1f6ac',
            wechat:'🚬'
        },
        {
            classN:'emoji1f6ad',
            wechat:'🚭'
        },
        {
            classN:'emoji267f',
            wechat:'♿'
        },
        {
            classN:'emoji1f6a9',
            wechat:'🚩'
        },
        {
            classN:'emoji26a0',
            wechat:'⚠'
        },
        {
            classN:'emoji26d4',
            wechat:'⛔'
        },
        {
            classN:'emoji267b',
            wechat:'♻'
        },
        {
            classN:'emoji1f6b2',
            wechat:'🚲'
        },
        {
            classN:'emoji1f6b6',
            wechat:'🚶'
        },
        {
            classN:'emoji1f6b9',
            wechat:'🚹'
        },
        {
            classN:'emoji1f6ba',
            wechat:'🚺'
        },
        {
            classN:'emoji1f6c0',
            wechat:'🛀'
        },
        {
            classN:'emoji1f6bb',
            wechat:'🚻'
        },
        {
            classN:'emoji1f6bd',
            wechat:'🚽'
        },
        {
            classN:'emoji1f6be',
            wechat:'🚾'
        },
        {
            classN:'emoji1f6bc',
            wechat:'🚼'
        },
        {
            classN:'emoji1f6aa',
            wechat:'🚪'
        },
        {
            classN:'emoji1f6ab',
            wechat:'🚫'
        },
        {
            classN:'emoji2714',
            wechat:'✔'
        },
        {
            classN:'emoji1f191',
            wechat:'🆑'
        },
        {
            classN:'emoji1f192',
            wechat:'🆒'
        },
        {
            classN:'emoji1f193',
            wechat:'🆓'
        },
        {
            classN:'emoji1f194',
            wechat:'🆔'
        },
        {
            classN:'emoji1f195',
            wechat:'🆕'
        },
        {
            classN:'emoji1f196',
            wechat:'🆖'
        },
        {
            classN:'emoji1f197',
            wechat:'🆗'
        },
        {
            classN:'emoji1f198',
            wechat:'🆘'
        },
        {
            classN:'emoji1f199',
            wechat:'🆙'
        },
        {
            classN:'emoji1f19a',
            wechat:'🆚'
        },
        {
            classN:'emoji1f201',
            wechat:'🈁'
        },
        {
            classN:'emoji1f202',
            wechat:'🈂'
        },
        {
            classN:'emoji1f232',
            wechat:'🈲'
        },
        {
            classN:'emoji1f233',
            wechat:'🈳'
        },
        {
            classN:'emoji1f234',
            wechat:'🈴'
        },
        {
            classN:'emoji1f235',
            wechat:'🈵'
        },
        {
            classN:'emoji1f236',
            wechat:'🈶'
        },
        {
            classN:'emoji1f21a',
            wechat:'🈚'
        },
        {
            classN:'emoji1f237',
            wechat:'🈷'
        },
        {
            classN:'emoji1f238',
            wechat:'🈸'
        },
        {
            classN:'emoji1f239',
            wechat:'🈹'
        },
        {
            classN:'emoji1f22f',
            wechat:'🈯'
        },
        {
            classN:'emoji1f23a',
            wechat:'🈺'
        },
        {
            classN:'emoji3299',
            wechat:'㊙'
        },
        {
            classN:'emoji3297',
            wechat:'㊗'
        },
        {
            classN:'emoji1f250',
            wechat:'🉐'
        },
        {
            classN:'emoji1f251',
            wechat:'🉑'
        },
        {
            classN:'emoji2795',
            wechat:'➕'
        },
        {
            classN:'emoji2796',
            wechat:'➖'
        },
        {
            classN:'emoji2716',
            wechat:'✖'
        },
        {
            classN:'emoji2797',
            wechat:'➗'
        },
        {
            classN:'emoji1f4a0',
            wechat:'💠'
        },
        {
            classN:'emoji1f4a1',
            wechat:'💡'
        },
        {
            classN:'emoji1f4a2',
            wechat:'💢'
        },
        {
            classN:'emoji1f4a3',
            wechat:'💣'
        },
        {
            classN:'emoji1f4a4',
            wechat:'💤'
        },
        {
            classN:'emoji1f4a5',
            wechat:'💥'
        },
        {
            classN:'emoji1f4a6',
            wechat:'💦'
        },
        {
            classN:'emoji1f4a7',
            wechat:'💧'
        },
        {
            classN:'emoji1f4a8',
            wechat:'💨'
        },
        {
            classN:'emoji1f4a9',
            wechat:'💩'
        },
        {
            classN:'emoji1f4aa',
            wechat:'💪'
        },
        {
            classN:'emoji1f4ab',
            wechat:'💫'
        },
        {
            classN:'emoji1f4ac',
            wechat:'💬'
        },
        {
            classN:'emoji2728',
            wechat:'✨'
        },
        {
            classN:'emoji2734',
            wechat:'✴'
        },
        {
            classN:'emoji2733',
            wechat:'✳'
        },
        {
            classN:'emoji26aa',
            wechat:'⚪'
        },
        {
            classN:'emoji26ab',
            wechat:'⚫'
        },
        {
            classN:'emoji1f534',
            wechat:'🔴'
        },
        {
            classN:'emoji1f535',
            wechat:'🔵'
        },
        {
            classN:'emoji1f532',
            wechat:'🔲'
        },
        {
            classN:'emoji1f533',
            wechat:'🔳'
        },
        {
            classN:'emoji2b50',
            wechat:'⭐'
        },
        {
            classN:'emoji2b1c',
            wechat:'⬜'
        },
        {
            classN:'emoji2b1b',
            wechat:'⬛'
        },
        {
            classN:'emoji25ab',
            wechat:'▫'
        },
        {
            classN:'emoji25aa',
            wechat:'▪'
        },
        {
            classN:'emoji25fd',
            wechat:'◽'
        },
        {
            classN:'emoji25fe',
            wechat:'◾'
        },
        {
            classN:'emoji25fb',
            wechat:'◻'
        },
        {
            classN:'emoji25fc',
            wechat:'◼'
        },
        {
            classN:'emoji1f536',
            wechat:'🔶'
        },
        {
            classN:'emoji1f537',
            wechat:'🔷'
        },
        {
            classN:'emoji1f538',
            wechat:'🔸'
        },
        {
            classN:'emoji1f539',
            wechat:'🔹'
        },
        {
            classN:'emoji2747',
            wechat:'❇'
        },
        {
            classN:'emoji1f4ae',
            wechat:'💮'
        },
        {
            classN:'emoji1f4af',
            wechat:'💯'
        },
        {
            classN:'emoji21a9',
            wechat:'↩'
        },
        {
            classN:'emoji21aa',
            wechat:'↪'
        },
        {
            classN:'emoji1f503',
            wechat:'🔃'
        },
        {
            classN:'emoji1f50a',
            wechat:'🔊'
        },
        {
            classN:'emoji1f50b',
            wechat:'🔋'
        },
        {
            classN:'emoji1f50c',
            wechat:'🔌'
        },
        {
            classN:'emoji1f50d',
            wechat:'🔍'
        },
        {
            classN:'emoji1f50e',
            wechat:'🔎'
        },
        {
            classN:'emoji1f512',
            wechat:'🔒'
        },
        {
            classN:'emoji1f513',
            wechat:'🔓'
        },
        {
            classN:'emoji1f50f',
            wechat:'🔏'
        },
        {
            classN:'emoji1f510',
            wechat:'🔐'
        },
        {
            classN:'emoji1f511',
            wechat:'🔑'
        },
        {
            classN:'emoji1f514',
            wechat:'🔔'
        },
        {
            classN:'emoji2611',
            wechat:'☑'
        },
        {
            classN:'emoji1f518',
            wechat:'🔘'
        },
        {
            classN:'emoji1f516',
            wechat:'🔖'
        },
        {
            classN:'emoji1f517',
            wechat:'🔗'
        },
        {
            classN:'emoji1f519',
            wechat:'🔙'
        },
        {
            classN:'emoji1f51a',
            wechat:'🔚'
        },
        {
            classN:'emoji1f51b',
            wechat:'🔛'
        },
        {
            classN:'emoji1f51c',
            wechat:'🔜'
        },
        {
            classN:'emoji1f51d',
            wechat:'🔝'
        },
        {
            classN:'emoji2705',
            wechat:'✅'
        },
        {
            classN:'emoji270a',
            wechat:'✊'
        },
        {
            classN:'emoji270b',
            wechat:'✋'
        },
        {
            classN:'emoji270c',
            wechat:'✌'
        },
        {
            classN:'emoji1f44a',
            wechat:'👊'
        },
        {
            classN:'emoji1f44d',
            wechat:'👍'
        },
        {
            classN:'emoji261d',
            wechat:'☝'
        },
        {
            classN:'emoji1f446',
            wechat:'👆'
        },
        {
            classN:'emoji1f447',
            wechat:'👇'
        },
        {
            classN:'emoji1f448',
            wechat:'👈'
        },
        {
            classN:'emoji1f449',
            wechat:'👉'
        },
        {
            classN:'emoji1f44b',
            wechat:'👋'
        },
        {
            classN:'emoji1f44f',
            wechat:'👏'
        },
        {
            classN:'emoji1f44c',
            wechat:'👌'
        },
        {
            classN:'emoji1f44e',
            wechat:'👎'
        },
        {
            classN:'emoji1f450',
            wechat:'👐'
        },

    ];//表情

    return emoji;
}