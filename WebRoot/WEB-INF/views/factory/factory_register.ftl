<@htmlHeader/>
<@htmlBody>
	<#assign h2="添加工厂"/>
	<#assign btn="工厂注册"/>
	<#assign action="/factoryRegister"/>
	<#if factoryId !='' >
		<#assign h2="修改工厂"/>
		<#assign btn="修改工厂"/>
		<#assign action="/factoryModify"/>
	</#if>
	<@form action="${(action)!''}" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="${h2}">
		<input type="hidden" name="id" value="${(factory.id)!''}" />
		<@form_group id="name" value="${(factory.name)!''}" desc="工厂名" name="name" dataType="Require" msg="客户名称不能为空" type="text"/>
		<@form_group id="phone" value="${(factory.phone)!''}" desc="工厂电话" name="phone" dataType="Require" msg="手机号码不正确" type="text" />
		<@form_group id="address" value="${(factory.address)!''}" desc="工厂地址" name="address" dataType="" msg="" type="text"/>
		<@showMsg type="danger"/>
		<div class="form-group">
			<input type="submit" value="${btn}" class="btn btn-primary">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
		</div>
	</@form>
		<script type="text/javascript">
	    	<#if _token?exists>_token='${_token}';</#if>
			var _loginCallback=function(response){
				if(response.success==true){
				    $form.reset("#_form");
				    alert(response.message);
				    window.setTimeout(function(){
						location.href='./showFactory';
					},2000); 
				}else{
					alert(response.msg)
				}
			}
	    </script>
</@htmlBody>
<@htmlFooter/>
	

