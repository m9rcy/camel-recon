package io.m9rcy.recon.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ControlHeader {

	// 1 'H'
	private String type;
	// 13 'Online EFTPOS'
	private String fileName;
	// 8 CCYYMMDD
	private Date creationDate;
	// 6 HHMMSS
	private Date creationTime;
	// 8 CCYYMMDD
	private Date settlementDate;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
}
