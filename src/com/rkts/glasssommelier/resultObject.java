/**
 * 
 */
package com.rkts.glasssommelier;

import org.json.JSONObject;

/**
 * @author ryan
 *
 */
public class resultObject {
	public resultObject() {
		
	}
	
	String name;
	String code;
	String region;
	String winery;
	String winery_id;
	String varietal;
	String price;
	String vintage;
	String type;
	String link;
	String image;
	String rating;
	JSONObject wine;
	
	
	public resultObject(String _name, String _code, String _region,String _winery,String _winery_id,String _varietal,String _price,String _vintage,
						String _type,String _link,String _image,String _rating) {
		vintage = _vintage;
		name = _name;
		winery = _winery;
		winery_id = _winery_id;
		type = _type;
		rating = _rating;
		link = _link;
		varietal = _varietal;
		price = _price;
		image = _image;
		code = _code;
		region = _region;
	}
	
	//getters
	public String getVintage() {
		return vintage;
	}
	public String getName() {
		return name;
	}
	public String getWinery() {
		return winery;
	}
	public String getWineryId() {
		return varietal;
	}
	public String getVarietal() {
		return varietal;
	}
	public String getType() {
		return type;
	}
	public String getPrice() {
		return price;
	}
	public String getRating() {
		return rating;
	}
	public String getRegion() {
		return region;
	}
	public String getLink() {
		return link;
	}
	public String getImage() {
		return image;
	}
	public String getCode() {
		return code;
	}
	
	//setters
	public void setvintage(String vintage) {
		this.vintage = vintage;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setWinery(String winery) {
		this.winery = winery;
	}
	public void setWineryId(String winery_id) {
		this.winery_id = winery_id;
	}
	public void setVarietal(String varietal) {
		this.varietal = varietal;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setlink(String link) {
		this.link = link;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setCode(String code) {
		this.code = code;
	}




}
