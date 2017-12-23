<@htmlHeader4Order >
	<body>
		<div id="searchDiv" style="text-align:center">
			<form id="searchForm" action="${url("/orderSearch")}" method="post" >
			<input type="hidden" name="_token" value="${_token}"/>
			
			<#if userType=="1">
				<@form_input value="${(entity.storeId)!''}" id="storeId" desc="店铺Id" name="storeId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
			</#if>
			<div class="layui-fluid">
			  <div class="layui-row">
			    <!--
			    <div class="layui-col-sm3">
			      <div class="grid-demo grid-demo-bg1">
					<@form_input value="${(entity.orderId)!''}" id="orderId" desc="交易号" name="orderId" dataType="Chinese" msg="" type="text" style="width:100px;"/>
			      </div>
			    </div>
			    -->
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.orderName)!''}" id="orderName" desc="商品名称" name="orderName" dataType="Chinese" msg="" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    <div class="layui-col-sm3">
			      <div class="grid-demo grid-demo-bg1">
					<@form_input value="${(entity.cusName)!''}" id="cusName" desc="客户名称" name="cusName" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    <!--
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
					<@form_input value="${(entity.operatorId)!''}" id="operatorId" desc="操作员ID" name="operatorId" dataType="Chinese" msg="客户名称必须全部为中文" type="text" style="width:100px;"/>
				  </div>
			    </div>
			    -->
			    <div class="layui-col-sm3">
			      <div class="grid-demo">
			      <#assign state="${(entity.state)!''}"/>
					订单状态:<select name="state" onchange="$('#searchForm').submit();">
						<option value="">---全部状态---</option>
						<option value="1" <#if state=="1">selected</#if>>---未付款---</option>
						<option value="2" <#if state=="2">selected</#if>>---待发货---</option>
						<option value="3" <#if state=="3">selected</#if>>----欠货----</option>
						<option value="4" <#if state=="4">selected</#if>>----完成----</option>
					</select>
				  </div>
			    </div>
			  </div>
			</div>
			<div style="margin: 10px;">
				查询时间:<@DateFields beginValue="${(entity.beginDate)!''}" endValue="${(entity.endDate)!''}"/>
			</div>
			
			<div class="form-group">
				<input class="layui-btn layui-btn-radius" type="submit" value="查询"/>
				<input class="layui-btn layui-btn-warm layui-btn-radius" type="button" value="报表打印" onclick="printReport()" />
				<input class="layui-btn layui-btn-normal layui-btn-radius" type="button" value="报表查看" onclick="searchReport()" />
				<input class="layui-btn layui-btn-primary layui-btn-radius" type="button" value="返回"   onclick="location.href='./index'" />
			</div>
			</form>
		</div>
	<div class="container">
		<div id="page" >
		  <table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
				  	<th>序号</th>
					<th>商品名称</th>
					<th>单价</th>
					<th>数量</th>
					<th>尺码</th>
					<th>颜色</th>
					<th>金额(元)</th>
					<th>总计(元)</th>
					<th>付款方式</th>
					<th>送货地址</th>
					<th>备注</th>
					<th>客户名称</th>
					<th>订单状态</th>
					<th style="width:80px;">操作</th>
				  </tr>
		  	</thead>
			<tbody>
			<#list orderList as order>
				<tr>
					<#assign itemSize = order.itemList?size >
					<td rowspan="${itemSize}">${order_index+1}</td>
					<#if (order.itemList?size > 0 ) >
						<#assign itemList = order.itemList >
						<#list itemList as item>
							<#if (item_index == 0) >
								<td>${item.name}</td>
								<td>${item.unitPrice}</td>
								<td>${item.amount}</td>
								<td>${item.size}</td>
								<td>${item.color}</td>
								<td>${item.price}</td>
							</#if>
						</#list>
					</#if>
					<td rowspan="${itemSize}">${order.totalAmount}</td>
					<td rowspan="${itemSize}">${order.payTypeDesc}</td>
					<td rowspan="${itemSize}">${order.address}</td>
					<td rowspan="${itemSize}">${order.remark}</td>
					<td rowspan="${itemSize}">${order.cusName}</td> 
					<#assign orderState="${order.state}">
					<td rowspan="${itemSize}" <#if orderState=="1">style="color:red;font-weight:bold"</#if>
						<#if orderState=="4">style="color:green;font-weight:bold"</#if>
						<#if orderState=="3">style="color:blue;font-weight:bold"</#if>>${order.orderStateDesc}</td> 
					<td rowspan="${itemSize}">
						<#if orderState=="1">
							<input type="button" value="付款" onclick="showPayMoneyDiv(this,'${order.id}')"/>
						<#elseif orderState=="2">
							<input type="button" value="发货" onclick="showDeliverDiv(this,'${order.id}')"/>
						<#elseif orderState=="3">
							<input type="button" value="欠货发货" onclick="showDeliverDiv(this,'${order.id}')"/>
						</#if>
						</br>
							<input type="button" value="重新打印" onclick="rePrint('${order.id}');"/>
						</br> 
						<#if userType=="1" || userType=="3">
							<input type="button" value="删除订单" onclick="deleteOrder('${order.id}');"/>
						</#if>
						
					</td>
				</tr>
				<#if (order.itemList?size > 1 ) >
					<#assign _itemList = order.itemList >
					<#list _itemList as item>
						<#if (item_index>0)>
							<tr>
								<td>${item.name}</td>
								<td>${item.unitPrice}</td>
								<td>${item.amount}</td>
								<td>${item.size}</td>
								<td>${item.color}</td>
								<td>${item.price}</td>
							</tr>
						</#if>
						
					</#list>
				</#if>
		    </#list>
			</tbody>
			</table>
		</div>
	</div>
		
	<@form id="payform" action="/payForTheMoney" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="" style="display:none">
		<div style="margin:10px;">
			付款方式：</br>
			<input type="radio" id="payType1" name="payType" value="1" checked="checked" /><label for="payType1">现金</label>
			<input type="radio" id="payType2" name="payType" value="2" /><label for="payType2">刷卡</label>
			<input type="radio" id="payType3" name="payType" value="3" /><label for="payType3">支付宝</label>
			<input type="radio" id="payType4" name="payType" value="4" /><label for="payType4">微信</label>
			<input type="radio" id="payType6" name="payType" value="6" /><label for="payType6">月结</label>
		</div>
    </@form>

	<@form id="deliverform" action="/deliverGoods" onsubmit="return $form.submit(this,_loginCallback);" class="fh5co-form animate-box" h2="" style="display:none">
		<div style="margin:10px;" id="deliverformDiv">
			<table id="table" class="layui-table" lay-filter="parse-table-demo">
  		  	<thead>
		  		<tr>
					<th>商品名称</th>
					<th>颜色</th>
					<th>尺码</th>
					<th>单价</th>
					<th>数量</th>
					<th>金额(元)</th>
			  	</tr>
		  	</thead>
			<tbody></tbody>
			</table>
			<div id="addressAndRemark"></div>
			<div id="oweGoodsDiv">
				<input type="checkbox" id="oweGoods"  /><label style="font-weight:bold" for="oweGoods">欠货登记</label>
				<div id="oweGoodsRemarkDiv" style="display:none">
					发货备注信息：&nbsp;<textarea rows="3" id="oweGoodsRemarkText"></textarea>
				</div>
			</div>
		</div>
    </@form>
		
	<script type="text/javascript">
	//欠货登记  显示备注信息栏
		$(document).on("click","#oweGoods",function(){
			if($("#oweGoods").is(':checked')){
				$("#oweGoodsRemarkDiv").show();
			}else{
				$("#oweGoodsRemarkText").html('');
				$("#oweGoodsRemarkDiv").hide();
			}
		});
		
		$(document).ready(function() {
		  $('#table').basictable();
		});
		
		function rePrint(orderId){
			$.ajax({
		        url:"./rePrint",
		        type:"POST",
		        dataType:"json",
		        data:{"orderId":orderId},
		        success:function(response){
		            alert(response.message)
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		function deleteOrder(orderId){
			if(!confirm("确定要删除该订单吗,删除后不可恢复!")){
				return;
			}
			$.ajax({
		        url:"./deleteOrder",
		        type:"POST",
		        dataType:"json",
		        data:{"orderId":orderId},
		        success:function(response){
		            alert(response.message)
		            $("#searchForm").submit();
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		//报表打印
		function printReport(){
			var beginDate = $("#beginDate").val();
			var endDate = $("#endDate").val();
			var storeId = $("#storeId").val();
			$.ajax({
		        url:"./printReport",
		        type:"POST",
		        dataType:"json",
		        data:{"beginDate":beginDate,"endDate":endDate,"storeId":storeId},
		        success:function(response){
		            alert(response.message)
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		//查询报表
		function searchReport(){
			var beginDate = $("#beginDate").val();
			var endDate = $("#endDate").val();
			var storeId = $("#storeId").val();
			$.ajax({
		        url:"./searchReport",
		        type:"POST",
		        dataType:"json",
		        data:{"beginDate":beginDate,"endDate":endDate,"storeId":storeId},
		        success:function(response){
		            alert(response.message)
		        },
		        error:function(){
		            alert("出错了,请联系管理员");
		        }
    		});
		}
		
		//付款按钮
		function showPayMoneyDiv(payBtn,orderId){
			layer.open({
                type:1,
                shift:-1,
                title: '选择付款方式',
                closeBtn:0,
                area: ['300px'],
                content: $("#payform"),
                btn:["提交","关闭"],
                yes:function(){
                	var _payType ="";
					$("#payform input[name='payType']").each(function(){
						if($(this).is(":checked")){
							_payType = $(this).val();
						}
					});
                	$.ajax({
						url:"./payForTheMoney.htm",
						type:"POST",
						dataType:"json",
						data:{orderId:orderId,payType:_payType},
						success:function(response){
							if(response.success == true){
								$("#searchForm").submit();
							}else{
								alert("付款方式更新失败!");
							}
						},
						error:function(){
							alert("系统错误,请联系管理员");
						}
					});
                },
                cancel:function(){
                }
            });
		}
		
		
		//发货按钮
		function  showDeliverDiv(deliverBtn ,_orderId){
			$.ajax({
				url:"./getOrderById.htm",
				type:"POST",
				dataType:"json",
				data:{orderId:_orderId},
				success:function(response){
					if(response.success == true){
					$("#deliverformDiv tbody").empty();
					$.each(response.data.items,function(i,item){
						$("#deliverformDiv tbody").append(
						"<tr><td>"+item.name+"</td>"+
						"<td>"+item.color+"</td>"+
						"<td>"+item.size+"</td>"+
						"<td>"+item.unitPrice+"</td>"+
						"<td>"+item.amount+"</td>"+
						"<td>"+item.price+"</td>");
					});
					$("#addressAndRemark").empty();
					$("#addressAndRemark").append(
					"<span style='font-weight:bold'>送货地址：</span>&nbsp;&nbsp;<span>"+response.data.address+"</span>"+
					"</br><span style='font-weight:bold'>备注：</span>&nbsp;&nbsp;<span>"+response.data.remark+"</span>");
						//打开弹出框
						layer.open({
		                type:1,
		                shift:-1,
		                title: '发货',
		                closeBtn:0,
		                area: ['500px', '350px'],
		                content: $("#deliverform"),
		                btn:["发货","关闭"],
		                yes:function(){
		                	var isOweGoods = 0;
		                	var oweGoodsRemarkText = "";
			                if($("#oweGoods").is(":checked")){
			                	isOweGoods = 1;
			                	oweGoodsRemarkText = $("#oweGoodsRemarkText").val()
			                }
		                	$.ajax({
		                		url:"./deliverGoods.htm",
								type:"POST",
								dataType:"json",
								data:{isOweGoods:isOweGoods,
								oweGoodsRemarkText:oweGoodsRemarkText,
								orderId:_orderId},
								success:function(response){
									console.log(response);
									if(response.success==true){
										$("#searchForm").submit();
									}
								},
								error:function(){
									alert("系统错误,请联系管理员");
								}
		                	});
		                },
		                cancel:function(){
		                	$("#oweGoods").attr("checked",false);
		                	$("#oweGoodsRemarkDiv").hide();
		                	$("#oweGoodsRemarkText").val('');
		                }
		            });
					}else{
						alert("订单获取失败!");
					}
				},
				error:function(){
					alert("系统错误,请联系管理员");
				}
			});
		}
	  </script>
	</body>
</@htmlHeader4Order>