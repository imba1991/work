package com.ht.common.page;

public class Page<T> {

	/**
	 * page number default 0
	 */
	private int page=1;
	/**
	 * size per page default 0
	 */
	private int size=20;
	/**
	 * total record number
	 */
	private int total;
	/**
	 * total page number
	 */
	/**
	 * query result
	 */
	private T content;
	public Page(int page,int size) {
		this.page=page;
		if(page<=0) {
			this.page=1;
		}
		this.size=size;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
}
