package a420.blaze.it.final_frontier.Environment;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import a420.blaze.it.final_frontier.Architecture.MainGame;

/**
 * Created by Conor on 09/12/2015.
 */
public class Background {

    public Bitmap image;
    public int x, y, dx;


    public Background(Bitmap res) {
        image = res;
        dx = MainGame.MOVESPEED;

    }

    public void update() {
        x += dx;
        x++;
        if (x < -MainGame.WIDTH) {
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if (x < 0) {
            canvas.drawBitmap(image, x + MainGame.WIDTH, y, null);
        }
    }
}