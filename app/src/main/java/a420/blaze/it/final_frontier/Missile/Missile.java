package a420.blaze.it.final_frontier.Missile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

import a420.blaze.it.final_frontier.Architecture.GameObject;
import a420.blaze.it.final_frontier.Player.Animation;

/**
 * Created by Josh on 16/03/16.
 */
public class Missile extends GameObject {

    private int score;
    private int speed;
    // rand variable used to generate random values for missile speed
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap ss;


    // as the game goes on and the score increases - missiles speed increases

    public Missile(Bitmap res, int x, int y, int w, int h, int s, int noOfFrames)
    {
        // super.x && super.y - calling the game object
        // setting missles x & y variables equal to that of game objects
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        // base speed of missiles = 7
        // as the game progresses and the score gets higher,
        // score will be divided by 30 and added to the base speed of 7
        speed = 7 + (int) (rand.nextDouble()*score/30);

        // cap on missile speed
        if(speed>40) {speed = 40;}

        Bitmap[] image = new Bitmap[noOfFrames];

        ss = res;

        // loop goes through image and assign each element of the array to specified image
        for(int i=0; i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(ss, 0, i*height, width, height);
        }

        // send image to animation class
        animation.setFrames(image);
        // if missile speed is faster - delay going to be less so missile spins faster
        // (velocity and all that sh*t)
        animation.setDelay(100-speed);
    }

    public void update()
    {
        x-= speed;
        animation.update();
    }

    public void draw(Canvas canvas)
    {
        try {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch(Exception e){}
    }

    @Override
    public int getWidth()
    {
        // offset slightly for more realistic collision detection
        return width-10;
    }


}
