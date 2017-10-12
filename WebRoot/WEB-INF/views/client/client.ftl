<@htmlHeader>
	<@css path="/js/layui/css/layui.css"/>
	<@css path="/css/loginAndRegister/bill.css"/>
	<@js path="/js/layui/layui.js"/>
</@htmlHeader>
<@htmlBody>
	<@form action="" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="客户管理">
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:35%">客户名称</td>
					<td style="width:35%">客户电话</td>
					<td style="width:30%">操作</td>
				</tr>
			</thead>
			<tbody>
				<#list clientList as client>
					<tr>
						<td>${client.name}</td>
						<td>${client.phone}</td>
						<td>
							<a href="javascript:void(0)" onclick="modifyClient('${client.id}');">修改</a> | 
						<#if userType=="1" || userType=="3">
							<a href="javascript:void(0)" onclick="deleteClient('${client.id}');" >删除</a>
						</#if>
						</td>
					</tr>
				</#list>
			</tbody>
		</table>
		<br/>
		<div id="page"></div>
		<div class="form-group">
			<input type="button" value="新增客户" class="btn btn-primary" onclick="location.href='./clientRegister'">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="location.href='./index'">
		</div>
	</div>
	</@form>
	<script type="text/javascript">
	  //分页功能
	  layui.use(['laypage', 'layer'], function(){
	  var laypage = layui.laypage
	  ,layer = layui.layer;
	  
	  laypage.render({
	    elem: 'page'
	    ,count: '${page.total}'
	    ,curr :'${page.start}'
	    ,layout: ['count', 'prev', 'page', 'next']
	    ,jump: function(obj,first){
	    if(!first){
		    var start = obj.curr;
		    location.href="./showClient?start="+start;
	    }
	    }
	  });
	  });
	  
	function deleteClient(clientId){
		infoUtil.confirm("确定要删除该客户吗,删除后不可恢复!",{
	        title:'删除客户确认',
	        closeBtn:0,
	        shift:-1,
	        btn: ['确定','取消'] //按钮
	    },function(_index){
	        infoUtil.close();
	        $.ajax({
	        url:"./deleteClient",
	        type:"POST",
	        dataType:"json",
	        data:{"clientId":clientId},
	        success:function(response){
	            alert(response.message);
	            window.setTimeout(function(){
					location.href='./showClient';
				},2000); 
	        },
	        error:function(){
	            alert("出错了,请联系管理员");
	        }
		});
	    },function(){});
	}
	
	function modifyClient(clientId){
		location.href="./clientRegister?clientId="+clientId;
	}
	
	
	</script>
</@htmlBody>
<@htmlFooter/>
	

