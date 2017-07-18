<@htmlHeader/>
<@htmlBody>
	<@form action="/login" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="登录">
		<@showMsg type="danger"/>
		<@form_group id="username" value="${username!''}" desc="用户名" type="text" name="username" dataType="Code" msg="登录名必须为英文或数字"/>
		<#if '${remember}'=="1">
			<#assign dataType=""/>
		<#else>
			<#assign dataType="PWD"/>
		</#if>
			<@form_group id="password" value="${password!''}"  desc="密码" type="password" name="password" dataType="${dataType!''}"  msg="密码是必须大于8位小于20位的英文或数字"/>
		<div class="form-group">
			<label for="remember">
				<input type="checkbox" id="remember" onclick="rememberMe(this)" <#if '${remember}'=="1">checked="checked"</#if>> 记住我
				<input type="hidden" name="remember" value="${remember!'0'}" />
			</label>
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
		function rememberMe(check){
			if($(check).prop("checked")){
				$(check).next().val('1');
			}else{
				$(check).next().val('0');
			}
		}
	</script>
</@htmlBody>
<@htmlFooter/>
	

