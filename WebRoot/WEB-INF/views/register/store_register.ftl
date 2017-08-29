<@htmlHeader >
	<@js path="/js/ajaxfileupload.js"/>
</@htmlHeader>
<@htmlBody>
	<#assign isAdd=true/>
	<#if store!="">
		<#assign isAdd=false/>
	</#if>
		<#if isAdd>
			<#assign action="/storeRegister"/>
			<#assign formName="店铺注册"/>
		<#else>
			<#assign action="/storeModify"/>
			<#assign formName="店铺修改"/>
		</#if>
		<@form action="${action}" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box"  h2="${formName}">
		<div class="form-group">
			店铺ID:<input type="text" readonly="readonly" dataType="NumberMax8" name="id" 
			msg="" class="form-control" id="storeId" value="${storeId}" autocomplete="off">
		</div>
		<@form_group id="name" value="${(store.name)!''}" desc="店铺名称(中英文均可)" type="text" name="name" dataType="Require" msg="店铺名称不能为空"/>
		<@form_group id="adress" value="${(store.address)!''}" desc="店铺地址" type="text" name="address" dataType="Require" msg="店铺地址不能为空" />
		<@form_group id="telephone" value="${(store.telePhone)!''}" desc="电话" type="text" name="telePhone" dataType="" msg="请输入正确的固话号码" onkeyup="validate('LTGH',this)" />
		<@form_group id="mobilephone" value="${(store.mobilePhone)!''}" desc="手机" type="text" name="mobilePhone" dataType="" msg="请输入正确的手机号码" onkeyup="validate('Phone',this)" />
		<@form_group id="reminder" value="${(store.reminder)!''}" desc="提醒(小票上的温馨提示)" type="text" name="reminder" dataType="" msg="输入的提醒不合法" />
		<#if userType=="1" && !isAdd>
			<#assign status="${(store.status)!''}"/>
			<div class="form-group">
				店铺状态:
				<input type="radio" <#if status=="1">checked="checked"</#if>  name="status"  id="normalStatus" value="1" />
				<label for="normalStatus">正常</label>
				<input type="radio" <#if status=="0">checked="checked"</#if> name="status"  id="unNormalstatus" value="0" />
				<label for="unNormalstatus">停用</label>
			</div>
		</#if>
		<@uploadImg id="logoImg" value="${(store.logoImg)!''}" desc="店铺Logo图片" />
		<@uploadImg id="wxAddImg" value="${(store.wxAddImg)!''}" desc="微信添加好友二维码"/>
		<@uploadImg id="wxPayImg" value="${(store.wxPayImg)!''}" desc="微信收款二维码"/>
		<@uploadImg id="zfbPayImg" value="${(store.zfbPayImg)!''}" desc="支付宝收款二维码"/>
		<@showMsg type="danger"/>
		<div class="form-group">
				<#if isAdd>
					<input type="submit" value="注册店铺" class="btn btn-primary">
				<#else>
					<input type="submit" value="修改店铺" class="btn btn-primary">
				</#if>
				<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
		</div>
	</@form>
	<script type="text/javascript">
    	<#if _token?exists>_token='${_token}';</#if>
		var _loginCallback=function(response){
			if(response.success==true){
				<#if isAdd>
					$form.reset("#_form");
				    //全部合上
				    $(".uploadImgDiv :file").hide();
				    $(".alert.alert-danger").parent().hide();
				    $(".uploadImgDiv .uploadSuccess").hide();
				    $(".uploadImgDiv .viewImg").html('');
				    alert(response.message);
				<#else>
					alert(response.message);
				</#if>
					window.setTimeout(function(){
						location.href='${url("")}';
					},3000); 
			    
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
				//隐藏掉 file丑陋的标签
				$("#"+$(a).prop("id")+"File").click();
			}else{//没选中
				//隐藏文本上传控件
				$("#"+$(a).prop("id")+"File").hide();
				//隐藏勾勾
				$(a).parent().find("img").eq(0).parent().hide();
				$("#"+$(a).prop("id")+"FilePath").val('');
				$("#"+$(a).prop("id")+"File").val('');
				$(a).parent().find(".viewImg").html('');
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
		        data: {'fileName': $(obj).prop("id"),'storeId':$("#storeId").val()},  
		        success : function(data){  
		        	if(data.success!=true){
		        		alert(data.message)
		        	}else{
		        		//上传成功
		        		$("#"+eId+"Path").val(data.message);
		        		$("#"+eId+"Path").next().show();
		        		//显示图片
		        		$("#"+eId).parent().find(".viewImg").html('');
		        		$("#"+eId).parent().find(".viewImg").append("<img src='"+data.message+"?_stmp="+data.data+"' onclick='$(this).parent().prev().click();'/>");
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
	

