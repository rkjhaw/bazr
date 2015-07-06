package utils;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.WindowManager;
 
public class ImageUtils 
{
 
    private FragmentActivity _context;
 
    // constructor
    public ImageUtils(FragmentActivity context)
    {
        this._context = context;
    }
 

    /*
     * getting screen width
     */
    public int getScreenWidth() 
    {
        int columnWidth;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
 
        final Point point = new Point();
        try 
        {
            display.getSize(point);
        } 
        catch (java.lang.NoSuchMethodError ignore) 
        { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}