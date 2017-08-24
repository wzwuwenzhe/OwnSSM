<@htmlHeader/>
<@htmlBody>
	<#if client.id!=''>
		<#assign h2="修改客户"/>
		<#assign btn="修改客户"/>
		<#assign action="/clientModify"/>
		<#else>
		<#assign h2="添加客户"/>
		<#assign btn="客户注册"/>
		<#assign action="/clientRegister"/>
	</#if>
	<@form action="${action}" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="${h2}">
		<input type="hidden" name="id" value="${(client.id)!''}" />
		<@form_group id="cusName" value="${(client.name)!''}" desc="客户名" name="name" dataType="Require" msg="客户名称必须全部为中文" type="text"/>
		<@form_group id="cusPhone" value="${(client.phone)!''}" desc="客户电话" name="phone" dataType="Phone" msg="手机号码不正确" type="text" />
		<@showMsg type="danger"/>
		<div class="form-group">
			<input type="submit" value="${btn}" class="btn btn-primary">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
		</div>
		<script type="text/javascript">
	    	<#if _token?exists>_token='${_token}';</#if>
			var _loginCallback=function(response){
				if(response.success==true){
				    $form.reset("#_form");
				    alert(response.message);
				    window.setTimeout(function(){
						location.href='./showClient';
					},2000); 
				}else{
					alert(response.msg)
				}
			}
	    </script>
	</@form>
</@htmlBody>
<@htmlFooter/>
	

