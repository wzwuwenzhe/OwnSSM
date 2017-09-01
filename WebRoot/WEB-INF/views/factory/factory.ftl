<@htmlHeader>
	<@css path="/css/loginAndRegister/bill.css"/>
</@htmlHeader>
<@htmlBody>
	<@form action="" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="工厂管理">
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:20%">工厂名称</td>
					<td style="width:15%">工厂电话</td>
					<td style="width:50%">工厂地址</td>
					<td style="width:15%">操作</td>
				</tr>
			</thead>
			<tbody>
				<#list factoryList as factory>
					<tr>
						<td>${factory.name}</td>
						<td>${factory.phone}</td>
						<td>${factory.address}</td>
						<td>
							<a href="javascript:void(0)" onclick="modifyFactory('${factory.id}');">修改</a> | 
						<#if userType=="1" || userType=="3">
							<a href="javascript:void(0)" onclick="deleteFactory('${factory.id}');" >删除</a>
						</#if>
						</td>
					</tr>
				</#list>
			</tbody>
		</table>
		<br/>
		<div class="form-group">
			<input type="button" value="新增工厂" class="btn btn-primary" onclick="location.href='./factoryRegister'">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="location.href='./index'">
		</div>
	</div>
	</@form>
	<script type="text/javascript">
	
	function deleteFactory(factoryId){
		infoUtil.confirm("确定要删除该工厂吗,删除后不可恢复!",{
	        title:'删除工厂确认',
	        closeBtn:0,
	        shift:-1,
	        btn: ['确定','取消'] //按钮
	    },function(_index){
	        infoUtil.close();
	        $.ajax({
	        url:"./deleteFactory",
	        type:"POST",
	        dataType:"json",
	        data:{"factoryId":factoryId},
	        success:function(response){
	            alert(response.message);
	            window.setTimeout(function(){
					location.href='./showFactory';
				},2000); 
	        },
	        error:function(){
	            alert("出错了,请联系管理员");
	        }
		});
	    },function(){});
	}
	
	function modifyFactory(factoryId){
		location.href="./factoryRegister?factoryId="+factoryId;
	}
	
	
	</script>
</@htmlBody>
<@htmlFooter/>
	

