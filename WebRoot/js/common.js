var _contextPath='';
var _token='';

Date.prototype.format = function(fmt) {
	var o = {   
		"M+" : this.getMonth()+1,                 //月份   
		"d+" : this.getDate(),                    //日   
		"h+" : this.getHours(),                   //小时   
		"m+" : this.getMinutes(),                 //分   
		"s+" : this.getSeconds(),                 //秒   
		"q+" : Math.floor((this.getMonth()+3)/3), //季度   
		"S"  : this.getMilliseconds()             //毫秒   
	};   
	if(/(y+)/.test(fmt))   
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	for(var k in o)   
		if(new RegExp("("+ k +")").test(fmt))   
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	return fmt;   
}
String.prototype.toDateString=function() {
	var re = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
	var _str=this;
	if (re.test(_str)) {
		_str=_str.replace(re,"$1-$2-$3 $4:$5:$6");
	}
	return _str;
}

String.prototype.toDateString2=function() {
	var re = /(\d{4})(\d{2})(\d{2})/;
	var _str=this;
	if (re.test(_str)) {
		_str=_str.replace(re,"$1-$2-$3");
	}
	return _str;
}

String.prototype.toDateString3=function() {
	var re = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
	var _str=this;
	if (re.test(_str)) {
		_str=_str.replace(re,"$1-$2-$3 $4:$5");
	}
	return _str;
}

String.prototype.removeAllSpace=function(){
	return this.replace(/\s+/g, "");
}


layer.config({
    extend: ['skin/layer.rc.css'] //加载新皮肤
});

var infoUtil={
	_index:null,
	alert:function(_msg,_callBack) {
		this._index=layer.alert(_msg, {icon:-1, shift:-1,title:'温馨提示',closeBtn:0},function(index){			
			layer.close(index);
			if (_callBack) _callBack();
		});
	},
	info:function(_msg,_callBack) {
		this._index=layer.alert(_msg, {icon:-1, shift:-1,title:'温馨提示',closeBtn:0},function(index){
			layer.close(index);
			if (_callBack) _callBack();
		});
	},
	confirm:function(_msg,_options,_yes,_cancel) {
		this._index=layer.confirm(_msg,_options,_yes,_cancel);		
	},
	close:function(){	
		if (this._index==null) {
			return;
		}
		layer.close(this._index);
		this._index=null;
	},
	submit:function(){ //页面敲回车触发submit，submit时，有些窗口不能关闭，所以此处不加入关闭操作，需各自调用layer.close(...)，直接调用infoUtil.close()会失败，因为已经置为null了
		if (this._index==null) {			
			return;
		}
		var _t=this._index;
		this._index=null; //不能再click之后设为null，click会有延迟，有时会导致很快出现的另外一个窗口获得的_index被置为null
		$("#layui-layer"+_t+" .layui-layer-btn0").click();	
	}
};

var _alert=window.alert;
window.alert=function(_msg,_callBack) {
	infoUtil.alert(_msg,_callBack);
}
window.info=function(_msg,_callBack) {
	if (!_callBack) {
		_callBack=function(){			
			$("#custCodeTemp,#money").setFocus();
		}
	}
	infoUtil.info(_msg,_callBack);
}

window.confirm=function(_msg,_options,_yes,_cancel) {
	infoUtil.confirm(_msg,_options,_yes,_cancel);
}

function rechargeConfirm(_formId,_phone,_money,_isFlow,isSlowCharge) {
	_isFlow=_isFlow || false;
	isSlowCharge = isSlowCharge || false;
	var _msg=null;
	if (_isFlow) {
		_msg="<font class='confirm-info'>充值号码：<font class='notice-red'>"+_phone+"</font><br/>充值流量：<font class='notice-red'>"+_money+"</font></font>";
	} else {
		var text = "元？";
		if(isSlowCharge) text="元？ 并知晓慢充细则?";
		_msg="<font class='confirm-info'>您确定为<font class='notice-red'>"+_phone+"</font><br/>充值<font class='notice-red'>"+_money+"</font>"+text+"</font>";
	}
	infoUtil.confirm(_msg,{
        title:'充值确认',
        closeBtn:0,
        shift:-1,
        btn: ['确定','取消'] //按钮
    },function(_index){
        infoUtil.close();
        $(_formId).submit();
    },function(){});
}

function depositConfirm(_formId,_money) {
	infoUtil.confirm("<font class='confirm-info'>您确定存款<font class='notice-red'>"+_money+"</font>元？</font>",{
        title:'存款确认',
        closeBtn:0,
        shift:-1,
        btn: ['确定','取消'] //按钮
    },function(_index){
        infoUtil.close();
        $(_formId).submit();
    },function(){});
}

function accountTransferConfirm(_formId,_phone,_money,_isCorrect) {
	_isCorrect=_isCorrect || false;
	if (_isCorrect) {
		infoUtil.confirm("<font class='confirm-info'>您确定对<font class='notice-red'>"+_phone+"</font><br/>进行<font class='notice-red'>"+_money+"</font>元冲正操作？</font>",{
	        title:'转账冲正确认',
	        closeBtn:0,
	        shift:-1,
	        btn: ['确定','取消'] //按钮
	    },function(_index){
	        infoUtil.close();
	        $(_formId).submit();
	    },function(){});
	} else {
		infoUtil.confirm("<font class='confirm-info'>您确定为<font class='notice-red'>"+_phone+"</font><br/>转账<font class='notice-red'>"+_money+"</font>元？</font>",{
	        title:'转账确认',
	        closeBtn:0,
	        shift:-1,
	        btn: ['确定','取消'] //按钮
	    },function(_index){
	        infoUtil.close();
	        $(_formId).submit();
	    },function(){});
	}
}

$(function(){
	$("body").keyup(function(e){
	    if (e.keyCode==13) {
	        infoUtil.submit();
	    }
	});
})

var _loadingIndex=null;
var loading={
	_index:null,
	show:function(){
		this._index=layer.load(1, {
			shift:-1,
		    shade: [0.1,'#fff'] //0.1透明度的白色背景
		});
	},
	hide:function(){
		if (this._index!=null) {
			layer.close(this._index);
		}
	}
};

$.ajaxSetup({
	timeout:120000, //120秒超时
	cache:false, //不缓存
	async:true, //异步
	dataType:"json", //默认返回json
	type:"POST" //默认post请求
});

function getToken() {
    $.ajax({
        url:_contextPath+"token",
        type:"GET",
        success:function(response){
            if (response.token) {
                _token=response.token;
                $('input[name=_token]').val(response.token);
            }
        },
        error:function(){
            alert("获取token失败，请刷新页面");
        }
    });
}

function $ajax(_options) {
	_options.notShowLoading=_options.notShowLoading || false;
	if (!_options.notShowLoading) loading.show();	
	var ajaxOptions={
		complete:function() {
			start();
			if (!_options.notShowLoading) loading.hide();
		},
		error:function(XMLHttpRequest,status){
			if (status=='timeout') {
				XMLHttpRequest.abort();
				getToken();
				alert("处理超时，请进行相关查询。");
			} else {
				alert("目前系统繁忙，请稍候再试。");
			}
		},
		fail:function(response){
			if (response.message==null) response.message="通讯错误，请稍后重试。";
            alert(response.message);
		}
	};
	for (var p in _options) {
		ajaxOptions[p]=_options[p];
	}
	ajaxOptions.success=function(response) {
		if (response.token) {
			_token=response.token;
			$('input[name=_token]').val(response.token);
		}
        if (!response.success) {
        	ajaxOptions.fail(response);
        } else {
        	if (response.updateAgentBalance) $("#_agtBalance").html(formatter.money(response.agentBalance));
        	_options.success(response);
        }
        if (_options.getToken) {
        	getToken();
    	}
	};
	window.setTimeout(function(){$.ajax(ajaxOptions);},1);
}

var sms={
	_windowIndex:null,
	_interval:null,
	_t:0,
	resend:{
		end:function(){
	        if (sms._interval==null) return;
	        window.clearInterval(sms._interval);
	        sms._interval=null;
	    },
	    start:function(){
	        sms._t=59;
	        $("#resend a").html("短信已发送..."+(parseInt(sms._t)+1)+"秒").unbind("click").show();        
	        sms._interval=window.setInterval(function(){
	            $("#resend a").html("短信已发送..."+sms._t+"秒");
	            sms._t-=1;
	            if (sms._t==0) {
	                sms.resend.end();
	                $("#resend a").html("点击重新获取短信").click(function(){
	                    $ajax({
	                        url:_contextPath+"sms/reSend", 
	                        dataType:'json',
	                        type:"post",
	                        success:function(){
	                            $("#resend a").hide();         
	                            infoUtil.info("短信已发送，请查收。",function(){
	                                $("#_smsNo").focus();
	                            });                            
	                        }
	                    });
	                });
	            }
	        },1000);
	    }
	},
	_authType:null,
	_valueField:null,
	_submitFunc:null,
	_cancelFunc:null,
	init:function(_authType,_valueField,_submitFunc,_cancelFunc){
		sms._authType=_authType;
		sms._valueField=_valueField;
		sms._submitFunc=_submitFunc;
		sms._cancelFunc=_cancelFunc;
	},
	show:function(){
		if ($("#_smsNo").length==0) {
			var _div=$('<div style="text-align:center;padding:10px 0px" id="_popCode"></div>').append($('<input class="auth-input" maxlength="6" id="_smsNo"/>')).append($('<div id="resend" style="display:none;height:30px;line-height:30px"><a href="javascript:void(0)" style="color:red;cursor:pointer">短信已发送...</a></div>'));
            $("body").append(_div.hide());
			$("#_smsNo").onlyLetterAndNumber().addKeyControl(null,null,-1,function(){
				sms.resend.end();
				$("#_smsNo").blur();
				$(sms._valueField).val($("#_smsNo").val());
				$("#_smsNo").val("");
				layer.close(sms._windowIndex);
				sms._submitFunc();
			});
		}
		(sms._authType==5)?$("#resend").hide():$("#resend").show();
		sms._windowIndex=layer.open({
			type: 1,
			title:"请输入"+((sms._authType==5)?"动态口令":"短信授权码"),
			shade:  0.3, //遮罩透明度
	        shift: -1,
	        btn: ['确定','取消'], //按钮
	        content:$("#_popCode"),
	        yes:function(){
	        	sms.resend.end();
				$(sms._valueField).val($("#_smsNo").val());
				$("#_smsNo").val("");
				layer.close(sms._windowIndex);
				sms._submitFunc();
	        },
	        cancel:function(){
                sms.resend.end();
                if (sms._cancelFunc) sms._cancelFunc();
            }
		});
		$("#_smsNo").focus();
		sms.resend.start();
	},
	close:function(){
		layer.close(sms._windowIndex);
	}
};

function showSwitchSmsLogin(_info) {
	infoUtil.confirm(_info+"<br/><br/>是否改为短信验证登录？",{
	    title:'温馨提示',
	    shift:-1,
	    btn: ['是','否']
	},function(){
		layer.closeAll();
	    if ($("#agtPwd").length==0) {
	    	var _input=$('<input type="password" style="height: 28px;line-height: 28px;color: #000;font-size:14px;margin:20px 40px" maxlength="15" id="agtPwd"/>');
            $("body").append(_input.hide());
            $("#agtPwd").addKeyControl(null,null,-1,function(){
            	if ($("#agtPwd").val()=="") {
	        		alert("交易密码不能为空");
	        		return false;
	        	}
	        	$ajax({
	    	        url:_contextPath+"password/sms", 
	    	        data:{
	    	            agtCode:$("#loginId").val(),
	    	            agtPwd:$("#agtPwd").val(),
	    	            _token:_token
	    	        },
	    	        success:function(){
	    	        	layer.close(_index);
	    	            alert("已改为短信验证登录，请重新登录。",function(){
	    	                showVCode();
	    	            });
	    	        }
	    	    });
			});
	    }
	    $("#agtPwd").val("");	    
	    var _index=layer.open({
			type: 1,
			title:"请输入交易密码",
			shade:  0.3, //遮罩透明度
	        shift: -1,
	        btn: ['确定','取消'], //按钮
	        content:$("#agtPwd"),
	        yes:function(){
	        	if ($("#agtPwd").val()=="") {
	        		alert("交易密码不能为空");
	        		return false;
	        	}
	        	$ajax({
	    	        url:_contextPath+"password/sms", 
	    	        data:{
	    	            agtCode:$("#loginId").val(),
	    	            agtPwd:$("#agtPwd").val(),
	    	            _token:_token
	    	        },
	    	        success:function(){
	    	        	layer.close(_index);
	    	            alert("已改为短信验证登录，请重新登录。",function(){
	    	                showVCode();
	    	            });
	    	        }
	    	    });
	        },
	        cancel:function(){                
            }
		});
		$("#agtPwd").focus();
	},function(){});
}

var _smsWindowIndex=null;
var $form={
	complete:null,
	showVcode:null,
	submit:function(_formObject,_callback,_notCheckValid){
		_notCheckValid=_notCheckValid || false;
		var _valid=false;
		if (_notCheckValid) {
			_valid=true;
		} else {
			var _valid=Validator.Validate($(_formObject)[0], 4);
		}		
		if (_valid) {
			loading.show();
			//密码加密
			var pwd = $("#password").val();
			if(pwd && pwd.length!=32){
				var hash = md5(pwd);
				$("#password").val(hash);
			}
			$(_formObject).ajaxSubmit({
				dataType:'json',
	            success:function(response){
	            	if (response.token) {
	            		_token=response.token;
	            		$('input[name=_token]').val(response.token);
	            	}
	            	if (response.message && response.message.indexOf("登录成功")==-1) {
	            		if ($form.showVcode!=null) {
	            			$form.showVcode();
	            		}
	            		if (!response.success) alert(response.message);
	            	}
	            	if ((response.authType || -1)==-1) {
	            		if (response.success) {
	            			if (response.updateAgentBalance) $("#_agtBalance").html(formatter.money(response.agentBalance));
		                    if (_callback) _callback(response);
		                }
	            	} else {
	            		if (response.authType==3 || response.authType==5) {
	            			infoUtil.alert(response.message,function(){
		            			//TODO 开启验证码输入框
	            				sms.init(response.authType,"#smsNo",function(){
	            					$(_formObject).submit();
	            				},null,true);
	            				sms.show();
		            		});
	            		} else {
	            			alert("不支持的授权方式");
	            		}
	            	}	                
	            },
	            error:function(XMLHttpRequest,status){
	    			if (status=='timeout') {
	    				XMLHttpRequest.abort();
	    				getToken();
	    				alert("处理超时，请进行相关查询。");
	    			} else {
	    				alert("目前系统繁忙，请稍候再试。");
	    			}
	    		},
	    		complete:function(){
	    			start();
	    			loading.hide();
	    			if ($form.complete) $form.complete();
	    		}
			});
		}
		return false;
	},
	clear:function(_form) {
		$(_form).find(":input").not(':radio, :button, :submit, :reset, :hidden, :text[disabled=disabled]').val('').removeAttr('selected').removeAttr('checked');
	},
	reset:function(_form) {
		$(_form)[0].reset();
		$('input[name=_token]').val(_token); //IE6、7、8下，隐藏域也会被重置，所以要重新设置下值
		$("#custCode").val("");
		$("#smsNo").val("");
		rechargeCtrl.reset();
		$($("#custCode").parent().parent().find(".info")).html("*");
	},
	confirm:function(_form,_msg,_title) {
		if (!Validator.Validate($(_form)[0], 2)) return;
		_title=_title || "操作确认";
		infoUtil.confirm(_msg,{
	        title:_title,
	        closeBtn:0,
	        shift:-1,
	        btn: ['确定','取消'] //按钮
	    },function(_index){
	        infoUtil.close();
	        $(_form).submit();
	    },function(){});
	}
};

var formatter={
	money:function(num) {//参数说明：num 要格式化的数字 n 保留小数位
		if (num=="") return "/";
		var n=2;//保留4位小数
		num = String(Number(num).toFixed(n));
		var temp=num.split("\.");
	    var re = /(-?\d+)(\d{3})/;
	    while(re.test(temp[0])) temp[0] = temp[0].replace(re,"$1,$2")
	    return temp[0]+"."+temp[1];
	},
	phone:function(_phone) {
		var _ret=_phone.substr(0,3)+" "+_phone.substr(3,4)+" "+_phone.substr(7);
		return _ret;
	}
};

function onlyNumber(e) {
	var key=e.keyCode;
    if ((e.ctrlKey && key==86) || (key > 47 && key < 58) || (key > 95 && key < 106) || key == 8 || key == 13 || key == 9 || key == 40 || key == 37 || key == 39 || key == 46) {
        return true;
    }
    return false;
}

function onlyLetterAndNumber(key) {
    if ((key > 47 && key < 58) || (key>64 && key<91) || (key > 95 && key < 106) || key == 8 || key == 13 || key == 9 || key == 40 || key == 37 || key == 39 || key == 46) {
        return true;
    }
    return false;
}

function isMoneyMode(key,_notAccepPoint) {
	if (!_notAccepPoint && (key==190 || key==110)) {
		return true;
	}
    if ((key > 47 && key < 58) || (key > 95 && key < 106) || key == 8 || key == 13 || key == 9 || key == 40 || key == 37 || key == 39 || key == 46) {
        return true;
    }
    return false;
}

function addSpace(_input,key,_isBlur){
	_isBlur=_isBlur || false;
	if(key!=8){//排除Backspace键
		var value = $(_input).val();
		if (!_isBlur) {			
			if(value.length==3 || value.length==8){
				$(_input).val(value+" ");
			}
			if (value.length==11 && value.indexOf(" ")==-1) {
				$(_input).val(value.substr(0,3)+" "+value.substr(3,4)+" "+value.substr(7,4)).showMobileOperator();
				
			}
		} else {
			if (value.length==11 && value.indexOf(" ")==-1) {
				$(_input).val(value.substr(0,3)+" "+value.substr(3,4)+" "+value.substr(7,4)).showMobileOperator();
			}
		}
	}
}

var phoneInputAutoFocus=true;
(function(z) {
	z.fn.setFocus=function(){
		for (var i=0;i<this.length;i++) {
			if ($(this[i]).length>0) {
				$(this[i])[0].focus();
			}
			break;
		}
		return this;
	};
	z.fn.onlyNumber=function(){
		$(this).keydown(function(e){return onlyNumber(e);});
		return this;
	};
	z.fn.onlyLetterAndNumber=function(){
		$(this).keydown(function(e){return onlyLetterAndNumber(e.keyCode);});
		return this;
	};
	z.fn.setPhoneInputMode=function(){
		for (var i=0;i<this.length;i++) {
			$(this[i]).keyup(function(e){
				addSpace(this,e.keyCode);
			}).keypress(function(e){
				addSpace(this,e.keyCode);
			}).blur(function(e){
				addSpace(this,e.keyCode,true);
			});
			if (phoneInputAutoFocus) $(this[i]).focus();
		}
		return this;
	};
	z.fn.setMoneyInputMode=function(_notAccepPoint){
		_notAccepPoint=_notAccepPoint || false;
		$(this).keydown(function(e){return isMoneyMode(e.keyCode,_notAccepPoint);});
		return this;
	};
	z.fn.linkInput=function(_input) {
		for (var i=0;i<this.length;i++) {
			$(this[i]).keyup(function(e){
				$(_input).val($(this).val().removeAllSpace());
			}).keypress(function(e){
				$(_input).val($(this).val().removeAllSpace());
			}).blur(function(){
				$(_input).val($(this).val().removeAllSpace());
			});
		}
		return this;
	};
	z.fn.addKeyControl=function(_prev,_next,_maxValueLength,_nextFunc,_autoExecuteNextFunc){
		_maxValueLength=_maxValueLength || 0;
		_autoExecuteNextFunc=_autoExecuteNextFunc || false;
		_nextFunc=_nextFunc || null;
		for (var i=0;i<this.length;i++) {
			$(this[i]).keyup(function(e){
				var key = e.keyCode;
				if (key==13) {
					 if (_next!=null) {
						 $(_next).focus();
					 } else {
						 $(this).blur();
						 $("#_focus").focus();
					 }
					 if (_nextFunc!=null) {
						 _nextFunc();
					 }
				} else {
					if (_prev!=null && key == 38) {
						 $(_prev).focus();
					 } else if (_next!=null && (key < 37 || key > 40)) {
						 if (_maxValueLength>0 && $(this).val().length == _maxValueLength) {
							 $(_next).focus();
							 if (_nextFunc!=null) {
								 if (_autoExecuteNextFunc) _nextFunc();
							 }
						 }
					 }
				}
				return false;
			});
		}
		return this;
	}
})(jQuery);

var rechargeCtrl={
		setMoney:function(_ctrl,_moneyInput){
			$(_moneyInput).val($(_ctrl).val());			
		},
		reset:function(){
			$("#rechargeType").val("");
		},
		setType:function(_ctrl){
			$("#rechargeType").val($(_ctrl).val());
		},
		moneyInputBlur:function(_input) {
			var _m=$(_input).val();
		    if (_m=="") return;
		    $("input[name=money_group][value="+_m+"]").prop("checked",true);
		}
};

function showReturnInfo(_info) {
	$("#returnInfo").show();
	for (var p in _info) {
		$("#returnInfo td.field-value[name="+p+"]").html(_info[p]);
	}
}

function _refreshBalance() {
	$ajax({
		url:_contextPath+"balance",
		type:"POST",
		data:{
			_token:_token
		},
		success:function(response){
			$("#_agtBalance").html(formatter.money(response.data.accountBlance));
		}
	});
}

function refreshBalance() {
	if (_token=="") {
		$.ajax({
	        url:_contextPath+"token",
	        type:"GET",
	        success:function(response){
	            if (response.token) {
	                _token=response.token;
	                $('input[name=_token]').val(response.token);
	            }
	            _refreshBalance();
	        },
	        error:function(){
	            alert("获取token失败，请刷新页面");
	        }
	    });
	} else {
		_refreshBalance();
	}	
}

var hdLocker={
	isLogin:false,
	_newToken:function(_length) {
		var rnd="";
		for (var i=0;i<_length;i++)
			rnd+=Math.floor(Math.random()*10);
		return rnd;
	},
	softDog:{
		rc:{
			write:function(_valueField) {
				try {
					var token=hdLocker._newToken(12);
					var DevicePath,mylen,ret;
					var s_simnew1 = new ActiveXObject("Syunew3A.s_simnew3");
					DevicePath = s_simnew1.FindPort(0);
					if (s_simnew1.LastError!= 0 ){
						hdLocker.showError ( "未发现加密锁，请插入加密锁",true);
		                return false;
		            }else{
		                mylen = s_simnew1.YWriteString(token, 21, "ffffffff", "ffffffff", DevicePath);
		                if( s_simnew1.LastError!= 0 ){ 
		                	hdLocker.showError ("写入失败3",true);
		                    return false;
		                }
		                s_simnew1.SetBuf(mylen, 0);
		                ret = s_simnew1.YWriteEx(20, 1, "ffffffff", "ffffffff",DevicePath);
		                if( ret != 0 ){ 
		                	hdLocker.showError ( "写入失败4",true);
		                    return false;
		                }
		            }
					$(_valueField).val(token);
					return true;
				} catch(e) {
					hdLocker.showError("请安装加密狗驱动及插入正确的加密狗");
					return false;
				}
			},
			read:function() {
				var _token=null;
				try {
			        var DevicePath,mylen,ret;
			        var s_simnew31=new ActiveXObject("Syunew3A.s_simnew3");
			        DevicePath = s_simnew31.FindPort(0);//'来查找加密锁
			        if ( s_simnew31.LastError!= 0 ) {
			        	hdLocker.showError ("未发现加密锁，请插入加密锁",true);
			        } else {
			            //获到设置在锁中的用户密码,,使用默认的读密码"FFFFFFFF","FFFFFFFF"
			            ret=s_simnew31.YReadEx(20,1,"ffffffff","ffffffff",DevicePath);
			            mylen =s_simnew31.GetBuf(0);
			            _token=s_simnew31.YReadString(21,mylen,"ffffffff", "ffffffff", DevicePath);
			            if( s_simnew31.LastError!= 0 ) {
			            	hdLocker.showError("获取加密锁信息错误，错误码是："+s_simnew31.LastError.toString());
			                return _token;
			            }
			        }
			    } catch(e)  {
			    	hdLocker.showError("请安装加密狗驱动及插入正确的加密狗");
			    }
			    return _token;
			}
		},
		ek:{
			read:function(){
				var _token=null;
				try{
		            var ePass = new ActiveXObject("ET99_FULL.ET99Full.1");
		            //找锁
		            var ePassPID = "1F5E9D1C";
		            var ePassNum = ePass.FindToken(ePassPID);
		            ePass.OpenToken(ePassPID,1);
		            var ePassSN = ePass.GetSN();
		            _token=ePassSN;
		            ePass.CloseToken();
		        }catch(error) {
		        	hdLocker.showError("请安装EK-KEY驱动及插入正确的EK-KEY");
		        }
		        return _token;
			},
			write:function(_valueField) {
				var _token=hdLocker.softDog.ek.read();
				if (_token==null) return false;
				$(_valueField).val(_token);
	        	return true;
			}
		}
	},
	machine:{
		rc:{
			read:function(){
				var _mac=null;
				try {
		            var objMac=new ActiveXObject("grain.mac");
		            _mac=objMac.getMac();
		            objMac=null;
		        } catch(e) {
		        	hdLocker.showError("请先下载控件进行安装");
		        }
		        return _mac;
			},
			write:function(_valueField){
				var _mac=hdLocker.machine.rc.read();
				if (_mac==null) return false;
				$(_valueField).val(_mac);
	        	return true;
			}
		},
		ek:{
			read:function() {
				var _mac=null;
				try {
		        	var UserHD=new ActiveXObject("FeiXun.Computer");
		            var _v=UserHD.GetMacAddr()+UserHD.GetHDSerial();
		            _v=_v.replace(/\s+/g, "");
		            _mac=_v;
		        } catch(e) {
		        	hdLocker.showError("请先下载控件进行安装");
		        }
		        return _mac;
			},
			write:function(_valueField) {
				var _mac=hdLocker.machine.ek.read();
				if (_mac==null) return false;
				$(_valueField).val(_mac);
	        	return true;
			}
		}
	},
	writeSoftDogInfo:function(_valueField) {		
		if (document.location.href.indexOf("ek10010.com")==-1) {
			return hdLocker.softDog.rc.write(_valueField);
		} else {
			return hdLocker.softDog.ek.write(_valueField);
		}
	},
	readSoftDogInfo:function() {
		if (document.location.href.indexOf("ek10010.com")==-1) {
			return hdLocker.softDog.rc.read();
		} else {
			return hdLocker.softDog.ek.read();
		}
	},
	writeMachineInfo:function(_valueField) {
		if (document.location.href.indexOf("ek10010.com")==-1) {
			return hdLocker.machine.rc.write(_valueField);
		} else {
			return hdLocker.machine.ek.write(_valueField);
		}
	},
	getMachineInfo:function(){
		if (document.location.href.indexOf("ek10010.com")==-1) {
			return hdLocker.machine.rc.read();
		} else {
			return hdLocker.machine.ek.read();
		}
	},
	showError:function(_msg,_notAddInfo) {
		_notAddInfo=_notAddInfo || false;
		var _info="";
		if (_notAddInfo) {
			_info=_msg;
		} else {
			_info="1. "+_msg+"<br/>2. 浏览器更换为IE，或切换为IE模式（需先在IE下允许控件运行）<br/>3. 如果浏览器弹出运行控件提示，请放行";
		}
		if (hdLocker.isLogin) {
			showSwitchSmsLogin(_info);
		} else {
			alert(_info);
		}
	}
};

function showNotice(_title) {
	layer.open({
	    type: 1,
	    area: ['500px'],
	    border: [0],
	    title:_title,
	    shade:  0.1, //遮罩透明度
	    shift: 2,
	    content: $("#notice")
	});
}

var seconds;
var timerId;
function start(){
    seconds = 0;
    clearTimeout(timerId);			
	count();
}		
function count(){
    var leftseconds = 60 * 60 * 4 - seconds;
	if (leftseconds < 0) leftseconds = 0;
	var newdate = new Date(2000,1,1);
	newdate.setSeconds(leftseconds);
	var timestr = ((newdate.getHours() < 10) ? "0" : "") + newdate.getHours() + ":" + ((newdate.getMinutes() < 10) ? "0" : "") + newdate.getMinutes() + ":" + ((newdate.getSeconds() < 10) ? "0" : "") + newdate.getSeconds();
	var timecontent = "离线倒计时：" + timestr;
	$(".last-time").html(timecontent);
	seconds ++;
	timerId = setTimeout("count()", 1000);
   if (seconds == 14400){
     document.location.href=_contextPath+"logout";
   }
}


function goUrl(_link,_url) {
	var _text=$(_link).text();
	infoUtil.confirm("<font class='confirm-info'>确定"+_text+"？</font>",{
        title:_text+'确认',
        closeBtn:0,
        shift:-1,
        btn: ['确定','取消'] //按钮
    },function(_index){
        infoUtil.close();
        document.location.href=_url;
    },function(){});
}