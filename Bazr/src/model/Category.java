package model;

public class Category 
{
	String id, name, imagePath;
	public String getId()
	{
		return id;
	}
	public void setId(String id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public Category(String id, String name, String imagePath)
	{
		super();
		this.id = id;
		this.name = name;
		this.imagePath = imagePath;
	}

}
