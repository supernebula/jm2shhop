package cn.evshop.domain;

import java.io.Serializable;

public class Product implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private double price;
	private String category;
	private String stockNum;
	private String imgUrl;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStockNum() {
		return stockNum;
	}
	public void setStockNum(String stockNum) {
		this.stockNum = stockNum;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	// a.jpg a_s.jpg
	public String getImgurl_s() {
		int index = imgUrl.lastIndexOf("."); // 得到最的.的索引
		String first = imgUrl.substring(0, index);
		String last = imgUrl.substring(index);
		return first + "_s" + last;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Product other = (Product)obj;
		if(this.id == null){
			if(other.id != null) return false;
		}else if(!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return String.format("Product [id=%s, name=%s, price=%s, category=%s, stockNum=%s, imgurl=%s, description=%s]", id, name, price, category, stockNum, imgUrl, description);
	}

	
}
