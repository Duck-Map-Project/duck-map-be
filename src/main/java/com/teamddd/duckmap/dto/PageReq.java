package com.teamddd.duckmap.dto;

import lombok.Getter;

@Getter
public class PageReq {
	private int pageNumber;
	private int pageSize;

	public PageReq() {
		this.pageNumber = 0;
		this.pageSize = 20;
	}
}
