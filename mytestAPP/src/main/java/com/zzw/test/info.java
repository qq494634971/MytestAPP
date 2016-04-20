package com.zzw.test;

public class info {
	private int id; //信息ID  
    private String title;   //信息标题  
    private String details; //详细信息  
    private int checkbox_visiable; //图片ID 
    private boolean checkboxstate;
      
      
  
    //信息ID处理函数  
    public void setId(int id) {    
        this.id = id;  
    }  
    public int getId() {    
        return id;  
    }  
    //标题  
    public void setTitle(String title) {    
        this.title = title;  
    }  
    public String getTitle() {  
        return title;    
    }  
      
    //详细信息  
    public void setDetails(String info) {    
        this.details = info;  
    }  
    public String getDetails() {    
        return details;    
    }  
      
    //复选框
    public void setcheckbox_visiable(int checkbox_visiable) {    
        this.checkbox_visiable = checkbox_visiable;  
    }  
    public int getcheckbox_visiable() {    
        return checkbox_visiable;  
    } 
    
    //复选框状态
    public void setcheckboxstate (boolean checkboxstate) {
    	this.checkboxstate = checkboxstate;
    }
    public boolean getcheckboxstate() {
    	return checkboxstate;
    }
}
