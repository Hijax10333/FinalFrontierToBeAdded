package a420.blaze.it.final_frontier.Player;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import a420.blaze.it.final_frontier.Architecture.GameObject;

/**
 * Created by Josh on 16/03/16.
 */
public class Smokepuff extends GameObject {

    public int r; // radius
    public Smokepuff(int x, int y)
    {
        r = 5;
        super.x = x;
        super.y = y;
    }

    public void update()
    {
        x -= 10; // given a speed of -10
    }

    // draw smoke puff clouds to canvas
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);

        // creates three overlapping circles to represent smoke clouds
        canvas.drawCircle(x - r, y - r, r, paint);
        canvas.drawCircle(x-r+2, y-r-2, r, paint);
        canvas.drawCircle(x-r+4, y-r+1, r, paint);

    }

}
