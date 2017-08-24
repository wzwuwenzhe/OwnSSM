<@htmlHeader/>
<@htmlBody>
	<@form action="/changePWD" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="修改密码">
		<@form_group id="oldPwd"  desc="旧密码" type="password" name="oldPwd" dataType="PWD" msg="密码是必须大于8位小于20位的英文或数字"/>
		<@form_group id="newpassword"   desc="密码" type="password" name="newpassword" dataType="PWD"  msg="密码是必须大于8位小于20位的英文或数字"/>
		<@form_group id="passwordpasswordConfirm"   desc="密码确认" type="password" name="passwordConfirm" dataType="PWD"  msg="密码是必须大于8位小于20位的英文或数字"/>
		<div class="form-group">
			<input type="submit" value="修改密码" class="btn btn-primary">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="location.href='./operatorInfo'">
		</div>
		<@showMsg type="danger"/>
	</@form>
	<script type="text/javascript">
		var _loginCallback=function(response){
			alert(response.message);
			if(response.success==true){
				window.setTimeout(function(){
					location.href='./changeAccount';
				},3000); 
			}
		}
	</script>
</@htmlBody>
<@htmlFooter/>
	

