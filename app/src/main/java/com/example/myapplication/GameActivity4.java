package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Gilb on 1/24/2017.
 */

public class GameActivity4 extends Activity {

    int Temp;
    int JabTime;
    int screenWidth;
    int screenHeight;
    int Currency = 0;
    Intent i;
    Intent StartShop;

    PlayerInfo Player;
    Character Enemy1;
    Character JumpButtonDown;
    Character dpadLeft;
    Character dpadRight;
    Character PunchButton;
    Character EnemyBottle;
    Character GrassPlatform;
    Character Portal;
    Character Spike1;
    Character Spike2;
    Character Spike3;
    Character Spike4;
    Character Spike5;
    Character Spike6;
    Character Heart;
    Character Coin;
    int lvl = 2;


    int charBlockBottomGap = 3;

    boolean charMoveLeft;
    boolean charMoveRight;
    boolean charMoveUp;
    boolean charMoveDown;
    boolean doubleJump;
    boolean enemyBottleMoveLeft = true;
    boolean enemiesDefeated = false;


    double blockSize;
    int numBlocksWide;
    int numBlocksHigh;
    int mysteriousBottomGapBlock = 15;

    boolean noPush = true;
    boolean enemyNoFlip = true;
    boolean noFlip = true;
    int ground1;
    boolean noGravity = false;

    int tempUp = 1;
    boolean tempUpFinal;

    Canvas canvas;
    GameActivityView gameActivityView;

    Bitmap coinBitmap;
    Bitmap heartBitmap;
    Bitmap enemyRobbieBitmap;
    Bitmap jumpNoPushBitmap, jumpPushBitmap;
    Bitmap dpadLeftBitmap;
    Bitmap dpadRightBitmap;
    Bitmap punchButtonBitmap;
    Bitmap portalBitmap;
    Bitmap spikeBitmap;
    Bitmap spikeBitmapSmall;
    Bitmap spikeBitmapLarge;
    Bitmap grassPlatformBitmap;
    Bitmap charBitmap0, charBitmap1, charBitmap2, charBitmap3, charBitmap4, charBitmap5, charBitmap6, charBitmap7, charBitmap8, charBitmap9;
    Bitmap charWalkBitmap0,charWalkBitmap1,charWalkBitmap2,charWalkBitmap3,charWalkBitmap4,charWalkBitmap5,charWalkBitmap6,charWalkBitmap7,charWalkBitmap8,charWalkBitmap9;
    Bitmap charJabBitmap0,charJabBitmap1,charJabBitmap2,charJabBitmap3,charJabBitmap4,charJabBitmap5,charJabBitmap6,charJabBitmap7,charJabBitmap8,charJabBitmap9;
    Bitmap enemyPopBottleBitmap0,enemyPopBottleBitmap1,enemyPopBottleBitmap2,enemyPopBottleBitmap3,enemyPopBottleBitmap4,enemyPopBottleBitmap5,enemyPopBottleBitmap6,enemyPopBottleBitmap7,enemyPopBottleBitmap8,enemyPopBottleBitmap9;
    Bitmap[] charJabBitmapArray;
    Bitmap[] charBitmapArray;
    Bitmap[] charWalkBitmapArray;
    Bitmap[] enemyPopBottleBitmapArray;



    int charFrame = 0;
    int jabFrame = 0;
    boolean charJab = false;

    //stats
    long lastFrameTime;
    int fps;
    int score;
    int hi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //   loadSound();
        configureDisplay();
        gameActivityView = new GameActivityView(this);
        setContentView(gameActivityView);

        i = new Intent(this, MainActivity.class);
        StartShop = new Intent(this, Shop.class);
        Player.setHealth(getIntent().getIntExtra("health", 0));
        Currency = getIntent().getIntExtra("currency", 0);
        Player.setSpeed(7 + getIntent().getIntExtra("speed",0));
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

            charMoveDown = true;
            /*
            //our starting snake
            getSnake();
            //get an apple to munch
            getApple();
            */
        }

        public void gravity() {
            if (noGravity == false) {
                Player.setBlockY(Player.getBlockY() + 10);
            }
        }

        public int applyGravityTo(int positionY){
            positionY = positionY + 10;
            return positionY;
        }

        public void checkJump(){
            //jump
            if (tempUpFinal && (tempUp == 10)){
                charMoveUp = false;
                tempUp = 1;
                tempUpFinal = false;
            }

        }

        public void jumpIfApplicable(){
            //10 stage jump
            if (charMoveUp && (Player.getBlockY() > 0)) {
                if (tempUp == 10){
                    tempUpFinal = true;
                }
                if (tempUp == 9) {
                    Player.setBlockY(Player.getBlockY() - 1);
                    tempUp = 10;
                }
                if (tempUp == 8) {
                    Player.setBlockY(Player.getBlockY() - 2);
                    tempUp = 9;
                }
                if (tempUp == 7) {
                    Player.setBlockY(Player.getBlockY() - 5);
                    tempUp = 8;
                }
                if (tempUp == 6) {
                    Player.setBlockY(Player.getBlockY() - 12);
                    tempUp = 7;
                }
                if (tempUp == 5){
                    Player.setBlockY(Player.getBlockY() - 18);
                    tempUp = 6;
                }
                if (tempUp == 4){
                    Player.setBlockY(Player.getBlockY() - 23);
                    tempUp = 5;
                }
                if (tempUp == 3){
                    Player.setBlockY(Player.getBlockY() - 26);
                    tempUp = 4;
                }
                if (tempUp == 2){
                    Player.setBlockY(Player.getBlockY() - 28);
                    tempUp = 3;
                }
                if (tempUp == 1){
                    Player.setBlockY(Player.getBlockY() - 30);
                    tempUp = 2;
                }
            }

            else if (charMoveUp && (Player.getBlockY() < 0)){
                charMoveUp = false;
                tempUp = 1;
                tempUpFinal = false;
            }
        }

        public boolean generalButtonTouchEvent(float motionEventX, float motionEventY, int xValue, int yValue, int blockWidth, int blockHeight){
            if (((motionEventX >= xValue) && (motionEventX <= (xValue + (int) Math.round(blockWidth * blockSize))))
                    && ((motionEventY >= yValue) && (motionEventY <= (yValue + (int) Math.round(blockHeight * blockSize))))){
                return true;
            }
            else{
                return false;
            }
        }

        @Override
        public void run() {
            while (playingGame) {

                updateGame();
                drawGame();
                controlFPS();
                checkJump();
                charFrame++;

                if(charFrame > 9){
                    charFrame = 0;
                }
                if (charJab){
                    jabFrame++;
                    if (jabFrame > 9){
                        jabFrame = 0;
                        charJab = false;
                    }
                }

            }

        }

        public void updateGame() {

            noGravity = false;

            if (((Player.getBlockX() + (Player.getBlockWidth()/2)) > GrassPlatform.getBlockX()) && ((Player.getBlockX() + (Player.getBlockWidth()/2)) < (GrassPlatform.getBlockX() + GrassPlatform.getBlockWidth()))) {
                //checks if the character is within range of (10 block to 0 blocks above) the platform
                if (((Player.getBlockY() + Player.getBlockHeight() - charBlockBottomGap) >= GrassPlatform.getBlockY() - 10) && ((Player.getBlockY() + Player.getBlockHeight() - charBlockBottomGap) <= GrassPlatform.getBlockY())) {
                    //sets noGravity to true so the character does not continue falling
                    noGravity = true;
                    //places the character on the platform
                    Player.setBlockY(GrassPlatform.getBlockY() - Player.getBlockHeight() + charBlockBottomGap);
                }
            }

            // "charPosition.x < numBlocksWide - charWidth" restricts it from going any further to the right
            if (charMoveRight && (Player.getBlockX() < (numBlocksWide - Player.getBlockWidth()))) {
                //move char right by 7
                Player.setBlockX(Player.getBlockX() + Player.getSpeed());
            }

            // "charPosition.x > 0" restricts it from going any further to the left of it is smaller than 0
            if (charMoveLeft && (Player.getBlockX() > 0)) {
                //move char left by 7
                Player.setBlockX(Player.getBlockX() - Player.getSpeed());
            }


            //call the jumpIfApplicable method
            jumpIfApplicable();

            //if the player is in the air, apply gravity
            if (Player.getBlockY() < (ground1 - Player.getBlockHeight())) {
                gravity();
            }

            //if the player has hit the ground or lower, set the player on the ground
            if (Player.getBlockY() >= (ground1 - Player.getBlockHeight())){
                Player.setBlockY(ground1 - Player.getBlockHeight());

            }




            if (EnemyBottle.getBlockY() < (ground1 - EnemyBottle.getBlockHeight())){
                EnemyBottle.setBlockY(applyGravityTo(EnemyBottle.getBlockY()));
            }

            if (EnemyBottle.getBlockX() > (400 - EnemyBottle.getBlockWidth())){
                enemyBottleMoveLeft = true;
            }

            if (enemyBottleMoveLeft){
                EnemyBottle.setBlockX(EnemyBottle.getBlockX() - 5);
            }

            if (EnemyBottle.getBlockX() < 0){
                enemyBottleMoveLeft = false;
            }

            if (!enemyBottleMoveLeft){
                EnemyBottle.setBlockX(EnemyBottle.getBlockX() + 5);
            }

            //collision detection between the char and enemyBottle

            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (EnemyBottle.getBlockX() + (EnemyBottle.getBlockWidth()/2))) <= (Player.getBlockWidth() + EnemyBottle.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (EnemyBottle.getBlockY() + (EnemyBottle.getBlockHeight()/2))) <= (Player.getBlockHeight() + EnemyBottle.getBlockHeight())/2 - 10)){
                if (charJab) {
                    EnemyBottle.setBlockX(-300);
                    EnemyBottle.setBlockY(600);
                    Currency += 3;
                }
                else{
                    if(Player.getHealth() > 0){
                        Player.setBlockX(200);
                        Player.setBlockY(0);
                        Player.deductHealth();
                    }
                    else{
                        startActivity(i);
                    }
                }
            }

            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Portal.getBlockX() + (Portal.getBlockWidth()/2))) <= (Player.getBlockWidth() + Portal.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Portal.getBlockY() + (Portal.getBlockHeight()/2))) <= (Player.getBlockHeight() + Portal.getBlockHeight())/2 - 10)){
                StartShop.putExtra("level", lvl);
                StartShop.putExtra("health", Player.getHealth());
                StartShop.putExtra("speed", Player.getSpeed());
                StartShop.putExtra("currency", Currency);
                StartShop.putExtra("lvl", 5);
                startActivity(StartShop);
            }

            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike1.getBlockX() + (Spike1.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike1.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike1.getBlockY() + (Spike1.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike1.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }
            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike2.getBlockX() + (Spike2.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike2.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike2.getBlockY() + (Spike2.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike2.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }
            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike3.getBlockX() + (Spike3.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike3.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike3.getBlockY() + (Spike3.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike3.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }
            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike4.getBlockX() + (Spike4.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike4.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike4.getBlockY() + (Spike4.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike4.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }
            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike5.getBlockX() + (Spike5.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike5.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike5.getBlockY() + (Spike5.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike5.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }
            if ((Math.abs((Player.getBlockX() + (Player.getBlockWidth()/2)) - (Spike6.getBlockX() + (Spike6.getBlockWidth()/2))) <= (Player.getBlockWidth() + Spike6.getBlockWidth())/2 - 15)
                    && (Math.abs((Player.getBlockY() + (Player.getBlockHeight()/2)) - (Spike6.getBlockY() + (Spike6.getBlockHeight()/2))) <= (Player.getBlockHeight() + Spike6.getBlockHeight())/2 - 10)){
                if(Player.getHealth() > 0) {
                    Player.setBlockX(200);
                    Player.setBlockY(0);
                    Player.deductHealth();
                }
                else{
                    startActivity(i);
                }
            }

            Player.setPositionX((int) Math.round(Player.getBlockX() * blockSize));
            Player.setPositionY((int) Math.round(Player.getBlockY() * blockSize));

            Enemy1.setPositionX((int) Math.round(Enemy1.getBlockX() * blockSize));
            Enemy1.setPositionY((int) Math.round(Enemy1.getBlockY() * blockSize));

            JumpButtonDown.setPositionX((int) Math.round(JumpButtonDown.getBlockX() * blockSize));
            JumpButtonDown.setPositionY((int) Math.round(JumpButtonDown.getBlockY() * blockSize));

            dpadLeft.setPositionX((int) Math.round(dpadLeft.getBlockX() * blockSize));
            dpadLeft.setPositionY((int) Math.round(dpadLeft.getBlockY() * blockSize));

            dpadRight.setPositionX((int) Math.round(dpadRight.getBlockX() * blockSize));
            dpadRight.setPositionY((int) Math.round(dpadRight.getBlockY() * blockSize));

            EnemyBottle.setPositionX((int) Math.round(EnemyBottle.getBlockX() * blockSize));
            EnemyBottle.setPositionY((int) Math.round(EnemyBottle.getBlockY() * blockSize));

            GrassPlatform.setPositionX((int) Math.round(GrassPlatform.getBlockX() * blockSize));
            GrassPlatform.setPositionY((int) Math.round(GrassPlatform.getBlockY() * blockSize));

            Portal.setPositionX((int) Math.round(Portal.getBlockX() * blockSize));
            Portal.setPositionY((int) Math.round(Portal.getBlockY() * blockSize));

            Spike1.setPositionX((int) Math.round(Spike1.getBlockX() * blockSize));
            Spike1.setPositionY((int) Math.round(Spike1.getBlockY() * blockSize));

            Spike2.setPositionX((int) Math.round(Spike2.getBlockX() * blockSize));
            Spike2.setPositionY((int) Math.round(Spike2.getBlockY() * blockSize));

            Spike3.setPositionX((int) Math.round(Spike3.getBlockX() * blockSize));
            Spike3.setPositionY((int) Math.round(Spike3.getBlockY() * blockSize));

            Spike4.setPositionX((int) Math.round(Spike4.getBlockX() * blockSize));
            Spike4.setPositionY((int) Math.round(Spike4.getBlockY() * blockSize));

            Spike5.setPositionX((int) Math.round(Spike5.getBlockX() * blockSize));
            Spike5.setPositionY((int) Math.round(Spike5.getBlockY() * blockSize));

            Spike6.setPositionX((int) Math.round(Spike6.getBlockX() * blockSize));
            Spike6.setPositionY((int) Math.round(Spike6.getBlockY() * blockSize));


        }

        public void drawGame() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.BLACK);//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawBitmap(heartBitmap, Heart.getPositionX(), Heart.getPositionY(), paint);
                canvas.drawText("x"+ Player.getHealth(), Heart.getPositionX() + Heart.getRoundedWidth(), Heart.getPositionY() + Heart.getRoundedHeight(), paint);
                canvas.drawBitmap(coinBitmap, Coin.getPositionX(), Coin.getPositionY(), paint);
                canvas.drawText("x"+ Currency, Coin.getPositionX() + Coin.getRoundedWidth(), Coin.getPositionY() + (int) Math.round(Coin.getRoundedHeight() * 0.8), paint);

                canvas.drawBitmap(grassPlatformBitmap, GrassPlatform.getPositionX(), GrassPlatform.getPositionY(), paint);

                if(!enemyBottleMoveLeft) {
                    enemyNoFlip = true;
                }
                if(enemyBottleMoveLeft){
                    enemyNoFlip = false;
                }
                if(enemyNoFlip){
                    canvas.drawBitmap(enemyPopBottleBitmapArray[charFrame], EnemyBottle.getPositionX(), EnemyBottle.getPositionY(),paint);
                }
                if(!enemyNoFlip){
                    Matrix flipHorizontalMatrix = new Matrix();
                    flipHorizontalMatrix.setScale(-1,1);
                    flipHorizontalMatrix.postTranslate(EnemyBottle.getPositionX() + EnemyBottle.getRoundedWidth(), EnemyBottle.getPositionY());
                    canvas.drawBitmap(enemyPopBottleBitmapArray[charFrame],flipHorizontalMatrix, paint);
                }
                canvas.drawBitmap(portalBitmap, Portal.getPositionX(), Portal.getPositionY(), paint);
                canvas.drawBitmap(spikeBitmap, Spike1.getPositionX(), Spike1.getPositionY(),paint);
                canvas.drawBitmap(spikeBitmap, Spike2.getPositionX(), Spike2.getPositionY(),paint);
                canvas.drawBitmap(spikeBitmap, Spike3.getPositionX(), Spike3.getPositionY(),paint);
                canvas.drawBitmap(spikeBitmapLarge, Spike4.getPositionX(), Spike4.getPositionY(),paint);
                canvas.drawBitmap(spikeBitmap, Spike5.getPositionX(), Spike5.getPositionY(),paint);
                canvas.drawBitmap(spikeBitmapSmall, Spike6.getPositionX(), Spike6.getPositionY(),paint);

                //draw both grass platforms

                if (charMoveRight){
                    noFlip = true;
                }
                if (charMoveLeft){
                    noFlip = false;
                }

                if (noFlip) {

                    if (charMoveRight) {
                        canvas.drawBitmap(charWalkBitmapArray[charFrame], Player.getPositionX(), Player.getPositionY(), paint);
                    }
                    if (charJab) {
                        /*
                        if(player.getBlockX() < (numBlocksWide - player.getBlockWidth())){
                            player.setBlockX(player.getBlockX() + 3);
                        }
                        */
                        canvas.drawBitmap(charJabBitmapArray[jabFrame], Player.getPositionX(), Player.getPositionY(), paint);
                    }
                    else if (!charMoveRight){
                        canvas.drawBitmap(charBitmapArray[charFrame], Player.getPositionX(), Player.getPositionY(), paint);
                    }
                }

                if (!noFlip){
                    /*
                    if (firstTouchCycle){
                        charPositionX = (int) Math.round((player.getBlockX() + (charBlockWidth/2)) * blockSize);
                        //firstTouchCycle = false;
                    }
                    */
                    //Flipping the character when facing to the left
                    Matrix flipHorizontalMatrix = new Matrix();
                    flipHorizontalMatrix.setScale(-1,1);
                    Player.setPositionX(Player.getPositionX() + (int) Math.round(Player.getBlockWidth() * blockSize));
                    flipHorizontalMatrix.postTranslate(Player.getPositionX(), Player.getPositionY());


                    if (charMoveLeft){
                        canvas.drawBitmap(charWalkBitmapArray[charFrame], flipHorizontalMatrix, paint);
                    }
                    if (charJab) {
                        /*if (player.getBlockX() > 0) {
                            player.setBlockX(player.getBlockX() - 3);
                        }
                        */
                        canvas.drawBitmap(charJabBitmapArray[jabFrame], flipHorizontalMatrix, paint);
                    }

                    else if (!charMoveLeft){
                        canvas.drawBitmap(charBitmapArray[charFrame], flipHorizontalMatrix, paint);
                    }
                }

                canvas.drawBitmap(dpadLeftBitmap, dpadLeft.getPositionX(), dpadLeft.getPositionY(), paint);
                canvas.drawBitmap(dpadRightBitmap, dpadRight.getPositionX(), dpadRight.getPositionY(), paint);
                canvas.drawBitmap(punchButtonBitmap, PunchButton.getPositionX(), PunchButton.getPositionY(), paint);

                //if the button is not being pushed down
                if (noPush) {
                    canvas.drawBitmap(jumpNoPushBitmap, JumpButtonDown.getPositionX(), JumpButtonDown.getPositionY(), paint);
                }
                //if the button is being pushed down
                if (!noPush){
                    canvas.drawBitmap(jumpPushBitmap, JumpButtonDown.getPositionX(), JumpButtonDown.getPositionY(), paint);
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

            boolean pointer1Jump, pointer1Right, pointer1Left, pointer1Jab;
            pointer1Jump = pointer1Right = pointer1Left = pointer1Jab = false;
            boolean pointer2Jump, pointer2Right, pointer2Left, pointer2Jab;
            pointer2Jump = pointer2Right = pointer2Left = pointer2Jab = false;

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), JumpButtonDown.getPositionX(), JumpButtonDown.getPositionY(), JumpButtonDown.getBlockWidth(), JumpButtonDown.getBlockHeight())){
                        //making it so that you can only jump once this is hardcoded for this map specifically for now.
                        if (noGravity == true || Player.getBlockY() == (ground1 - Player.getBlockHeight())){
                            charMoveUp = true;
                            noPush = false;

                        }
                        else{
                            noPush = false;

                        }
                        pointer1Jump = true;
                    }
                    /*if (motionEvent.getY() <= screenHeight / 2){
                        charMoveUp = true;
                    }
                    */
                    //move right

                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), dpadRight.getPositionX(), dpadRight.getPositionY(), dpadRight.getBlockWidth(), dpadRight.getBlockHeight())) {
                        charMoveRight = true;
                        charMoveLeft = false;
                        charJab = false;
                        pointer1Right = true;
                        //firstTouchCycle = true;

                    }
                    //move left
                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), dpadLeft.getPositionX(), dpadLeft.getPositionY(), dpadLeft.getBlockWidth(), dpadLeft.getBlockHeight())){
                        charMoveLeft = true;
                        charMoveRight = false;
                        charJab = false;
                        pointer1Left = true;
                        //firstTouchCycle = true;

                    }
                    //Jab animation when we click the jab button
                    if (generalButtonTouchEvent(motionEvent.getX(),motionEvent.getY(), PunchButton.getPositionX(), PunchButton.getPositionY(), PunchButton.getBlockWidth(), PunchButton.getBlockWidth())){
                        charMoveRight = false;
                        charMoveLeft = false;
                        charJab = true;

                        pointer1Jab = true;

                    }

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = motionEvent.getActionIndex();


                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), JumpButtonDown.getPositionX(), JumpButtonDown.getPositionY(), JumpButtonDown.getBlockWidth(), JumpButtonDown.getBlockHeight())){
                        //making it so that you can only jump once this 702-3453251-5641836is hardcoded for this map specifically for now.
                        if (noGravity == true || Player.getBlockY() == (ground1 - Player.getBlockHeight())){
                            charMoveUp = true;
                            noPush = false;
                        }
                        else{
                            noPush = false;
                        }
                        pointer2Jump = true;
                    }
                    /*if (motionEvent.getY() <= screenHeight / 2){
                        charMoveUp = true;
                    }
                    */
                    //move right
                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), dpadRight.getPositionX(), dpadRight.getPositionY(), dpadRight.getBlockWidth(), dpadRight.getBlockHeight())) {
                        charMoveRight = true;
                        charMoveLeft = false;
                        charJab = false;
                        pointer2Right = true;
                        //firstTouchCycle = true;

                    }
                    //move left
                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), dpadLeft.getPositionX(), dpadLeft.getPositionY(), dpadLeft.getBlockWidth(), dpadLeft.getBlockHeight())){
                        charMoveLeft = true;
                        charMoveRight = false;
                        charJab = false;
                        pointer2Left = true;
                        //firstTouchCycle = true;

                    }
                    //Jab animation when we click the jab button
                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), PunchButton.getPositionX(), PunchButton.getPositionY(), PunchButton.getBlockWidth(), PunchButton.getBlockWidth())){
                        charMoveRight = false;
                        charMoveLeft = false;
                        charJab = true;

                        pointer2Jab = true;

                    }

                    break;


                case MotionEvent.ACTION_UP:

                    if (!pointer1Jump) {
                        noPush = true;
                    }
                    charMoveRight = false;
                    charMoveLeft = false;

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    if (!pointer2Jump) {
                        noPush = true;
                    }

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

    public void configureDisplay(){
        //find out the width and height of the screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //Determine the size of each block/place on the game board
        blockSize = screenWidth/400.0;

        //Determine how many game blocks will fit into the height and width
        //Leave one block for the score at the top
        numBlocksWide = 400;
        numBlocksHigh = (int) Math.round(screenHeight/blockSize);



        GrassPlatform = new Character(200,110,18,300,blockSize);
        Player = new PlayerInfo(200,0,50,30,blockSize);
        Enemy1 = new Character(360,0,75,33,blockSize);
        JumpButtonDown = new Character(340,numBlocksHigh - mysteriousBottomGapBlock - 75,60,60,blockSize);
        dpadLeft = new Character(0,numBlocksHigh - mysteriousBottomGapBlock - 75,60,60,blockSize);
        dpadRight = new Character(90,numBlocksHigh - mysteriousBottomGapBlock - 75,60,60,blockSize);
        PunchButton = new Character(270,numBlocksHigh - mysteriousBottomGapBlock - 75,60,60,blockSize);
        EnemyBottle = new Character(355,0,75,45,blockSize);
        int jabWidth = Player.getBlockWidth() + (int) Math.round(blockSize * 45);
        Portal = new Character(370,600,50,30,blockSize);
        ground1 = numBlocksHigh - 30;
        Spike1 = new Character(150,180,40,100,blockSize);
        Spike2 = new Character(100,280,40,100,blockSize);
        Spike3 = new Character(0,380,40,100,blockSize);
        Spike4 = new Character(100,480,40,300,blockSize);
        Spike5 = new Character(0,580,40,100,blockSize);
        Spike6 = new Character(200,650,40,50,blockSize);
        Heart = new Character(5,5,20, 25, blockSize);
        Coin = new Character(60,5,25,25,blockSize);

        //Load bitmaps
        charBitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.idle1);
        charBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.idle2);
        charBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.idle3);
        charBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.idle4);
        charBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.idle5);
        charBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.idle6);
        charBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.idle7);
        charBitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.idle8);
        charBitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.idle9);
        charBitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.idle10);
        charWalkBitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_000);
        charWalkBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_001);
        charWalkBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_002);
        charWalkBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_003);
        charWalkBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_004);
        charWalkBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_005);
        charWalkBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_006);
        charWalkBitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_007);
        charWalkBitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_008);
        charWalkBitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.walk_009);
        charJabBitmap0 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_000);
        charJabBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_001);
        charJabBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_002);
        charJabBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_003);
        charJabBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_004);
        charJabBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_005);
        charJabBitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_006);
        charJabBitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_007);
        charJabBitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_008);
        charJabBitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.jab_009);
        enemyPopBottleBitmap0 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_000);
        enemyPopBottleBitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_001);
        enemyPopBottleBitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_002);
        enemyPopBottleBitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_003);
        enemyPopBottleBitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_004);
        enemyPopBottleBitmap5 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_005);
        enemyPopBottleBitmap6 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_006);
        enemyPopBottleBitmap7 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_007);
        enemyPopBottleBitmap8 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_008);
        enemyPopBottleBitmap9 = BitmapFactory.decodeResource(getResources(),R.drawable.ghost_009);
        spikeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.spike);
        jumpNoPushBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jumpnopush);
        jumpPushBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jumppush);
        dpadLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dpadleft);
        dpadRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dpadright);
        punchButtonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.punchbutton);
        grassPlatformBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grassplatform);
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
        heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);


        //scale the bitmaps to match the block size
        charBitmap0 = Bitmap.createScaledBitmap(charBitmap0, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap1 = Bitmap.createScaledBitmap(charBitmap1, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap2 = Bitmap.createScaledBitmap(charBitmap2, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap3 = Bitmap.createScaledBitmap(charBitmap3, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap4 = Bitmap.createScaledBitmap(charBitmap4, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap5 = Bitmap.createScaledBitmap(charBitmap5, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap6 = Bitmap.createScaledBitmap(charBitmap6, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap7 = Bitmap.createScaledBitmap(charBitmap7, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap8 = Bitmap.createScaledBitmap(charBitmap8, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charBitmap9 = Bitmap.createScaledBitmap(charBitmap9, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap0 = Bitmap.createScaledBitmap(charWalkBitmap0, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap1 = Bitmap.createScaledBitmap(charWalkBitmap1, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap2 = Bitmap.createScaledBitmap(charWalkBitmap2, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap3 = Bitmap.createScaledBitmap(charWalkBitmap3, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap4 = Bitmap.createScaledBitmap(charWalkBitmap4, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap5 = Bitmap.createScaledBitmap(charWalkBitmap5, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap6 = Bitmap.createScaledBitmap(charWalkBitmap6, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap7 = Bitmap.createScaledBitmap(charWalkBitmap7, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap8 = Bitmap.createScaledBitmap(charWalkBitmap8, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charWalkBitmap9 = Bitmap.createScaledBitmap(charWalkBitmap9, Player.getRoundedWidth(), Player.getRoundedHeight(), false);
        charJabBitmap0 = Bitmap.createScaledBitmap(charJabBitmap0,jabWidth , Player.getRoundedHeight(), false);
        charJabBitmap1 = Bitmap.createScaledBitmap(charJabBitmap1,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap2 = Bitmap.createScaledBitmap(charJabBitmap2,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap3 = Bitmap.createScaledBitmap(charJabBitmap3,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap4 = Bitmap.createScaledBitmap(charJabBitmap4,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap5 = Bitmap.createScaledBitmap(charJabBitmap5,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap6 = Bitmap.createScaledBitmap(charJabBitmap6,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap7 = Bitmap.createScaledBitmap(charJabBitmap7,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap8 = Bitmap.createScaledBitmap(charJabBitmap8,jabWidth, Player.getRoundedHeight(), false);
        charJabBitmap9 = Bitmap.createScaledBitmap(charJabBitmap9,jabWidth, Player.getRoundedHeight(), false);
        enemyPopBottleBitmap0 = Bitmap.createScaledBitmap(enemyPopBottleBitmap0, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap1 = Bitmap.createScaledBitmap(enemyPopBottleBitmap1, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap2 = Bitmap.createScaledBitmap(enemyPopBottleBitmap2, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap3 = Bitmap.createScaledBitmap(enemyPopBottleBitmap3, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap4 = Bitmap.createScaledBitmap(enemyPopBottleBitmap4, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap5 = Bitmap.createScaledBitmap(enemyPopBottleBitmap5, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap6 = Bitmap.createScaledBitmap(enemyPopBottleBitmap6, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap7 = Bitmap.createScaledBitmap(enemyPopBottleBitmap7, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap8 = Bitmap.createScaledBitmap(enemyPopBottleBitmap8, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        enemyPopBottleBitmap9 = Bitmap.createScaledBitmap(enemyPopBottleBitmap9, EnemyBottle.getRoundedWidth(), EnemyBottle.getRoundedHeight(),false);
        portalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.portal);
        heartBitmap = Bitmap.createScaledBitmap(heartBitmap, Heart.getRoundedWidth(), Heart.getRoundedHeight(), false);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, Coin.getRoundedWidth(), Coin.getRoundedHeight(), false);

        grassPlatformBitmap = Bitmap.createScaledBitmap(grassPlatformBitmap, GrassPlatform.getRoundedWidth(), GrassPlatform.getRoundedHeight(), false);
        jumpNoPushBitmap = Bitmap.createScaledBitmap(jumpNoPushBitmap, JumpButtonDown.getRoundedWidth(), JumpButtonDown.getRoundedHeight(), false);
        jumpPushBitmap = Bitmap.createScaledBitmap(jumpPushBitmap, JumpButtonDown.getRoundedWidth(), JumpButtonDown.getRoundedHeight(), false);
        dpadLeftBitmap = Bitmap.createScaledBitmap(dpadLeftBitmap, dpadLeft.getRoundedWidth(), dpadLeft.getRoundedHeight(), false);
        dpadRightBitmap = Bitmap.createScaledBitmap(dpadRightBitmap, dpadRight.getRoundedWidth(), dpadRight.getRoundedHeight(), false);
        punchButtonBitmap = Bitmap.createScaledBitmap(punchButtonBitmap, PunchButton.getRoundedWidth(), PunchButton.getRoundedHeight(),false);
        portalBitmap = Bitmap.createScaledBitmap(portalBitmap, Portal.getRoundedWidth(), Portal.getRoundedHeight(), false);
        spikeBitmap = Bitmap.createScaledBitmap(spikeBitmap, Spike1.getRoundedWidth(), Spike1.getBlockHeight(), false);
        spikeBitmapSmall = Bitmap.createScaledBitmap(spikeBitmap, Spike6.getRoundedWidth(), Spike6.getBlockHeight(),false);
        spikeBitmapLarge = Bitmap.createScaledBitmap(spikeBitmap, Spike4.getRoundedWidth(), Spike4.getRoundedHeight(),false);

        enemyPopBottleBitmapArray = new Bitmap[]{enemyPopBottleBitmap0,enemyPopBottleBitmap1,enemyPopBottleBitmap2,enemyPopBottleBitmap3,enemyPopBottleBitmap4,enemyPopBottleBitmap5,enemyPopBottleBitmap6,enemyPopBottleBitmap7,enemyPopBottleBitmap8,enemyPopBottleBitmap9};
        charBitmapArray = new Bitmap[]{charBitmap0, charBitmap1, charBitmap2, charBitmap3, charBitmap4, charBitmap5, charBitmap6, charBitmap7, charBitmap8, charBitmap9};
        charWalkBitmapArray = new Bitmap[]{charWalkBitmap0, charWalkBitmap1, charWalkBitmap2, charWalkBitmap3, charWalkBitmap4, charWalkBitmap5, charWalkBitmap6, charWalkBitmap7, charWalkBitmap8, charWalkBitmap9};
        charJabBitmapArray = new Bitmap[]{charJabBitmap0,charJabBitmap1,charJabBitmap2,charJabBitmap3,charJabBitmap4,charJabBitmap5,charJabBitmap6,charJabBitmap7,charJabBitmap8,charJabBitmap9};

    }
}
