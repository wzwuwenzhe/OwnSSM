<@htmlHeader>
	<@css path="/css/loginAndRegister/bill.css"/>
	<@css path="/css/billing/awesomplete.css" />
	<@css path="/css/billing/default.css" />
	<@css path="/css/billing/normalize.css" />
	<@js path="/js/awesomplete.js" />
	<@js path="/js/map.js" />
<style>
	ul.color,
	ul.size,
	ul.name,
	ul.amount{
	    padding-left: 0px;
	    margin-left: 0px;
	    margin-bottom: 0px;
		list-style:none;
	}
	ul li.selected{
		background-color:yellow;
	}
	.returnNumber{
		width:25px;
	}
	.innerInput{
		width: 24px;
		height:25px;
		border :0px;
		padding-top: 0px;
		padding-bottom: 0px;
	}
</style>
</@htmlHeader>
<@htmlBody>
	<@form action="/returnGoods" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="退换货">
	<div class="form-group" style="text-align:center">
		<input type="hidden" name="returnOrderId" value="${order.id!''}"/>
		<input type="hidden" id="cusId" name="cusId"  value="${order.cusId!''}"/>
		<input type="hidden" name="cusName" value="${order.cusName!''}" />
		<h2>${order.cusName!''}</h2>
	</div>
	<div class="form-group">
		<table class="billtable" id="returnTable">
			<thead>
				<tr>
					<td style="width:16%">款号</td>
					<td style="width:16%">颜色</td>
					<td style="width:13%">尺码</td>
					<td style="width:12%">原数量</td>
					<td style="width:12%">退货数量</td>
					<td style="width:13%">单价</td>
					<td style="width:18%">金额</td>
				</tr>
			</thead>
			<tbody>
				<#list order.itemList as item>
					<tr>
						<td><input type="hidden" name="oldName"  value="${item.name}"/>${item.name}</td>
						<td><input type="hidden" name="oldColor"  value="${item.color}"/>${item.color}</td>
						<td><input type="hidden" name="oldSize"  value="${item.size}"/>${item.size}</td>
						<td><input type="hidden" name="oldAmount"  value="${item.amount}"/><label style="font-weight:bold">${item.amount}</label></td>
						<td><input type="text" value="" name="returnNumber" class="returnNumber" onkeyup='calculateReturn()'/></td>
						<td><input type="hidden" name="oldUnitPrice"  value="${item.unitPrice}"/><label style="font-weight:bold">${item.unitPrice}</label></td>
						<td>${item.price}</td>
					</tr>
				</#list>
				<tr><td colspan="7">原订单付款总计：${order.totalAmount!''}元</td></tr>
				<tr><td colspan="7">付款方式：${order.payTypeDesc!''}</td></tr>
				<tr><td colspan="7">订单状态：${order.orderStateDesc!''}</td></tr>
				<tr><td colspan="7">备注：${order.remark!''}</td></tr>
				<tr><td colspan="7">退款金额：<label id="_returnMoney" style="color:red;font-weight:bold">0</label>元</td></tr>
			</tbody>
		</table>
	</div>
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:18%">款号</td>
					<td style="width:20%">颜色</td>
					<td style="width:13%">尺码</td>
					<td style="width:13%">数量</td>
					<td style="width:13%">单价</td>
					<td style="width:18%">金额</td>
					<td style="width:5%">操作</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="7"><input class="form-control" type="button" value="新增新款" onclick="addRecord(this)"/></td>
				</tr>
			</tbody>
		</table>
	</div>
	<@showMsg type="danger"/>
	<div class="form-group">
		小计:<label id='smallCount'></label>元 
		<input type="hidden" name="smallCount"/>
		<br/>
		退款金额:<label id='returnMoney'></label>元 
		<input type="hidden" name="returnMoney"/>
		<br/>
		折扣金额:<input id='discount' name="discount" onkeyup="calculateTotal()" onfocus="$('#discount').val('')" style="width:50px;" type="text" value="0" />元 <br/>
		<!--<input name="discount" id="discount" type="hidden" onkeyup="calculateTotal()" value="0" />-->
		应付金额:<label id='totalAmount'></label>元 
		<input type="hidden" name="totalAmount"/><br/>
		付款方式:</br>
		<input type="radio" id="payType5" name="payType" value="5" checked="checked" /><label for="payType5">未付</label>
		<input type="radio" id="payType1" name="payType" value="1" /><label for="payType1">现金</label>
		<input type="radio" id="payType2" name="payType" value="2" /><label for="payType2">刷卡</label>
		<input type="radio" id="payType3" name="payType" value="3" /><label for="payType3">支付宝</label>
		<input type="radio" id="payType4" name="payType" value="4" /><label for="payType4">微信</label>
		<input type="radio" id="payType6" name="payType" value="6" /><label for="payType6">月结</label>
		<br/>
	</div>
	<@form_group id="address" value="${(order.address)!''}" desc="送货地址" type="text" name="address" dataType="" msg="" />
	<@form_group id="remark" value="" desc="备注" type="text" name="remark" dataType="" msg="" />
	<div class="form-group">
		<input type="submit" value="下单" class="btn btn-primary">
		<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
	</div>
	</@form>
	<script type="text/javascript">
		<#if _token?exists>_token='${_token}';</#if>
	
	var proposals = new Array();
	var map = new Map();
	<#list clientList as client>
		proposals[${client_index}] = '${client.name}';
		var Custom = new Object();
		Custom.id = '${client.id}';
		Custom.name = '${client.name}';
		Custom.phone = '${client.phone}';
		Custom.address = '${client.deliverAddress}';
		map.put('${client.name}',Custom);
	</#list>
	//页面加载完成时 写入map中的自动完成数据
	$(document).ready(function(){ 
		var dataList ="";
		var keyArr = map.keys();
		for(var i = 0;i<keyArr.length;i++){
			dataList += ","+keyArr[i]
		}
		$(".awesomplete").attr("data-list",dataList.substring(1,dataList.length));
	}); 
	
	
	function selectDeliverAddress(item){
		var _cusName =$(item).val();
		var _client = map.get(_cusName);
		if(null != _client){
			$("#address").val(_client.address);
			$("#cusId").val(_client.id);
		}else{
			$("#address").val('');
			$("#cusId").val('');
		}
	}
	//根据客户名称查找客户送货地址
	function findClientAddress(){
		var clientName = $("#cusName").val();
		$.ajax({
				url:"./getClientAddressByName.htm",
				type:"POST",
				dataType:"json",
				data:{clientName:clientName},
				success:function(response){
					if(response.success==true){
						$("#address").val(response.data);
					}
		        },
		        error:function(){
		            alert("系统错误,请联系管理员");
		        }
			});
	}
	
	


	function removeRecord(btn){
		//先找到第一个td的rowspan的值
		var firstRowspan = $(btn).parent().parent().find("td").eq(0).attr("rowspan");
		
		if(parseInt(firstRowspan)>1){
			for(var n = 0 ;n < (firstRowspan-1) ; n++){
				$(btn).parent().parent().next().remove();
			}
		}
		$(btn).parent().parent().remove();
		calculateTotal();
	}
	
	function clickLi(li,index){
		//判断是颜色还是尺寸
		var ulClass = $(li).parent().attr("class");
		if(ulClass && ulClass=="color"){
			$(li).parent().find("li").each(function(){
				$(this).removeClass();
			});
			var _color = $(li).html();
			var _amountTd = $(li).parent().parent().next().next();
			$(_amountTd).find("li").each(function(){
				$(this).find("input[name='color']").val(_color);
			});
			$(li).addClass("selected");
		}else if(ulClass && ulClass=="size"){
			var _size = $(li).html();
			var _amountTd = $(li).parent().parent().next();
			if($(li).attr("class")=="selected"){
				$(li).removeClass();
				$(_amountTd).find("li").eq(parseInt(index)).find("input[name='size']").val('');
				var tempAmount=$(_amountTd).find("li").eq(parseInt(index)).find("input[name='amount']").eq(0);
				$(tempAmount).val('');
				$(tempAmount).attr("readonly","readonly");
				//需要重新计算
				calculate($(tempAmount),true);
			}else{
				$(li).addClass("selected");
				$(_amountTd).find("li").eq(parseInt(index)).find("input[name='size']").val(_size);
				$(_amountTd).find("li").eq(parseInt(index)).find("input[name='amount']").removeAttr("readonly");
			}
		}
	}
	
	
	var btnIndex = 0;
	//新增新款
	function addRecord(btn){
		$(btn).parent().parent().before("<tr class='first'>"+
		"<td rowspan='2'><ul id='" + btnIndex + "nameUl' class='name'></ul><div><input onclick='removeLiClass(this)'  class='form-control' style='border-bottom:0px;' type='text' onblur='findColorsAndSizes(this)'/></div></td>"+
		"<td><ul class='color'></td>"+
		"<td><ul class='size'></td>"+
		"<td><ul class='amount'></ul></td>"+
		"<td><input  dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)' style='border-bottom:0px;'/></td>"+
		"<td><input name='price' class='price' type='hidden'  /> <label class='price' ></label></td>"+
		"<td><a href='javascript:void(0)' onclick='removeRecord(this)'>删</a></td>"+
		"</tr><tr>"+
		"<td colspan='6'><input class='form-control' type='button' value='新增同款' onclick='addSameRecord(this)'/></td>"+
		"</tr>");
		<#list nameList as name>
			$("#"+btnIndex+"nameUl").append("<li onclick='findColorsAndSizes(this,true)' >"+${name}+"</li>");
		</#list>
		btnIndex++;
	}
	//去掉li的样式
	function removeLiClass(item){
		$(item).parent().prev().find("li").each(function(){
			$(this).removeClass();
		});
	}
	
	//新增同款
	function addSameRecord(btn){
		//修改第一列的rowspan
		//是否同一个款式只有一行记录
		var hasMoreThanOneLine = false ;
		var prev = $(btn).parent().parent().prev();
		do {
		    var flag = $(prev).is('.first');
		    if(flag){
		    	//如果款号没有输入  就不能新增
		    	var _name = $(prev).find("td").eq(0).find("input").eq(0).val();
		    	var selectedName = "";
		    	$(prev).find("li").each(function(){
		    		if($(this).hasClass("selected")){
		    			selectedName = $(this).html();
		    		}
		    	});
		    	if(!_name || _name==""){
		    		_name = selectedName;
		    		if(!_name || _name==""){
			    		alert("请先填写款号!");
			    		return;
		    		}
		    	}
		    	var lastrowspan = $(prev).find("td").eq(0).attr("rowspan");
		    	$(prev).find("td").eq(0).attr("rowspan",(parseInt(lastrowspan)+1));
		    }else{
			    prev = $(prev).prev();
			    hasMoreThanOneLine = true;
		    }
		}
		while (flag==false);
		
		var prevTd2 =  $(btn).parent().parent().prev().find("td").eq(1).html();
		var prevTd3 =  $(btn).parent().parent().prev().find("td").eq(2).html();
		var prevTd4 =  $(btn).parent().parent().prev().find("td").eq(3).html();
		if(hasMoreThanOneLine){
			prevTd2 =  $(btn).parent().parent().prev().find("td").eq(0).html();
			prevTd3 =  $(btn).parent().parent().prev().find("td").eq(1).html();
			prevTd4 =  $(btn).parent().parent().prev().find("td").eq(2).html();
		}
		//去掉每个数量中的值
		$(prevTd4).find("input").each(function(){
			$(this).val('');
		});
		$(btn).parent().parent().before("<tr>"+
		"<td>"+prevTd2+"</td>"+
		"<td>"+prevTd3+"</td>"+
		"<td>"+prevTd4+"</td>"+
		"<td><input dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)' style='border-bottom:0px;'/></td>"+
		"<td><input name='price' class='price' type='hidden'  /> <label class='price' ></label></td>"+
		"<td><a href='javascript:void(0)' onclick='removeRecordForOne(this)'>删</a></td>"+
		"</tr>");
	}
	
	function removeRecordForOne(btn){
		//修改第一列的rowspan
		var hasMoreThanOneLine = false ;
		var prev = $(btn).parent().parent().prev();
		do {
		    var flag = $(prev).is('.first');
		    if(flag){
		    	var lastrowspan = $(prev).find("td").eq(0).attr("rowspan");
		    	$(prev).find("td").eq(0).attr("rowspan",(parseInt(lastrowspan)-1));
		    }else{
			    prev = $(prev).prev();
			    hasMoreThanOneLine = true;
		    }
		}
		while (flag==false);
		$(btn).parent().parent().remove();
		calculateTotal();
	}
	
	
	//计算退款金额
	function calculateReturn(){
		$("#returnMoney").html('');
		$("#_returnMoney").html('');
		var returnMoney = 0;
		$("#returnTable .returnNumber").each(function(){
			var _oldAmount = $(this).parent().prev().find("label").eq(0).html();//前下单数量
			var _returnNumber = $(this).val();//退款数量
			var _oldUnitPrice = $(this).parent().next().find("label").eq(0).html();//退款单价
			var oldAmount = parseInt(_oldAmount);
			var returnNumber = parseInt(_returnNumber);
			var oldUnitPrice = parseInt(_oldUnitPrice);
			if(!returnNumber || returnNumber < 0 || !oldUnitPrice){
				return;
			}
			//再控制最大数量不能超过原数量
			if(oldAmount<returnNumber){
				alert("退货数量不能超过原数量!");
				return;
			}
			returnMoney +=(returnNumber*oldUnitPrice);
		});
		$("#returnMoney").html(parseFloat(returnMoney));
		$("#_returnMoney").html(parseFloat(returnMoney));
		$("input[name='returnMoney']").val(parseFloat(returnMoney));
		calculateTotal();
	}
	
	function calculate(number,inner){
		var tr = $(number).parent().parent();
		var unitPrice = 0;
		if(inner && inner==true){
			tr = $(number).parent().parent().parent().parent();
			unitPrice = $(number).parent().parent().parent().next().find("input").eq(0).val();
		}else{
			unitPrice = $(number).val();
			$(number).parent().prev().find("li").each(function(){
				$(this).find("input[name='unitPrice']").val(unitPrice);
			});
		}
		$("#smallCount").html('');
		$("#totalAmount").html('');
		tr.find(".price").val('');
		tr.find(".price").html('');
		if($(number).val()!="" && !Validator.Double.test($(number).val())){
			$(number).val('');
		}
		//数量进行累加
		var amount = 0;
		tr.find("input[name='amount']").each(function(){
			if(Validator.Integer.test($(this).val())){
				amount += parseInt($(this).val());
			}
		});
		if(amount=="" || !Validator.Integer.test(amount) || amount<=0){
			return;
		}
		if(unitPrice=="" || !Validator.Double.test(unitPrice) || unitPrice<0){
			return;
		}
		var prize = parseFloat(unitPrice)*parseFloat(amount);
		tr.find(".price").val(prize.toFixed(2));
		tr.find(".price").html(prize.toFixed(2));
		calculateTotal()
	}
	function calculateTotal(){
		//小计
		var smallCount =0;
		$(".billtable").find("input[name='price']").each(function(){
			var each = $(this).val();
			if(Validator.Double.test(each)){
				smallCount += parseFloat(each);
			}
		});
		$("#smallCount").html(smallCount.toFixed(2));
		$("#smallCount").next().val(smallCount.toFixed(2));
		
		//退款金额
		var returnMoney = 0;
		if(Validator.Double.test($("#returnMoney").html())){
			returnMoney = parseFloat($("#returnMoney").html());
		}
		
		//应付
		var totalAmount =0;
		var discount = 0;
		if(Validator.Double.test($("#discount").val())){
			discount = parseFloat($("#discount").val());
		}
		var temp = parseFloat(smallCount.toFixed(2))-discount-returnMoney;
		$("#totalAmount").html(temp.toFixed(2));
		$("#totalAmount").next().val(temp.toFixed(2));
	}
	
	var _loginCallback=function(response){
			if(response.success==true){
			    $form.reset("#_form");
			    alert(response.message);
			    location.href='${url("")}';
			}else{
				alert(response.msg)
			}
		}
	
	//根据款号找到颜色和尺码
	function findColorsAndSizes(item,isLi){
		if(isLi){
			$(item).parent().find("li").each(function(){
				$(this).removeClass("selected");
			});
			$(item).addClass("selected");
			clickLi(item);
			$(item).parent().next().find("input").eq(0).val('');
		}
		var _name = $(item).val();
		var selectedName = $(item).html();
		if("" ==_name || null == _name ){
			_name = selectedName;
			if("" ==_name || null == _name ){
				alert("款号不能为空");
				return;
			}
		}
		//添加隐藏的name选项  以便数据添加
		var _amountTd = $(item).parent().parent().next().next().next();
		$(_amountTd).find("li").each(function(){
			if($(this).find("input[name='name']")){
				$(this).find("input[name='name']").remove();
			}
			if($(this).find("input[name='color']")){
				$(this).find("input[name='color']").remove();
			}
			if($(this).find("input[name='size']")){
				$(this).find("input[name='size']").remove();
			}
		});
		$.ajax({
				url:"./getCorlorAndSizeByName.htm",
				type:"POST",
				data:{name:_name},
				success:function(response){
					if(response.result==2){//表示查询到了数据
						var sizeArr = response.sizes.split(",");
						var colorArr = response.colors.split(",");
						showSizeAndColor(sizeArr,colorArr,item);
						return;
					}
					showSizeAndColor(sizeArr,colorArr,item);//传空的也无妨
		        },
		        error:function(){
		            alert("系统错误,请联系管理员");
		        }
			});
	}
	
	function showSizeAndColor(sizesArr,colorsArr,item){
		if(null == sizesArr || null == colorsArr){
				//默认 黑色白色   M,L,XL
				appendSelector(['M','L','XL'],['黑色','白色'],item);
				return;
		}
		appendSelector(sizesArr,colorsArr,item);
	}
	
	function appendSelector(sizeArr,colorArr,item){
		//添加多选框
		var colorUl = $(item).parent().parent().next().find("ul").eq(0);
		colorUl.empty();
		for(var i =0;i<colorArr.length;i++){
			colorUl.append("<li onclick='clickLi(this)'>"+colorArr[i]+"</li>");
		}
		var sizeUl = $(item).parent().parent().next().next().find("ul").eq(0);
		sizeUl.empty();
		for(var i =0;i<sizeArr.length;i++){
			sizeUl.append("<li onclick='clickLi(this,"+i+")'>"+sizeArr[i]+"</li>");
		}
		//根据尺码的数量添加数量框
		var amountUl = $(item).parent().parent().next().next().next().find("ul").eq(0);
		amountUl.empty();
		for(var i =0;i<sizeArr.length;i++){
			amountUl.append("<li ><input name='amount'  readonly='readonly'  class='innerInput' type='text' onkeyup='calculate(this,true)' style='border-bottom:0px;' /></li>");
		}
		var _name = $(item).val();
		var selectedName = $(item).html();
		if("" ==_name || null == _name ){
			_name = selectedName;
		}
		//添加color和size的name隐藏属性
		$(amountUl).find("li").each(function(){
			$(this).append("<input type='hidden' value='' name='color'/>");
			$(this).append("<input type='hidden' value='' name='size'/>");
			$(this).append("<input type='hidden' value='' name='unitPrice'/>");
			$(this).append("<input type='hidden' value='"+_name+"' name='name'/>");
		});
	}
	
	</script>
</@htmlBody>
<@htmlFooter/>
	

