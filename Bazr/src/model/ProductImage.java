package model;

public class ProductImage {
	String imageId, productId, fileName, imagePath;

	public ProductImage(String imageId, String productId, String fileName,
			String imagePath) {
		super();
		this.imageId = imageId;
		this.productId = productId;
		this.fileName = fileName;
		this.imagePath = imagePath;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
