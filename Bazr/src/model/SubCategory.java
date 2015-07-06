package model;

public class SubCategory 
{
	String subcategoryid,categoryid, name, imagePath;
	
	
	public String getSubcategoryid() 
	{
		return subcategoryid;
	}

	public void setSubcategoryid(String subcategoryid) 
	{
		this.subcategoryid = subcategoryid;
	}

	public String getCategoryid()
	{
		return categoryid;
	}

	public void setCategoryid(String categoryid) 
	{
		this.categoryid = categoryid;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImagePath() 
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public SubCategory(String subcategoryid,String categoryid,String name, String imagePath)
	{
		super();
		this.subcategoryid = subcategoryid;
		this.categoryid = categoryid;
		this.name = name;
		this.imagePath = imagePath;
	}

}
