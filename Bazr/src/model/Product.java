package model;

import java.util.ArrayList;

import android.util.Log;

public class Product {

	String productId, sellerId, subCategoryI, categoryId, productName,
			contactPerson, price, location, email, contactNo, description,
			date, status, latitude, longitude;
	ArrayList<ProductImage> productImages;
	private ArrayList<ProductImage> productImage;
	private double distance;

	public String getProductId() {
		return productId;
	}

	public Product(String productId, String sellerId, String subCategoryI,
			String categoryId, String productName, String contactPerson,
			String price, String location, String email, String contactNo,
			String description, String date, String status,
			ArrayList<ProductImage> productImage, String latitude,
			String longitude) {
		super();
		this.productId = productId;
		this.latitude = latitude;
		this.longitude = longitude;

		this.sellerId = sellerId;
		this.subCategoryI = subCategoryI;
		this.categoryId = categoryId;
		this.productName = productName;
		this.contactPerson = contactPerson;
		this.price = price;
		this.location = location;
		this.email = email;
		this.contactNo = contactNo;
		this.description = description;
		this.date = date;
		this.status = status;
		this.productImage = productImage;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSubCategoryI() {
		return subCategoryI;
	}

	public void setSubCategoryI(String subCategoryI) {
		this.subCategoryI = subCategoryI;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<ProductImage> getProductImage() {
		return productImage;
	}

	public void setProductImage(ArrayList<ProductImage> productImage) {
		this.productImage = productImage;
	}

	public void setDisatnce(String distance) {
		// TODO Auto-generated method stub
		Log.e("distance", distance);
		this.distance = Double.parseDouble(distance.replace("km", ""));
	}

	public double getDistance() {
		return this.distance;
	}

}
