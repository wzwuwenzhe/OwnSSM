<@htmlHeader/>
<@htmlBody>
	<#assign h2="产品入库"/>
	<#assign btn="入库"/>
	<#assign action="/putInStock"/>
	<#if factoryId !='' >
		<#assign h2="修改库存"/>
		<#assign btn="修改库存"/>
		<#assign action="/modifyStock"/>
	</#if>
	<@form action="${(action)!''}" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="${h2}">
		<input type="hidden" name="id" value="${(stock.id)!''}" />
		<@form_group id="name" value="${(name)!''}" desc="款号" name="name" dataType="Require" msg="款号不能为空" type="text"/>
		<div class="form-group">
			<#if (factoryList?size>0)>
			厂家:<select name="factoryId">
					<#list factoryList as factory>
						<option value="${factory.id}">${factory.name}</option>
					</#list>
				</select>
			<#else>
				<a style="text-decoration:none;" href="${url("/factoryRegister")}">您还没有厂家,请先点我添加厂家</a>
			</#if>
		</div>
		<@form_group id="address" value="${(stock.amount)!''}" desc="数量" name="amount" dataType="Number" msg="数量必须为数字" type="text"/>
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
						location.href='./index';
					},2000); 
				}else{
					alert(response.msg)
				}
			}
			console.log('${factoryList?size}')
	    </script>
</@htmlBody>
<@htmlFooter/>
	

