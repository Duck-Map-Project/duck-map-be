package com.teamddd.duckmap.dto;

import lombok.Getter;

@Getter
public class PageReq {
	private Integer pageNumber;
	private Integer pageSize;

	public PageReq(Integer pageNumber, Integer pageSize) {
		this.pageNumber = pageNumber == null ? 0 : pageNumber;
		this.pageSize = pageSize == null ? 20 : pageSize;
	}
}
