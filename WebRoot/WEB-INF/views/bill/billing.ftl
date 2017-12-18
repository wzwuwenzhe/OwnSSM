<@htmlHeader>
	<@css path="/css/loginAndRegister/bill.css"/>
	<@css path="/css/billing/autocomplete.css" />
	<@js path="/js/autocomplete.js" />
	<@js path="/js/map.js" />
</@htmlHeader>
<@htmlBody>
	<@form action="/billing" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="开单">
	<div class="form-group">
		<input type="hidden" id="cusId" name="cusId" />
		<div id="demo">
			<div class="wrapper">
				<div id="search-form"></div>
			</div>
		</div>
	</div>
	<div class="form-group">
		<table class="billtable">
			<thead>
				<tr>
					<td style="width:20%">款号</td>
					<td style="width:12%">颜色</td>
					<td style="width:12%">尺码</td>
					<td style="width:12%">数量</td>
					<td style="width:12%">单价</td>
					<td style="width:17%">金额</td>
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
	<div class="form-group">
		小计:<label id='smallCount'></label>元 
		<input type="hidden" name="smallCount"/>
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
	<@form_group id="remark" value="${(order.remark)!''}" desc="备注" type="text" name="remark" dataType="" msg="" />
	<div class="form-group">
		<input type="submit" value="下单" class="btn btn-primary">
		<input type="button" value="返回" class="btn btn-primary back"  onclick="window.history.back()">
	</div>
	</@form>
	<@showMsg type="danger"/>
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
		map.put('${client.name}',Custom);
	</#list>
	
	function cleanIdValue(){
		$("#cusId").val('');
	}
	function cleanValue(){
		$("#cusId").val('');
		$("#cusName").val('');
	}
	
	$(document).ready(function(){
		$('#search-form').autocomplete({
			hints: proposals,
			width: 300,
			height: 30,
			onSubmit: function(text){
				$('#message').html('Selected: <b>' + text + '</b>');			
			}
		});
	});
	


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
	//新增新款
	function addRecord(btn){
		$(btn).parent().parent().before("<tr class='first'>"+
		"<td rowspan='2'><input  dataType='Require' msg='商品名称不能为空' class='form-control' style='border-bottom:0px;' type='text' onblur='findColorsAndSizes(this)'/></td>"+
		"<td><select name='color'></select></td>"+
		"<td><select name='size'></select></td>"+
		"<td><input name='amount' dataType='Number' msg='数量必须为正整数' class='form-control' type='text' onkeyup='calculate(this)' /></td>"+
		"<td><input name='unitPrice' dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)'/></td>"+
		"<td><input name='price' class='price' type='hidden'  /> <label class='price' ></label></td>"+
		"<td><a href='javascript:void(0)' onclick='removeRecord(this)'>删</a></td>"+
		"</tr><tr>"+
		"<td colspan='6'><input class='form-control' type='button' value='新增同款' onclick='addSameRecord(this)'/></td>"+
		"</tr>");
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
		    	if(!_name || _name==""){
		    		alert("请先填写款号!");
		    		return;
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
		if(hasMoreThanOneLine){
			prevTd2 =  $(btn).parent().parent().prev().find("td").eq(0).html();
			prevTd3 =  $(btn).parent().parent().prev().find("td").eq(1).html();
		}
		$(btn).parent().parent().before("<tr>"+
		"<td>"+prevTd2+"</td>"+
		"<td>"+prevTd3+"</td>"+
		"<td><input name='amount' dataType='Number' msg='数量必须为正整数' class='form-control' type='text' onkeyup='calculate(this)' /></td>"+
		"<td><input name='unitPrice' dataType='Double' msg='单价必须为数字(可包含小数)' class='form-control' type='text' onkeyup='calculate(this)'/></td>"+
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
	}
	
	function calculate(number){
		var tr = $(number).parent().parent();
		$("#smallCount").html('');
		$("#totalAmount").html('');
		tr.find(".price").val('');
		tr.find(".price").html('');
		if(Validator.Double.test($(number).val())){
			var unitPrice = tr.find("input[name='unitPrice']").eq(0).val();
			var amount = tr.find("input[name='amount']").eq(0).val();
			if(amount=="" || !Validator.Integer.test(amount) || amount<=0){
				return;
			}
			if(unitPrice=="" || !Validator.Double.test(unitPrice) || unitPrice<0){
				return;
			}
			var prize = parseFloat(unitPrice)*parseFloat(amount);
			tr.find(".price").val(prize.toFixed(2));
			tr.find(".price").html(prize.toFixed(2));
		}
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
		//应付
		var totalAmount =0;
		var discount = 0;
		if(Validator.Double.test($("#discount").val())){
			discount = parseFloat($("#discount").val());
		}
		var temp = parseFloat(smallCount.toFixed(2))-discount;
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
	function findColorsAndSizes(item){
		var _name = $(item).val();
		if("" ==_name || null == _name){
			alert("款号不能为空");
			return;
		}
		//添加隐藏的name选项  以便数据添加
		if($(item).parent().next().find('input').eq(0)){
			$(item).parent().next().find('input').eq(0).remove();
		}
		$(item).parent().next().append("<input type='hidden' value='"+$(item).val()+"' name='name'/>");
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
		var colorSelector = $(item).parent().next().find("select").eq(0);
		colorSelector.empty();
		for(var i =0;i<colorArr.length;i++){
			colorSelector.append("<option value='"+colorArr[i]+"'>"+colorArr[i]+"</option>");
		}
		var sizeSelector = $(item).parent().next().next().find("select").eq(0);
		sizeSelector.empty();
		for(var i =0;i<sizeArr.length;i++){
			sizeSelector.append("<option value='"+sizeArr[i]+"'>"+sizeArr[i]+"</option>");
		}
	}
	
	</script>
</@htmlBody>
<@htmlFooter/>
	

