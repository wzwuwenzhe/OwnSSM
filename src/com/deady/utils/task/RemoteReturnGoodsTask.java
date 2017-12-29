package com.deady.utils.task;

public class RemoteReturnGoodsTask extends Task {

	private String oldOrderId;// 老的退货订单号
	private String newOrderId;// 新的订单号
	private String operatorId;// 操作员Id
	private String storeId;// 店铺Id
	private boolean isRePrint;// 是否重新打印

	public RemoteReturnGoodsTask(String oldOrderId, String newOrderId,
			String operatorId, String storeId, boolean isRePrint) {
		this.oldOrderId = oldOrderId;
		this.newOrderId = newOrderId;
		this.operatorId = operatorId;
		this.storeId = storeId;
		this.isRePrint = isRePrint;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

}
