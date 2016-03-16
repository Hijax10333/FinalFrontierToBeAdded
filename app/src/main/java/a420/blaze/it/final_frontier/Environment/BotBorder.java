package a420.blaze.it.final_frontier.Environment;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import a420.blaze.it.final_frontier.Architecture.GameObject;
import a420.blaze.it.final_frontier.Architecture.MainGame;

/**
 * Created by Josh on 16/03/16.
 */
public class BotBorder extends GameObject {

    private Bitmap image;

    // constructor for bot border
    public BotBorder(Bitmap res, int x, int y)
    {
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        dx = MainGame.MOVESPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update()
    {
        x+=dx;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
}
