package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Shop extends Activity {
    int screenWidth;
    int screenHeight;
    Intent map1, map2, map3, map4, map5;
    Intent i;
    Character Fab;
    Character SpeedButton;
    Character HealthButton;
    Character CoinButton;
    Character NextSignButton;
    double BlockSize;
    int numBlocksWide;
    int numBlocksHigh;
    int ground1;
    int lvl;
    Canvas canvas;
    GameActivityView gameActivityView;
    Bitmap healthBitmap;
    Bitmap speedBitmap;
    Bitmap coinBitmap;
    Bitmap nextSignBitmap;
    Bitmap fabBitmap;
    int charFrame = 0;

    int playerHealth;
    int mapNum = 1;
    int playerSpeed;
    //This is money
    int playerCurrency;
    int speedUpgradeCost = 1;
    int healthUpgradeCost = 1;

    //stats
    long lastFrameTime;
    int fps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   loadSound();
        configureDisplay();
        gameActivityView = new GameActivityView(this);
        setContentView(gameActivityView);
        i = new Intent(this, MainActivity.class);
        map5 = new Intent(this, FinalLevel.class);
        map4 = new Intent(this, GameActivity4.class);
        map3 = new Intent(this, GameActivity3.class);
        //map2 = new Intent(this, GameActivity2.class);
        map1 = new Intent(this,MainActivity.class);



        playerHealth = getIntent().getIntExtra("health", 0);
        playerCurrency = getIntent().getIntExtra("currency", 0);
        lvl = getIntent().getIntExtra("lvl", 2);
        mapNum = lvl;


    }
    class GameActivityView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingGame;
        Paint paint;
        public GameActivityView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
        }
        public boolean generalButtonTouchEvent(float motionEventX, float motionEventY, int xValue, int yValue, int blockWidth, int blockHeight) {
            if (((motionEventX >= xValue) && (motionEventX <= (xValue + (int) Math.round(blockWidth * BlockSize))))
                    && ((motionEventY >= yValue) && (motionEventY <= (yValue + (int) Math.round(blockHeight * BlockSize))))) {
                return true;
            } else {
                return false;
            }
        }
        @Override
        public void run() {
            while (playingGame) {
                drawGame();
                controlFPS();
                charFrame++;
                if (charFrame > 9) {
                    charFrame = 0;
                }
            }
        }

        public void drawGame() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.BLACK);//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(screenWidth/30);
                canvas.drawBitmap(fabBitmap, Fab.getPositionX(), Fab.getPositionY(), paint);
                canvas.drawText("Health: " + playerHealth, HealthButton.getPositionX() + 25, HealthButton.getPositionY() - 50, paint);
                canvas.drawText("Speed Level: " + playerSpeed, SpeedButton.getPositionX() + 25, SpeedButton.getPositionY() - 50, paint);
                canvas.drawText("Money: " + playerCurrency, CoinButton.getPositionX() + CoinButton.getRoundedWidth(), CoinButton.getPositionY() + CoinButton.getRoundedHeight()/2, paint);
                //canvas.drawText("SALE! EVERYTHING 1BTC", (int) Math.round(screenWidth/3.0), (int) Math.round(screenHeight * 0.7), paint);
                canvas.drawBitmap(healthBitmap, HealthButton.getPositionX(), HealthButton.getPositionY(), paint);
                canvas.drawBitmap(speedBitmap, SpeedButton.getPositionX(), SpeedButton.getPositionY(), paint);
                canvas.drawBitmap(coinBitmap, CoinButton.getPositionX(), CoinButton.getPositionY(), paint);
                canvas.drawBitmap(nextSignBitmap, NextSignButton.getPositionX(), NextSignButton.getPositionY(), paint);

                if (mapNum == 2){
                    paint.setTextSize(screenWidth/50);
                    canvas.drawText(" 'Pollution is one of the biggest global killer, over 100 million people affected' ", 20, screenHeight -75 , paint);
                }
                if (mapNum == 3){
                    paint.setTextSize(screenWidth/50);
                    canvas.drawText(" 'Ships & Cargos dumped 14 billion pounds of garbage into the ocean in 1975' ", 20, screenHeight - 75 , paint);                }
                if (mapNum == 4){
                    paint.setTextSize(screenWidth/50);
                    canvas.drawText(" 'A million of seabirds and hudred thousands of sea mammals are killed by pollution annually.' ", 20, screenHeight - 75 , paint);
                }
                if (mapNum == 5){
                    paint.setTextSize(screenWidth/50);
                    canvas.drawText(" 'Living with high levels of air pollutants have 20% higher risk of death from lung cancer' ", 20, screenHeight - 75 , paint);
                }
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
        public void controlFPS() {
            int FPS = 60;
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 100 - FPS;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / FPS);
            }
            if (timeToSleep > 0) {

                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    //Print an error message to the console
                    Log.e("error", "failed to load sound files");
                }
            }
            lastFrameTime = System.currentTimeMillis();
        }
        public void pause() {
            playingGame = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }
        }
        public void resume() {
            playingGame = true;
            ourThread = new Thread(this);
            ourThread.start();
        }
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), HealthButton.getPositionX(), HealthButton.getPositionY(), HealthButton.getBlockWidth(), HealthButton.getBlockHeight())){
                        if (playerCurrency >= healthUpgradeCost) {
                            playerHealth++;
                            playerCurrency = playerCurrency - healthUpgradeCost;
                            healthUpgradeCost++;
                        }
                    }
                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), SpeedButton.getPositionX(), SpeedButton.getPositionY(), SpeedButton.getBlockWidth(), SpeedButton.getBlockHeight())) {
                        if (playerCurrency >= speedUpgradeCost) {
                            playerSpeed++;
                            playerCurrency = playerCurrency - speedUpgradeCost;
                            speedUpgradeCost++;
                        }
                    }
                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), NextSignButton.getPositionX(), NextSignButton.getPositionY(), NextSignButton.getBlockWidth(), NextSignButton.getBlockHeight())) {

                        if(mapNum == 2){
                            map2.putExtra("level", lvl);
                            map2.putExtra("health", playerHealth);
                            map2.putExtra("speed", playerSpeed);
                            map2.putExtra("currency", playerCurrency);
                            startActivity(map2);
                        }
                        if(mapNum == 3){
                            map3.putExtra("level", lvl);
                            map3.putExtra("health", playerHealth);
                            map3.putExtra("speed", playerSpeed);
                            map3.putExtra("currency", playerCurrency);
                            startActivity(map3);
                        }
                        if(mapNum == 4){
                            map4.putExtra("level", lvl);
                            map4.putExtra("health", playerHealth);
                            map4.putExtra("speed", playerSpeed);
                            map4.putExtra("currency", playerCurrency);
                            startActivity(map4);
                        }
                        if(mapNum == 5){
                            map5.putExtra("level", lvl);
                            map5.putExtra("health", playerHealth);
                            map5.putExtra("speed", playerSpeed);
                            map5.putExtra("currency", playerCurrency);
                            startActivity(map5);
                        }

                    }

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
            }
            return true;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        while (true) {
            gameActivityView.pause();
            break;
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameActivityView.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        gameActivityView.pause();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            gameActivityView.pause();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return false;
    }
    public void configureDisplay() {
        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        //Determine the size of each block/place on the game board
        BlockSize = screenWidth / 400.0;
        //Determine how many game blocks will fit into the height and width
        //Leave one block for the score at the top
        numBlocksWide = 400;

        numBlocksHigh = (int) Math.round(screenHeight / BlockSize);

        Fab = new Character(0, 0, numBlocksHigh, 400, BlockSize);
        HealthButton = new Character(50, 80, 70, 70, BlockSize);
        SpeedButton = new Character(150, 80, 70, 70, BlockSize);
        CoinButton = new Character(5, 5, 50, 50, BlockSize);
        NextSignButton = new Character(330, 140, 75, 75, BlockSize);
        ground1 = numBlocksHigh - 30;

        fabBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.shopbackground);
        fabBitmap = Bitmap.createScaledBitmap(fabBitmap, Fab.getRoundedWidth(), Fab.getRoundedHeight(), false);
        healthBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.heart);
        healthBitmap = Bitmap.createScaledBitmap(healthBitmap, HealthButton.getRoundedWidth(), HealthButton.getRoundedHeight(), false);

        speedBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.shoe);
        speedBitmap = Bitmap.createScaledBitmap(speedBitmap, SpeedButton.getRoundedWidth(), SpeedButton.getRoundedHeight(), false);

        coinBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, CoinButton.getRoundedWidth(), CoinButton.getRoundedHeight(), false);

        nextSignBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nextsign);
        nextSignBitmap = Bitmap.createScaledBitmap(nextSignBitmap, NextSignButton.getRoundedWidth(), NextSignButton.getRoundedHeight(), false);

    }

}

