package a420.blaze.it.final_frontier.Architecture;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import a420.blaze.it.final_frontier.Environment.Background;
import a420.blaze.it.final_frontier.Environment.BotBorder;
import a420.blaze.it.final_frontier.Environment.TopBorder;
import a420.blaze.it.final_frontier.Missile.Missile;
import a420.blaze.it.final_frontier.Player.Explosion;
import a420.blaze.it.final_frontier.Player.Player;
import a420.blaze.it.final_frontier.Player.Smokepuff;
import a420.blaze.it.final_frontier.R;

public class MainGame extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private long missileElapsed;
    private GameThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Explosion> explosion;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;

    /*
    * progress denominator can be increase to slow down difficulty progression in the game
    * or decreased to speed up difficulty progression
    * */
    private int progressDenom = 20;
   // private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private boolean colisionM = false;


    public MainGame(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {}

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        int counter = 0; // prevents infinte loop
        while (retry && counter < 1000)
        {
            counter++; // makes sure this loop only runs a max of 999 times then closes
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null; // allows garbage collector to pick up object

            } catch (InterruptedException e) {e.printStackTrace();}
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.space));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.moulinroguefiredrun), 32, 32, 3);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();
        explosion = new ArrayList<Explosion>();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new GameThread(getHolder(), this);

        // start game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying())
            {
                if(!started) started=true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying())
        {

            if(botborder.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            if(topborder.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            bg.update();
            player.update();

            // calc the threshold of height the border can have based on the score
            // max and min border height are updated and the border swaps direction
            // (incline to decline) when either max or min height is met

            maxBorderHeight = 30+player.getScore()/progressDenom;
            if(maxBorderHeight>HEIGHT/4)
            {
                // cap max border height so borders can only take up a total of 1/2 screen
                maxBorderHeight = HEIGHT/4;
                minBorderHeight = 5+player.getScore()/progressDenom;
            }

            // check top border collision
            for(int i=0; i<topborder.size(); i++)
            {
                if(collision(topborder.get(i), player))
                {
                    player.setPlaying(false);
                    colisionM = true;
                }
            }

            // check bot border collision
            for(int i=0; i<botborder.size(); i++)
            {
                if(collision(botborder.get(i), player))
                {
                    player.setPlaying(false);
                    colisionM = true;
                }
            }

            // update top border
            this.updateTopBorder();

            // update bot border
            this.updateBotBorder();


            // add missiles on timer
            missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            // as the players score gets higher the speed of the missiles also increases
            if(missileElapsed>(2000 - player.getScore()/4))
            {
                // first missile always appears down middle of screen
                if(missiles.size()==0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, HEIGHT/2, 45, 15, player.getScore(), 13));
                }
                else
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int) (rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2)) + maxBorderHeight), 45, 15, player.getScore(), 13));
                }
                missileStartTime = System.nanoTime(); // resets missile timer
            }

            // loop through every missile and check collision and remove
            for(int i=0; i<missiles.size(); i++)
            {
                missiles.get(i).update();
                if(collision(missiles.get(i),player))
                {
                    missiles.remove(i);
                    player.setPlaying(false);
                    colisionM = true;
                    break;
                }

                // remove missile if it moves off screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }

            }

            // start smoke puffs after 120 milliseconds
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed>120)
            {
                // + 10 off y position gives the impression
                // the cloud of smoke is coming from the exhaust of the player
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }

            // loop iterates through every smoke smoke puff object in array list
            // will update every smoke puff object and if the position of the smoke puff
            // is less than -10 (off screen) it wil be removed

            for(int i = 0; i < smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }
        }
        else{
            player.resetDY();
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                if(colisionM){
                    explosion.add(new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                            player.getY()-30, 100, 100, 25));
                }

            }

            if (colisionM)
            {
                updateExplosion();
               // explosion.update();
            }


            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }


        }
    }

    public boolean collision(GameObject a, GameObject b)
    {
        if (Rect.intersects(a.getRectangle(), b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    // override draw
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!dissapear)
            {
                player.draw(canvas);
            }

            // draw smoke puffs
            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }

            // draw missiles
            for(Missile m: missiles)
            {
                m.draw(canvas);
            }

            // draw top border
            for(TopBorder tb: topborder)
            {
                tb.draw(canvas);
            }

            // draw bot border
            for(BotBorder bb: botborder)
            {
                bb.draw(canvas);
            }

            // draw explosion
            if(colisionM)
            {
                for(Explosion ee: explosion)

                ee.draw(canvas);


            }

            drawText(canvas);

            canvas.restoreToCount(savedState);

        }

    }

    public void updateTopBorder()
    {
        // every 50 points, insert randomly placed top blocks that break border pattern
        if(player.getScore()%50==0)
        {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock)
                    , topborder.get(topborder.size() - 1).getX() + 20, 0, (int) ((rand.nextDouble() * (maxBorderHeight
            ))+1)));
        }

        // update top border
        for(int i=0; i<topborder.size(); i++)
        {
            topborder.get(i).update();
            if(topborder.get(i).getX()<-20)
            {
                topborder.remove(i);
                // remove element of arraylist, replace it by adding a new one

                // calc topdown which determines the direction the border is moving (up or down)
                if(topborder.get(topborder.size()-1).getHeight()>=maxBorderHeight)
                {
                    topDown = false;
                }
                if(topborder.get(topborder.size()-1).getHeight()>=minBorderHeight)
                {
                    topDown = true;
                }
                // new border added will have larger height
                if(topDown)
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.moonrock), topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()+1));
                }
                // new border added will have smaller height
                else
                {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.moonrock), topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()-1));
                }
            }
        }

    }

    public void updateBotBorder()
    {
        // same as the top but only every 40 points
        if(player.getScore()%40==0)
        {
            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock)
                    , botborder.get(botborder.size()-1).getX()+20, (int) ((rand.nextDouble()
                    *(maxBorderHeight)+HEIGHT-maxBorderHeight))));
        }

        // update bottom border
        for(int i=0; i<botborder.size(); i++)
        {
            botborder.get(i).update();

            // if border is moving off screen, remove it and add a corresponding new one
            if(botborder.get(i).getX()<-20) {
                botborder.remove(i);

                // determines whether bot border will be moving up or down
                if (botborder.get(botborder.size() - 1).getHeight() >= maxBorderHeight) {
                    botDown = false;
                }
                if (botborder.get(botborder.size() - 1).getHeight() <= minBorderHeight) {
                    botDown = true;
                }

                if (botDown) {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() + 1));
                } else {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() - 1));
                }
            }

        }

    }

    public void newGame()
    {
        dissapear = false;
        colisionM = false;
        botborder.clear();
        topborder.clear();


        missiles.clear();
        explosion.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT/2);

        if(player.getScore()>best)
        {
            best = player.getScore();
        }

        // create initial borders

        // initial top border
        for(int i=0; i*20<WIDTH+40; i++)
        {
            // first top border created
            if(i==0)
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock
                ), i*20, 0, 10));
            }
            else
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock
                ), i*20, 0, topborder.get(i-1).getHeight()+1));
            }

        }

        // initial bot border created
        for(int i=0; i*20<WIDTH+40; i++)
        {
            // first bot border is created
            if(i==0)
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock)
                        ,i*20, HEIGHT - minBorderHeight));
            }
            // adds borders until the bot side of the initial screen is filled
            else
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.moonrock)
                        ,i*20, botborder.get(i - 1).getY()-1));
            }
        }
        newGameCreated = true;
    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Distance: " + (player.getScore() * 3), 10, HEIGHT - 10, paint);
        canvas.drawText("Best: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint p1 = new Paint();
            p1.setColor(Color.YELLOW);
            p1.setTextSize(40);
            p1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, p1);

            p1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH / 2 - 50, HEIGHT / 2 + 20, p1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 40, p1)   ;
        }

    }

    public void updateExplosion(){
        for(int i=0; i<explosion.size(); i++)
        {
            explosion.get(i).update();
        }

    }




}

