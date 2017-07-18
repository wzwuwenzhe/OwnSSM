<@htmlHeader/>
<@htmlBody>
	<@form action="/operatorRegister" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="操作员注册">
		<@form_group id="storeId" desc="店铺ID" name="storeId" type="text" dataType="NumberMax8" msg="店铺ID长度为8位数字"/>
		<@form_group id="opName" desc="您的姓名" name="name" type="text" dataType="Chinese" msg="必须为中文,请填您的真实姓名"/>
		<@form_group id="mobilephone" desc="手机" name="phone" type="text" dataType="Phone" msg="手机号码错误"/>
		<!-- 只有超管才能看到这个选项 并且选择用户类型-->
		<#if userType=="1">
			<@radioGroup name="userType" datas={"1":"管理员","2":"营业员","3":"店主"} text="用户类型"/>
		<#else>
			<@radioGroup name="userType" datas={"2":"营业员"} text="用户类型"/>
		</#if>
		<@form_group id="loginName" desc="登录名" name="loginName" type="text" dataType="Code" msg="登录名必须为英文或数字"/>
		<@form_group id="password" desc="密码" name="pwd" type="password" dataType="PWD" msg="密码是必须大于8位小于20位的英文或数字"/>
		<!--
		<div class="form-group">
			<label for="re-password" class="sr-only">确认密码</label>
			<input type="password" dataType="PWD" name="rePwd" class="form-control" id="re-password" placeholder="确认密码" autocomplete="off">
		</div>
		-->
		<div class="form-group">
			<input type="submit" value="操作员注册"  class="btn btn-primary">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
		</div>
		<@showMsg type="danger"/>
		<@showMsg type="success"/>
	</@form>
	<script type="text/javascript">
    	<#if _token?exists>_token='${_token}';</#if>
		var _loginCallback=function(response){
			if(response.success==true){
			    $form.reset("#_form");
			    alert(response.message);
			}else{
				//$(".alert.alert-danger").html(response.msg);
				alert(response.msg)
			}
		}
    </script>
</@htmlBody>
<@htmlFooter/>
	

