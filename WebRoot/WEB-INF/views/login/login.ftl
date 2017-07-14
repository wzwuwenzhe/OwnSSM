<@htmlHeader/>
<@htmlBody>
	<@form action="/login" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="登录">
		<@showMsg type="danger"/>
		<@form_group id="username" value="用户名" type="text" name="username" dataType="Code" msg="登录名必须为英文或数字"/>
		<@form_group id="password" value="密码" type="password" name="password" dataType="PWD" msg="密码是必须大于8位小于20位的英文或数字"/>
		<div class="form-group">
			<label for="remember"><input type="checkbox" id="remember"> 记住我</label>
		</div>
		<div class="form-group">
			<input type="submit" value="登 录" class="btn btn-primary">
		</div>
	</@form>
	<script type="text/javascript">
		<#if _token?exists>_token='${_token}';</#if>
		var _loginCallback=function(response){
			if(response.success==true){
			    location.href="${url("/index")}";
			}else{
				//$(".alert.alert-danger").html(response.msg);
				alert(response.msg);
			}
		}
	</script>
</@htmlBody>
<@htmlFooter/>
	

