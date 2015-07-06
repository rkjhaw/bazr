package webservices;

import java.util.ArrayList;
import model.Category;

public interface TaskCompleted
{
	void onTaskCompleted();
	void onTaskCompleted(ArrayList<Category> categories);
	
}
