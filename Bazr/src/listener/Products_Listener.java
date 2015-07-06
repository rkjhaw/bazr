package listener;

import java.util.ArrayList;

import model.Product;

public interface Products_Listener 
{
   public void onProductLoadResult(ArrayList<Product> Lst_Products);
}
