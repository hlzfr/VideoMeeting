package com.vc.entity;

public class PageInfo {
	private boolean hasNext = false; // 是否有下一页

	private int curPage = 0; // 当前页码

	private int perPageDataNum = 0; // 每页数据的数量

	private long totalDataNum = 0; // 总共的数据数量

	private int totalPageNum = 0; // 总共有多少页

	private long totalDataNumCurPage = 0; // 到此页为止(包括此页)一共的数据数量

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPerPageDataNum() {
		return perPageDataNum;
	}

	public void setPerPageDataNum(int perPageDataNum) {
		this.perPageDataNum = perPageDataNum;
	}

	public long getTotalDataNum() {
		return totalDataNum;
	}

	public void setTotalDataNum(long totalDataNum) {
		this.totalDataNum = totalDataNum;
	}

	public int getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public long getTotalDataNumCurPage() {
		return totalDataNumCurPage;
	}

	public void setTotalDataNumCurPage(long totalDataNumCurPage) {
		this.totalDataNumCurPage = totalDataNumCurPage;
	}
}
