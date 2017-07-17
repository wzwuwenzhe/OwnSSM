<@htmlHeader >
	<@js path="/js/ajaxfileupload.js"/>
</@htmlHeader>
<@htmlBody>
	<@form action="/storeRegister" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="店铺注册" >
		<div class="form-group">
			店铺ID:<input type="text" readonly="readonly" dataType="NumberMax8" name="id" 
			msg="" class="form-control" id="storeId" value="${storeId}" autocomplete="off">
		</div>
		<@form_group id="name" value="店铺名称(中英文均可)" type="text" name="name" dataType="Text" msg="店铺名称不能包含特殊字符"/>
		<@form_group id="adress" value="店铺地址" type="text" name="address" dataType="MoreText" msg="店铺地址不能包含特殊字符" />
		<@form_group id="telephone" value="电话" type="text" name="telePhone" dataType="" msg="请输入正确的固话号码" onkeyup="validate('LTGH',this)" />
		<@form_group id="mobilephone" value="手机" type="text" name="mobilePhone" dataType="" msg="请输入正确的手机号码" onkeyup="validate('Phone',this)" />
		<@form_group id="reminder" value="提醒(小票上的温馨提示)" type="text" name="reminder" dataType="" msg="输入的提醒不合法" onkeyup="validate('MoreText',this)"/>
		<@uploadImg id="logoImg" desc="店铺Logo图片(png、jpg格式)" />
		<@uploadImg id="wxAddImg" desc="微信添加好友二维码(png、jpg格式)"/>
		<@uploadImg id="wxPayImg" desc="微信收款二维码(png、jpg格式)"/>
		<@uploadImg id="zfbPayImg" desc="支付宝收款二维码(png、jpg格式)"/>
		<@showMsg type="danger"/>
		<div class="form-group">
			<input type="submit" value="注册店铺" class="btn btn-primary">
		</div>
	</@form>
	<script type="text/javascript">
    	<#if _token?exists>_token='${_token}';</#if>
		var _loginCallback=function(response){
			if(response.success==true){
			    $form.reset("#_form");
			    //全部合上
			    $(".uploadImgDiv :file").hide();
			    $(".alert.alert-danger").parent().hide();
			    alert(response.message);
			}else{
				alert(response.msg)
			}
		}
		//验证文本框
		function validate(dataType ,a){
			if($(a).val()!=""){
				$(a).attr("dataType",dataType);
			}else{
				$(a).attr("dataType","");
			}
		}
		function showUpload(a){
			//选中
			if($(a).prop("checked")){
				$("#"+$(a).prop("id")+"File").show();
			}else{//没选中
				//隐藏文本上传控件
				$("#"+$(a).prop("id")+"File").hide();
				//隐藏勾勾
				$(a).parent().find("img").eq(0).parent().hide();
				$("#"+$(a).prop("id")+"FilePath").val('');
				$("#"+$(a).prop("id")+"File").val('');
			}
		}
		//图片上传
		function uploadFile(obj) {  
			var eId =  $(obj).prop("id");
		    $.ajaxFileUpload({  
		        url : "${url("/storeRegister4Img")}",  
		        secureuri : false,// 一般设置为false  
		        fileElementId : eId,  
		        dataType : 'json',// 返回值类型 一般设置为json  
		        data: {'fileName': $(obj).prop("id")},  
		          
		        success : function(data){  
		        	if(data.success!=true){
		        		alert(data.message)
		        	}else{
		        		//上传成功
		        		$("#"+eId+"Path").val(data.message);
		        		$("#"+eId+"Path").next().show();
		        	}
		        },  
		        error : function(data){  
		            alert("服务器异常!");  
		        }  
		    });  
		    return false;  
		} 
    </script>
</@htmlBody>
<@htmlFooter/>
	

