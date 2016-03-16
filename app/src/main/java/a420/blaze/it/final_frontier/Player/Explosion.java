package a420.blaze.it.final_frontier.Player;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import a420.blaze.it.final_frontier.Player.Animation;

/**
 * Created by Josh on 16/03/16.
 */
public class Explosion {
    private int x;
    private int y;
    private int width;
    private int height;
    private int row;
    private Animation animation = new Animation();
    private Bitmap ss;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numOfFrames)
    {
        this.x = x;
        this.y = y;
        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numOfFrames];

        ss = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%5==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(ss, (i-(5*row))*width, row*height, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(1);
    }

    public void draw(Canvas canvas)
    {
        if(animation.playedOnce())
        {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }

    }

    public void update()
    {

            animation.update();
    }

    public int getHeight()
    {
        return height;
    }

}
