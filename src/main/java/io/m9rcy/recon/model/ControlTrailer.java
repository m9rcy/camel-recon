package io.m9rcy.recon.model;

import java.math.BigDecimal;

public class ControlTrailer {

	// 1 'T'
	private String type;
	// 10
	private Integer totalPurchaseCount;
	// 10
	private Integer totalRefundCount;
	// 16
	private BigDecimal totalPurchaseAmount;
	// 16
	private BigDecimal totalRefundAmount;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTotalPurchaseCount() {
		return totalPurchaseCount;
	}

	public void setTotalPurchaseCount(Integer totalPurchaseCount) {
		this.totalPurchaseCount = totalPurchaseCount;
	}

	public Integer getTotalRefundCount() {
		return totalRefundCount;
	}

	public void setTotalRefundCount(Integer totalRefundCount) {
		this.totalRefundCount = totalRefundCount;
	}

	public BigDecimal getTotalPurchaseAmount() {
		return totalPurchaseAmount;
	}

	public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) {
		this.totalPurchaseAmount = totalPurchaseAmount;
	}

	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
}
