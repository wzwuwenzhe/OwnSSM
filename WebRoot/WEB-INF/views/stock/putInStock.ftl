<@htmlHeader>
	<@js path="/js/map.js"/>
	<@css path="/js/layui/css/layui.css"/>
</@htmlHeader>
<@htmlBody>
	<#assign h2="产品入库"/>
	<#assign btn="入库"/>
	<#assign action="/putInStock"/>
	<#if factoryId !='' >
		<#assign h2="修改库存"/>
		<#assign btn="修改库存"/>
		<#assign action="/modifyStock"/>
	</#if>
	<@form action="${(action)!''}" onsubmit="setColorsAndSizes();return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="${h2}">
		<input type="hidden" name="id" value="${(stock.id)!''}" />
		<@form_group id="name" value="${(name)!''}" desc="款号" name="name" dataType="Require" msg="款号不能为空" type="text" action="findColorAndSize(this)"/>
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
		<div id="colorDiv" ></div>
		<div id="sizeDiv"></div>
		<input type="hidden" name="colors" id="colors"/>
		<input type="hidden" name="sizes" id="sizes"/>
		<@form_group id="_amount" value="${(stock.amount)!''}" name="_amount" desc="数量"  dataType="" msg="" type="text"/>
		<div class="form-group">
			<input type="button" value="添加" class="btn btn-primary"  onclick="addItem()">
		</div>
		<div id="page" style="display:none">
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
					<th>颜色</th>
					<th>尺码</th>
					<th>数量</th>
					<th>操作</th>
				  </tr>
		  	</thead>
			<tbody>
			</tbody>
			</table>
		</div>
		</br>
		<div class="form-group" id="totalDiv" style="display:none">
			统计:
			<@form_group id="total" value="" desc="统计" name="total" dataType="Number" msg="数量必须为数字" type="text" readonly="readonly"/>
		</div>
		<@showMsg type="danger"/>
		<div class="form-group">
			<input type="submit" value="${btn}" class="btn btn-primary">
			<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
		</div>
	</@form>
		<script type="text/javascript">
		var colorMap = new Map();
		var sizeMap = new Map(); 
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
		function addNewItem(btn,_name,type){
			var item = $(btn).prev().val();
			if(item==''){
				alert("不能为空");
				return;
			}
			//判断是否已经存在该种类
			if(type==1 && colorMap.get(item)==item){
				alert("已经存在颜色:"+item);
				$(btn).prev().val('');
				return ;
			}else if(type==2 && sizeMap.get(item)==item){
				alert("已经存在尺码:"+item);
				$(btn).prev().val('');
				return ;
			}
			$(btn).prev().prev().before(
				"<input type='radio' name='"+_name+"' id='"+item+"' /><label for='"+item+"'>"+item+"</label>"
			);
			$(btn).prev().val('');
		}
		//款号失去焦点  寻找相应的颜色和大小信息
		function findColorAndSize(a){
			var name = $(a).val();
			if(name==''){
				return;
			}
			clearForm();
			$.ajax({
				url:"./getCorlorAndSizeByName.htm",
				type:"POST",
				data:{name:name},
				success:function(response){
					if(response.result==2){//表示查询到了数据
						var sizeArr = response.sizes.split(",");
						var colorArr = response.colors.split(",");
						showSizeAndColor(sizeArr,colorArr);
						return;
					}
					showSizeAndColor(sizeArr,colorArr);//传空的也无妨
		        },
		        error:function(){
		            alert("系统错误,请联系管理员");
		        }
			});
		}
		function clearForm(){
			$("#_amount").val('');
			$("#table tbody").empty();
			$("#totalDiv").hide();
			$("#total").val('');
		}
		function showSizeAndColor(sizeArr ,colorArr){
			if(null == sizeArr || null == colorArr){
				//默认 黑色白色   M,L,XL
				appendItem(['M','L','XL'],['黑色','白色']);
				return;
			}
			appendItem(sizeArr,colorArr);
		}
		function appendItem(sizeArr,colorArr){
			var colorDiv = $("#colorDiv");
			var sizeDiv = $("#sizeDiv");
			colorDiv.empty();
			sizeDiv.empty();
			colorDiv.append("颜色:</br>");
			for(var i =0 ;i<colorArr.length;i++){
				colorDiv.append('<input type="radio"  name="_colors" id="'+colorArr[i]+'" /><label for="'+colorArr[i]+'">'+colorArr[i]+'</label>');
				colorMap.put(colorArr[i],colorArr[i]);
			}
			colorDiv.append("</br>");
			colorDiv.append("其他:<input type='text' style='width:50px;'/><input type='button' onclick='addNewItem(this,\"_colors\",1)' value='+'/>");
			
			sizeDiv.append("尺码:</br><div>");
			for(var i =0 ;i<sizeArr.length;i++){
				sizeDiv.append('<input type="radio"  name="_sizes" id="'+sizeArr[i]+'" /><label for="'+sizeArr[i]+'">'+sizeArr[i]+'</label>');
				sizeMap.put(sizeArr[i],sizeArr[i]);
			}
			sizeDiv.append("</div></br>");
			sizeDiv.append("其他:<input type='text' style='width:50px;'/><input type='button' onclick='addNewItem(this,\"_sizes\",2)' value='+'/>");
		}
		
		//提交表单时  设置颜色和大小的值
		function setColorsAndSizes(){
			var colorStr = "";
			$("input[name='_colors']").each(function(){
				if($(this).is(':checked')){
					colorStr += $(this).next().html()+",";
				}
			});
			$("#colors").val(colorStr.substring(0,colorStr.length-1));
			var sizeStr = "";
			$("input[name='_sizes']").each(function(){
				if($(this).is(':checked')){
					sizeStr += $(this).next().html()+",";
				}
			});
			$("#sizes").val(sizeStr.substring(0,sizeStr.length-1));
		}
		
		function addItem(){
			var _color ="";
			var _size = "";
			var _amount = $("#_amount").val();
			if(isNaN(_amount)){
				alert("数量必须是数字!");
				$("#_amount").val('');
				return;
			}
			$("input[name='_colors']").each(function(){
				if($(this).is(":checked")){
					_color = $(this).next().html();
				}
			});
			$("input[name='_sizes']").each(function(){
				if($(this).is(":checked")){
					_size = $(this).next().html();
				}
			});
			if(_color =="" || _size=="" || _amount==""){
				alert("颜色,尺码,数量 都不能为空");
				return;
			}
			addTableItem(_color,_size,_amount);
			console.log(_color);
			console.log(_size);
			console.log(_amount);
			$("#page").show();
			$("#_amount").val('');
			$("#totalDiv").show();
		}
		var total =0;
		function addTableItem(_color,_size,_amount){
			$("#table tbody").append("<tr>"+
				"<td><input name='color' style='width:50px' type='text' value='"+_color+"' readonly='readonly'/></td>"+
				"<td><input name='size' style='width:40px' type='text' value='"+_size+"' readonly='readonly'/></td>"+
				"<td><input name='amount' style='width:50px' type='text' value='"+_amount+"' readonly='readonly'/></td>"+
				"<td><a href='javascript:void(0)' onclick='removeItem(this)' >删除</a></td>"+
			"</tr>");
			total += parseInt(_amount);
			$("#total").val(total);
		}
		
		function removeItem(_item){
			$(_item).parent().parent().remove();
			total -= parseInt($(_item).parent().prev().find("input").eq(0).val());
			$("#total").val(total);
			if(total == 0 ){
				$("#totalDiv").hide();
			}
		}
	    </script>
</@htmlBody>
<@htmlFooter/>
	

