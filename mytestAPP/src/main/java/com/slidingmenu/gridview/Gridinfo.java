package com.slidingmenu.gridview;

public class Gridinfo {
	private int id; //信息ID  
	private String title; //信息标题  
	private int imageid; //图片ID 

	//信息ID处理函数  
	public void setid(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	//信息标题处理
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	//图片ID 
	public void setImageID(int imageid) {
		this.imageid = imageid;
	}

	public int getImageID() {
		return imageid;
	}
}
