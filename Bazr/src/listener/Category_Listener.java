package listener;

import java.util.ArrayList;

import model.Category;

public interface Category_Listener 
{
   public void onCategoriesLoaded(ArrayList<Category> Lst_Categories,String Str_Message);
}
