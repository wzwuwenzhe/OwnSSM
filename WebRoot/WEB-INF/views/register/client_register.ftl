<@htmlHeader/>
<@htmlBody>
	<@form action="/clientRegister" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="添加客户">
		<@form_group id="cusName" value="客户名" name="cusName" dataType="Chinese" msg="客户名称必须全部为中文" type="text"/>
		<@form_group id="cusPhone" value="客户电话" name="cusPhone" dataType="Phone" msg="手机号码不正确" type="text" />
		<@showMsg type="danger"/>
		<div class="form-group">
			<input type="submit" value="客户注册" class="btn btn-primary">
		</div>
		<script type="text/javascript">
	    	<#if _token?exists>_token='${_token}';</#if>
			var _loginCallback=function(response){
				if(response.success==true){
				    $form.reset("#_form");
				    alert(response.message);
				}else{
					alert(response.msg)
				}
			}
	    </script>
	</@form>
</@htmlBody>
<@htmlFooter/>
	

