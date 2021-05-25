
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

public class FinalLevel extends Activity {

    Intent i;
    Intent startShop;

    int currency = 0;
    int lvl = 5;
    int screenWidth;
    int screenHeight;

    PlayerInfo PlayerInfo;
    Character Enemy1;
    Character GrassPlatform;
    Character GrassPlatform2;
    Character JumpButtonDown;
    Character dpadLeft;
    Character dpadRight;
    Character Portal;
    Character PunchButton;
    Character Heart;
    Character Coin;
    Character Knight;

    int CharBlockBottomGap = 3;

    boolean CharMoveLeft;
    boolean CharMoveRight;
    boolean CharMoveUp;
    boolean CharMoveDown;
    boolean Enemy1MoveLeft = true;
    boolean KnightMoveLeft = true;
    boolean KnightPunch = false;
    boolean KnightPunchLeft = false;


    double blockSize;
    int numBlocksWide;
    int numBlocksHigh;
    int mysteriousBottomGapBlock = 15;

    boolean noPush = true;

    boolean noFlip = true;
    int ground1;
    boolean noGravity = false;
    boolean knightNoGravity = false;

    int tempUp = 1;
    boolean tempUpFinal;

    Canvas canvas;
    GameActivityView gameActivityView;

    Bitmap grassPlatformBitmap;
    Bitmap jumpNoPushBitmap, jumpPushBitmap;
    Bitmap dpadLeftBitmap;
    Bitmap dpadRightBitmap;
    Bitmap portalBitmap;
    Bitmap punchButtonBitmap;
    Bitmap heartBitmap, coinBitmap;
    Bitmap charBitmap0, charBitmap1, charBitmap2, charBitmap3, charBitmap4, charBitmap5, charBitmap6, charBitmap7, charBitmap8, charBitmap9;
    Bitmap enemyPopBottleBitmap0,enemyPopBottleBitmap1,enemyPopBottleBitmap2,enemyPopBottleBitmap3,enemyPopBottleBitmap4,enemyPopBottleBitmap5,enemyPopBottleBitmap6,enemyPopBottleBitmap7,enemyPopBottleBitmap8,enemyPopBottleBitmap9;
    Bitmap charWalkBitmap0,charWalkBitmap1,charWalkBitmap2,charWalkBitmap3,charWalkBitmap4,charWalkBitmap5,charWalkBitmap6,charWalkBitmap7,charWalkBitmap8,charWalkBitmap9;
    Bitmap charJabBitmap0,charJabBitmap1,charJabBitmap2,charJabBitmap3,charJabBitmap4,charJabBitmap5,charJabBitmap6,charJabBitmap7,charJabBitmap8,charJabBitmap9;
    Bitmap knightMove0, knightMove1,knightMove2,knightMove3,knightMove4,knightMove5,knightMove6,knightMove7,knightMove8,knightMove9;
    Bitmap knightPunch0, knightPunch1,knightPunch2,knightPunch3,knightPunch4,knightPunch5,knightPunch6,knightPunch7,knightPunch8,knightPunch9;
    Bitmap[] knightMoveBitmapArray;
    Bitmap[] knightPunchBitmapArray;
    Bitmap[] charJabBitmapArray;
    Bitmap[] charBitmapArray;
    Bitmap[] charWalkBitmapArray;
    Bitmap[] popBottleArray;
    Bitmap backgroundBitmap, groundBitmap;


    int charFrame = 0;
    int jabFrame = 0;
    boolean charJab = false;
    boolean invincibility = false;
    int invincibleFrames = 0;
    boolean playerFlash = false;
    int trackPlayerPlatform = 0;
    int trackKnightPlatform = 0;
    int punchSequence = 0;
    boolean punchSequenceStart =  false;

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
        startShop = new Intent(this, Shop.class);
        PlayerInfo.setHealth(getIntent().getIntExtra("health", 0));
        currency = getIntent().getIntExtra("currency", 0);
        PlayerInfo.setSpeed(7 + getIntent().getIntExtra("speed",0));

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

            CharMoveDown = true;
           /*
           //our starting snake
           getSnake();
           //get an apple to munch
           getApple();
           */
        }

        public void gravity() {
            if (noGravity == false) {
                PlayerInfo.setBlockY(PlayerInfo.getBlockY() + 10);
            }
        }
        public void knightgravity() {
            if (knightNoGravity == false) {
                Knight.setBlockY(Knight.getBlockY() + 1);
            }
        }

        public int applyGravityTo(int positionY){
            positionY = positionY + 10;
            return positionY;
        }

        public void checkJump(){
            //jump
            if (tempUpFinal && (tempUp == 10)){
                CharMoveUp = false;
                tempUp = 1;
                tempUpFinal = false;
            }

        }

        public void jumpIfApplicable(){
            if (CharMoveUp && (PlayerInfo.getBlockY() > 0)) {
                if (tempUp == 10) {
                    tempUpFinal = true;
                }else{
                    int diff = 0;
                    switch (tempUp){
                        case 9:
                            diff = 1;
                            break;
                        case 8:
                            diff = 2;
                            break;
                        case 7:
                            diff = 5;
                            break;
                        case 6:
                            diff = 12;
                            break;
                        case 5:
                            diff = 18;
                            break;
                        case 4:
                            diff = 23;
                            break;
                        case 3:
                            diff = 26;
                            break;
                        case 2:
                            diff = 28;
                            break;
                        case 1:
                            diff = 30;
                            break;
                    }
                    PlayerInfo.setBlockY(PlayerInfo.getBlockY() - diff);
                    tempUp++;
                }
            }

            else if (CharMoveUp && (PlayerInfo.getBlockY() < 0)){
                CharMoveUp = false;
                tempUp = 1;
                tempUpFinal = false;
            }
        }

        public void knightJump() {
            if (Knight.getBlockY() > 0) {
                Knight.setBlockY(Knight.getBlockY() -50);
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
                playerFlash = false;

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
                if (invincibility){
                    invincibleFrames++;
                    if(invincibleFrames > 9){
                        invincibleFrames = 0;
                        invincibility = false;
                        if(invincibleFrames < 5){
                            playerFlash = true;
                        }

                    }
                }
                /*if (punchSequenceStart){
                    punchSequence++;
                    if (punchSequence > 9){
                        punchSequence = 0;
                        player.setBlockX(10);
                        player.setBlockY(80);
                        player.deductHealth();
                    }
                }
                /*if (charHurt){
                    charHealth--;
                    charHurt = false;

                }*/

            }

        }

        public void updateGame() {

            //noGravity is always set to false so if it was true in the previous frame it will be set to false again
            noGravity = false;
            knightNoGravity = false;

            //checks if the character is along the width of the platform
            if (((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) > GrassPlatform.getBlockX()) && ((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) < (GrassPlatform.getBlockX() + GrassPlatform.getBlockWidth()))) {
                //checks if the character is within range of (10 block to 0 blocks above) the platform
                if (((PlayerInfo.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) >= GrassPlatform.getBlockY() - 10) && ((PlayerInfo.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) <= GrassPlatform.getBlockY())) {
                    //sets noGravity to true so the character does not continue falling
                    noGravity = true;
                    trackPlayerPlatform = 1;
                    //places the character on the platform
                    PlayerInfo.setBlockY(GrassPlatform.getBlockY() - PlayerInfo.getBlockHeight() + CharBlockBottomGap);
                }
            }
            //second platform
            if (((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) > GrassPlatform2.getBlockX()) && ((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) < (GrassPlatform2.getBlockX() + GrassPlatform.getBlockWidth()))) {
                if (((PlayerInfo.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) >= GrassPlatform2.getBlockY() - 10) && ((PlayerInfo.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) <= GrassPlatform2.getBlockY())) {
                    noGravity = true;
                    trackKnightPlatform = 2;
                    PlayerInfo.setBlockY(GrassPlatform2.getBlockY() - PlayerInfo.getBlockHeight() + CharBlockBottomGap);
                }
            }


            if (((Knight.getBlockX() + (PlayerInfo.getBlockWidth()/2)) > GrassPlatform.getBlockX()) && ((Knight.getBlockX() + (PlayerInfo.getBlockWidth()/2)) < (GrassPlatform.getBlockX() + GrassPlatform.getBlockWidth()))) {
                //checks if the character is within range of (10 block to 0 blocks above) the platform
                if (((Knight.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) >= GrassPlatform.getBlockY() - 10) && ((Knight.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) <= GrassPlatform.getBlockY())) {
                    //sets noGravity to true so the character does not continue falling
                    knightNoGravity = true;
                    trackPlayerPlatform = 1;
                    //places the character on the platform
                    Knight.setBlockY(GrassPlatform.getBlockY() - Knight.getBlockHeight());
                }
            }
            //second platform
            if (((Knight.getBlockX()  + (PlayerInfo.getBlockWidth()/2)) > GrassPlatform2.getBlockX()) && ((Knight.getBlockX() + (PlayerInfo.getBlockWidth()/2)) < (GrassPlatform2.getBlockX() + GrassPlatform.getBlockWidth()))) {
                if (((Knight.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) >= GrassPlatform2.getBlockY() - 10) && ((Knight.getBlockY() + PlayerInfo.getBlockHeight() - CharBlockBottomGap) <= GrassPlatform2.getBlockY())) {
                    knightNoGravity = true;
                    trackKnightPlatform = 2;
                    Knight.setBlockY(GrassPlatform2.getBlockY() - Knight.getBlockHeight());
                }
            }





            // "charPosition.x < numBlocksWide - charWidth" restricts it from going any further to the right
            if (CharMoveRight && (PlayerInfo.getBlockX() < (numBlocksWide - PlayerInfo.getBlockWidth()))) {
                //move char right by 7
                PlayerInfo.setBlockX(PlayerInfo.getBlockX() + PlayerInfo.getSpeed());
            }

            // "charPosition.x > 0" restricts it from going any further to the left of it is smaller than 0
            if (CharMoveLeft && (PlayerInfo.getBlockX() > 0)) {
                //move char left by 7
                PlayerInfo.setBlockX(PlayerInfo.getBlockX() - PlayerInfo.getSpeed());
            }


            //call the jumpIfApplicable method
            jumpIfApplicable();

            /*if (trackPlayerPlatform > trackKnightPlatform){
                knightJump();
            }
            */

            //if the player is in the air, apply gravity
            if (PlayerInfo.getBlockY() < (ground1 - PlayerInfo.getBlockHeight())) {
                gravity();
            }

            //if the knight is in the air, apply gravity
            if (Knight.getBlockY() < (ground1 - Knight.getBlockHeight())) {
                knightgravity();
            }

            //if the player has hit the ground or lower, set the player on the ground
            if (PlayerInfo.getBlockY() >= (ground1 - PlayerInfo.getBlockHeight())){
                PlayerInfo.setBlockY(ground1 - PlayerInfo.getBlockHeight());
            }

            //if the knight has hit the ground or lower, set the player on the ground
            if (Knight.getBlockY() >= (ground1 - Knight.getBlockHeight())){
                Knight.setBlockY(ground1 - Knight.getBlockHeight());
            }

            //if enemy is in the air, apply gravity
            if (Enemy1.getBlockY() < (ground1 - Enemy1.getBlockHeight())){
                Enemy1.setBlockY(applyGravityTo(Enemy1.getBlockY()));
            }

            if (Enemy1.getBlockX() > (400 - Enemy1.getBlockWidth())){
                Enemy1MoveLeft = true;
            }

            if (Enemy1MoveLeft){
                Enemy1.setBlockX(Enemy1.getBlockX() - 5);
            }

            if (Enemy1.getBlockX() < 0){
                Enemy1MoveLeft = false;
            }

            if (!Enemy1MoveLeft){
                Enemy1.setBlockX(Enemy1.getBlockX() + 5);
            }

            //collision detection between the player and enemy1
            if ((Math.abs((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) - (Enemy1.getBlockX() + (Enemy1.getBlockWidth()/2))) <= (PlayerInfo.getBlockWidth() + Enemy1.getBlockWidth())/2 - 15)
                    && (Math.abs((PlayerInfo.getBlockY() + (PlayerInfo.getBlockHeight()/2)) - (Enemy1.getBlockY() + (Enemy1.getBlockHeight()/2))) <= (PlayerInfo.getBlockHeight() + Enemy1.getBlockHeight())/2 - 10)){
                if (charJab) {
                    Enemy1.setBlockX(-200);
                    Enemy1.setBlockY(0);
                    currency += 3;
                }
                else if (!invincibility) {
                    if (PlayerInfo.getHealth() > 0) {
                        invincibility = true;
                        PlayerInfo.deductHealth();
                    }
                    else {

                        startActivity(i);
                    }
                }
                else{
                    PlayerInfo.setBlockX(10);
                    PlayerInfo.setBlockY(80);
                }

            }

            if ((Math.abs((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) - (Knight.getBlockX() + (Knight.getBlockWidth()/2))) <= (Knight.getBlockWidth() + Knight.getBlockWidth())/2 - 15)
                    && (Math.abs((PlayerInfo.getBlockY() + (PlayerInfo.getBlockHeight()/2)) - (Knight.getBlockY() + (Knight.getBlockHeight()/2))) <= (Knight.getBlockHeight() + Knight.getBlockHeight())/2 - 40)){

                if (!invincibility) {
                    if (PlayerInfo.getHealth() > 0) {
                        invincibility = true;
                        PlayerInfo.deductHealth();
                    }
                    else {

                        startActivity(i);
                    }
                }
                else{
                    PlayerInfo.setBlockX(10);
                    PlayerInfo.setBlockY(80);
                }

            }

            if (PlayerInfo.getBlockX() < Knight.getBlockX()){
                if ((Knight.getBlockX() - PlayerInfo.getBlockX()) < 40){
                    if (trackKnightPlatform == trackPlayerPlatform){
                        KnightPunch = true;
                        KnightPunchLeft = false;
                        if (Knight.getBlockX() > 100) {
                            Knight.setBlockX(Knight.getBlockX() - 8);

                        }
                    }
                }
                else{
                    if (Knight.getBlockX() > 100) {
                        Knight.setBlockX(Knight.getBlockX() - 5);
                        KnightPunch = false;
                        punchSequenceStart = false;
                    }
                }
                KnightMoveLeft = false;
            }

            if (PlayerInfo.getBlockX() > Knight.getBlockX()){
                if ((Knight.getBlockX() - PlayerInfo.getBlockX()) > 40){
                    if (trackKnightPlatform == trackPlayerPlatform){
                        KnightPunch = true;
                        KnightPunchLeft = true;
                        Knight.setBlockX(Knight.getBlockX() + 8);

                    }
                }
                else{
                    Knight.setBlockX(Knight.getBlockX() + 5);
                    KnightPunch = false;
                    punchSequenceStart = false;
                }
                KnightMoveLeft = true;
            }

            /*if (!knightMoveLeft){
                knight.setBlockX(knight.getBlockX() - 7);
            }

            if (knightMoveLeft){
                knight.setBlockX(knight.getBlockX() + 7);
            }
            */


            //portal
            if ((Math.abs((PlayerInfo.getBlockX() + (PlayerInfo.getBlockWidth()/2)) - (Portal.getBlockX() + (Portal.getBlockWidth()/2))) <= (PlayerInfo.getBlockWidth() + Portal.getBlockWidth())/2 - 15)
                    && (Math.abs((PlayerInfo.getBlockY() + (PlayerInfo.getBlockHeight()/2)) - (Portal.getBlockY() + (Portal.getBlockHeight()/2))) <= (PlayerInfo.getBlockHeight() + Portal.getBlockHeight())/2 - 10)){
                startShop.putExtra("level", lvl);
                startShop.putExtra("health", PlayerInfo.getHealth());
                startShop.putExtra("speed", PlayerInfo.getSpeed());
                startShop.putExtra("currency", currency);
                startShop.putExtra("lvl", 2);
                startActivity(startShop);
            }

            //if char is jumps above the screen, make it reappear at the bottom
           /*
           if (charPosition.y < 0) {
              charPosition.y = screenHeight - charWidth;
           }
            */

            PlayerInfo.setPositionX((int) Math.round(PlayerInfo.getBlockX() * blockSize));
            PlayerInfo.setPositionY((int) Math.round(PlayerInfo.getBlockY() * blockSize));

            Enemy1.setPositionX((int) Math.round(Enemy1.getBlockX() * blockSize));
            Enemy1.setPositionY((int) Math.round(Enemy1.getBlockY() * blockSize));

            GrassPlatform.setPositionX((int) Math.round(GrassPlatform.getBlockX() * blockSize));
            GrassPlatform.setPositionY((int) Math.round(GrassPlatform.getBlockY() * blockSize));

            GrassPlatform2.setPositionX((int) Math.round(GrassPlatform2.getBlockX() * blockSize));
            GrassPlatform2.setPositionY((int) Math.round(GrassPlatform2.getBlockY() * blockSize));

            JumpButtonDown.setPositionX((int) Math.round(JumpButtonDown.getBlockX() * blockSize));
            JumpButtonDown.setPositionY((int) Math.round(JumpButtonDown.getBlockY() * blockSize));

            dpadLeft.setPositionX((int) Math.round(dpadLeft.getBlockX() * blockSize));
            dpadLeft.setPositionY((int) Math.round(dpadLeft.getBlockY() * blockSize));

            dpadRight.setPositionX((int) Math.round(dpadRight.getBlockX() * blockSize));
            dpadRight.setPositionY((int) Math.round(dpadRight.getBlockY() * blockSize));

            Portal.setPositionX((int) Math.round(Portal.getBlockX() * blockSize));
            Portal.setPositionY((int) Math.round(Portal.getBlockY() * blockSize));

            Knight.setPositionX((int) Math.round(Knight.getBlockX() * blockSize));
            Knight.setPositionY((int) Math.round(Knight.getBlockY() * blockSize));

        }

        public void drawGame() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.BLACK);//the background
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(screenWidth/20);

                canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
                canvas.drawBitmap(groundBitmap, 0, ((int) Math.round(ground1 * blockSize)), paint);

                //canvas.drawText(player.getBlockX() + ", " + player.getBlockY() +" fps:" + fps +", charFrame: " + charFrame + " numBlocksWide: " + numBlocksWide + " numBlocksHigh: " + numBlocksHigh
                //        + " " + (player.getBlockY() + player.getBlockHeight() - charBlockBottomGap) +" "+ temp +" "+ charHealth + " " + invincibility + " " + inviFrame + " posx" + enemy1.getPositionX() + " " + enemy1.getPositionY() + " blockx" + enemy1.getBlockX() + " " + enemy1.getBlockY(), 20, 40, paint);

                canvas.drawBitmap(portalBitmap, Portal.getPositionX(), Portal.getPositionY(), paint);
                //draw both grass platforms
                //canvas.drawBitmap(grassPlatformBitmap, grassPlatform.getPositionX(), grassPlatform.getPositionY(), paint);
                //canvas.drawBitmap(grassPlatformBitmap, grassPlatform2.getPositionX(), grassPlatform2.getPositionY(), paint);

                if (CharMoveRight){
                    noFlip = true;
                }
                if (CharMoveLeft){
                    noFlip = false;
                }
                //canvas.drawBitmap(knightMoveBitmapArray[charFrame], knight.getPositionX(), knight.getPositionY(), paint);
                if (!KnightMoveLeft){

                    canvas.drawBitmap(knightMoveBitmapArray[charFrame], Knight.getPositionX(), Knight.getPositionY(), paint);

                }
                if(KnightMoveLeft){
                    Matrix flipHorizontalMatrix = new Matrix();
                    flipHorizontalMatrix.setScale(-1,1);
                    Knight.setPositionX(Knight.getPositionX() + (int) Math.round(Knight.getBlockWidth() * blockSize));
                    flipHorizontalMatrix.postTranslate(Knight.getPositionX(), Knight.getPositionY());

                    canvas.drawBitmap(knightMoveBitmapArray[charFrame],flipHorizontalMatrix, paint);

                }



                if (!Enemy1MoveLeft){
                    canvas.drawBitmap(popBottleArray[charFrame], Enemy1.getPositionX(), Enemy1.getPositionY(), paint);
                }
                if(Enemy1MoveLeft){
                    Matrix flipHorizontalMatrix = new Matrix();
                    flipHorizontalMatrix.setScale(-1,1);
                    Enemy1.setPositionX(Enemy1.getPositionX() + (int) Math.round(Enemy1.getBlockWidth() * blockSize));
                    flipHorizontalMatrix.postTranslate(Enemy1.getPositionX(), Enemy1.getPositionY());

                    //player.setPositionX(player.getPositionX() + (int) Math.round(player.getBlockWidth() * blockSize));
                    //flipHorizontalMatrix.postTranslate(player.getPositionX(), player.getPositionY());

                    canvas.drawBitmap(popBottleArray[charFrame],flipHorizontalMatrix, paint);
                }

                if (noFlip) {

                    if (CharMoveRight) {
                        if (!playerFlash) {
                            canvas.drawBitmap(charWalkBitmapArray[charFrame], PlayerInfo.getPositionX(), PlayerInfo.getPositionY(), paint);
                        }
                    }
                    if (charJab) {

                        canvas.drawBitmap(charJabBitmapArray[jabFrame], PlayerInfo.getPositionX(), PlayerInfo.getPositionY(), paint);
                    }
                    else if (!CharMoveRight) {
                        if (!playerFlash) {
                            canvas.drawBitmap(charBitmapArray[charFrame], PlayerInfo.getPositionX(), PlayerInfo.getPositionY(), paint);
                        }
                    }
                }

                if (!noFlip){
                    //Flipping the character when facing to the left
                    Matrix flipHorizontalMatrix = new Matrix();
                    flipHorizontalMatrix.setScale(-1,1);
                    PlayerInfo.setPositionX(PlayerInfo.getPositionX() + (int) Math.round(PlayerInfo.getBlockWidth() * blockSize));
                    flipHorizontalMatrix.postTranslate(PlayerInfo.getPositionX(), PlayerInfo.getPositionY());


                    if (CharMoveLeft){
                        if (!playerFlash) {
                            canvas.drawBitmap(charWalkBitmapArray[charFrame], flipHorizontalMatrix, paint);
                        }
                    }
                    if (charJab) {

                        canvas.drawBitmap(charJabBitmapArray[jabFrame], flipHorizontalMatrix, paint);
                    }

                    else if (!CharMoveLeft) {
                        if (!playerFlash) {
                            canvas.drawBitmap(charBitmapArray[charFrame], flipHorizontalMatrix, paint);
                        }
                    }
                }



                canvas.drawBitmap(dpadLeftBitmap, dpadLeft.getPositionX(), dpadLeft.getPositionY(), paint);
                canvas.drawBitmap(dpadRightBitmap, dpadRight.getPositionX(), dpadRight.getPositionY(), paint);
                canvas.drawBitmap(punchButtonBitmap, PunchButton.getPositionX(), PunchButton.getPositionY(), paint);
                canvas.drawBitmap(heartBitmap, Heart.getPositionX(), Heart.getPositionY(), paint);
                canvas.drawText("x"+ PlayerInfo.getHealth(), Heart.getPositionX() + Heart.getRoundedWidth(), Heart.getPositionY() + Heart.getRoundedHeight(), paint);
                canvas.drawBitmap(coinBitmap, Coin.getPositionX(), Coin.getPositionY(), paint);
                canvas.drawText("x"+ currency, Coin.getPositionX() + Coin.getRoundedWidth(), Coin.getPositionY() + (int) Math.round(Coin.getRoundedHeight() * 0.8), paint);

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
                        if (noGravity == true || PlayerInfo.getBlockY() == (ground1 - PlayerInfo.getBlockHeight())){
                            CharMoveUp = true;
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
                        CharMoveRight = true;
                        CharMoveLeft = false;
                        charJab = false;
                        pointer1Right = true;
                        //firstTouchCycle = true;

                    }
                    //move left
                    if (generalButtonTouchEvent(motionEvent.getX(), motionEvent.getY(), dpadLeft.getPositionX(), dpadLeft.getPositionY(), dpadLeft.getBlockWidth(), dpadLeft.getBlockHeight())){
                        CharMoveLeft = true;
                        CharMoveRight = false;
                        charJab = false;
                        pointer1Left = true;
                        //firstTouchCycle = true;

                    }
                    //Jab animation when we click the jab button
                    if (generalButtonTouchEvent(motionEvent.getX(),motionEvent.getY(), PunchButton.getPositionX(), PunchButton.getPositionY(), PunchButton.getBlockWidth(), PunchButton.getBlockWidth())){
                        CharMoveRight = false;
                        CharMoveLeft = false;
                        charJab = true;

                        pointer1Jab = true;

                    }

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = motionEvent.getActionIndex();


                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), JumpButtonDown.getPositionX(), JumpButtonDown.getPositionY(), JumpButtonDown.getBlockWidth(), JumpButtonDown.getBlockHeight())){
                        //making it so that you can only jump once this 702-3453251-5641836is hardcoded for this map specifically for now.
                        if (noGravity == true || PlayerInfo.getBlockY() == (ground1 - PlayerInfo.getBlockHeight())){
                            CharMoveUp = true;
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
                        CharMoveRight = true;
                        CharMoveLeft = false;
                        charJab = false;
                        pointer2Right = true;
                        //firstTouchCycle = true;

                    }
                    //move left
                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), dpadLeft.getPositionX(), dpadLeft.getPositionY(), dpadLeft.getBlockWidth(), dpadLeft.getBlockHeight())){
                        CharMoveLeft = true;
                        CharMoveRight = false;
                        charJab = false;
                        pointer2Left = true;
                        //firstTouchCycle = true;

                    }
                    //Jab animation when we click the jab button
                    if (generalButtonTouchEvent(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), PunchButton.getPositionX(), PunchButton.getPositionY(), PunchButton.getBlockWidth(), PunchButton.getBlockWidth())){
                        CharMoveRight = false;
                        CharMoveLeft = false;
                        charJab = true;

                        pointer2Jab = true;

                    }

                    break;


                case MotionEvent.ACTION_UP:

                    if (!pointer1Jump) {
                        noPush = true;
                    }
                    CharMoveRight = false;
                    CharMoveLeft = false;

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




        PlayerInfo = new PlayerInfo(20,0,50,30,blockSize);
        PlayerInfo.setHealth(30);
        PlayerInfo.setSpeed(7);
        Enemy1 = new Character(360,0,50,30,blockSize);
        GrassPlatform = new Character(67,128,10,400-67,blockSize);
        GrassPlatform2 = new Character(174,66,10,400-174,blockSize);
        JumpButtonDown = new Character(350,numBlocksHigh - mysteriousBottomGapBlock - 50,40,40,blockSize);
        dpadLeft = new Character(10,numBlocksHigh - mysteriousBottomGapBlock - 50,40,40,blockSize);
        dpadRight = new Character(70,numBlocksHigh - mysteriousBottomGapBlock - 50,40,40,blockSize);
        Portal = new Character(360,10,60,30,blockSize);
        PunchButton = new Character(300,numBlocksHigh - mysteriousBottomGapBlock - 50,40,40,blockSize);
        Heart = new Character(5,5,20, 25, blockSize);
        Coin = new Character(60,5,25,25,blockSize);
        Knight = new Character(350,60, 70,70, blockSize);



        int jabWidth = PlayerInfo.getBlockWidth() + (int) Math.round(blockSize * 45);

        ground1 = numBlocksHigh - 30;

        //Load bitmaps

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rainybackground);
        groundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rainybackgroundground);
        heartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
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
        grassPlatformBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.concreteplateform);
        jumpNoPushBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jumpnopush);
        jumpPushBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jumppush);
        dpadLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dpadleft);
        dpadRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dpadright);
        portalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.portal);
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
        punchButtonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.punchbutton);
        knightMove0 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_000);
        knightMove1 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_001);
        knightMove2 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_002);
        knightMove3 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_003);
        knightMove4 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_004);
        knightMove5 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_005);
        knightMove6 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_006);
        knightMove7 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_007);
        knightMove8 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_008);
        knightMove9 = BitmapFactory.decodeResource(getResources(), R.drawable.knightmove_009);
        knightPunch0 = BitmapFactory.decodeResource(getResources(), R.drawable.android_000);
        knightPunch1 = BitmapFactory.decodeResource(getResources(), R.drawable.android_001);
        knightPunch2 = BitmapFactory.decodeResource(getResources(), R.drawable.android_002);
        knightPunch3 = BitmapFactory.decodeResource(getResources(), R.drawable.android_003);
        knightPunch4 = BitmapFactory.decodeResource(getResources(), R.drawable.android_004);
        knightPunch5 = BitmapFactory.decodeResource(getResources(), R.drawable.android_005);
        knightPunch6 = BitmapFactory.decodeResource(getResources(), R.drawable.android_006);
        knightPunch7 = BitmapFactory.decodeResource(getResources(), R.drawable.android_007);
        knightPunch8 = BitmapFactory.decodeResource(getResources(), R.drawable.android_008);
        knightPunch9 = BitmapFactory.decodeResource(getResources(), R.drawable.android_009);





        //scale the bitmaps to match the block size
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenWidth, screenHeight, false);
        groundBitmap = Bitmap.createScaledBitmap(groundBitmap, screenWidth, screenHeight - ((int) Math.round(ground1 * blockSize)), false);
        heartBitmap = Bitmap.createScaledBitmap(heartBitmap, Heart.getRoundedWidth(), Heart.getRoundedHeight(), false);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, Coin.getRoundedWidth(), Coin.getRoundedHeight(), false);
        charBitmap0 = Bitmap.createScaledBitmap(charBitmap0, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap1 = Bitmap.createScaledBitmap(charBitmap1, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap2 = Bitmap.createScaledBitmap(charBitmap2, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap3 = Bitmap.createScaledBitmap(charBitmap3, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap4 = Bitmap.createScaledBitmap(charBitmap4, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap5 = Bitmap.createScaledBitmap(charBitmap5, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap6 = Bitmap.createScaledBitmap(charBitmap6, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap7 = Bitmap.createScaledBitmap(charBitmap7, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap8 = Bitmap.createScaledBitmap(charBitmap8, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charBitmap9 = Bitmap.createScaledBitmap(charBitmap9, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);

        charWalkBitmap0 = Bitmap.createScaledBitmap(charWalkBitmap0, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap1 = Bitmap.createScaledBitmap(charWalkBitmap1, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap2 = Bitmap.createScaledBitmap(charWalkBitmap2, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap3 = Bitmap.createScaledBitmap(charWalkBitmap3, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap4 = Bitmap.createScaledBitmap(charWalkBitmap4, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap5 = Bitmap.createScaledBitmap(charWalkBitmap5, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap6 = Bitmap.createScaledBitmap(charWalkBitmap6, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap7 = Bitmap.createScaledBitmap(charWalkBitmap7, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap8 = Bitmap.createScaledBitmap(charWalkBitmap8, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charWalkBitmap9 = Bitmap.createScaledBitmap(charWalkBitmap9, PlayerInfo.getRoundedWidth(), PlayerInfo.getRoundedHeight(), false);
        charJabBitmap0 = Bitmap.createScaledBitmap(charJabBitmap0,jabWidth , PlayerInfo.getRoundedHeight(), false);
        charJabBitmap1 = Bitmap.createScaledBitmap(charJabBitmap1,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap2 = Bitmap.createScaledBitmap(charJabBitmap2,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap3 = Bitmap.createScaledBitmap(charJabBitmap3,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap4 = Bitmap.createScaledBitmap(charJabBitmap4,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap5 = Bitmap.createScaledBitmap(charJabBitmap5,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap6 = Bitmap.createScaledBitmap(charJabBitmap6,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap7 = Bitmap.createScaledBitmap(charJabBitmap7,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap8 = Bitmap.createScaledBitmap(charJabBitmap8,jabWidth, PlayerInfo.getRoundedHeight(), false);
        charJabBitmap9 = Bitmap.createScaledBitmap(charJabBitmap9,jabWidth, PlayerInfo.getRoundedHeight(), false);
        enemyPopBottleBitmap0 = Bitmap.createScaledBitmap(enemyPopBottleBitmap0, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap1 = Bitmap.createScaledBitmap(enemyPopBottleBitmap1, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap2 = Bitmap.createScaledBitmap(enemyPopBottleBitmap2, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap3 = Bitmap.createScaledBitmap(enemyPopBottleBitmap3, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap4 = Bitmap.createScaledBitmap(enemyPopBottleBitmap4, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap5 = Bitmap.createScaledBitmap(enemyPopBottleBitmap5, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap6 = Bitmap.createScaledBitmap(enemyPopBottleBitmap6, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap7 = Bitmap.createScaledBitmap(enemyPopBottleBitmap7, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap8 = Bitmap.createScaledBitmap(enemyPopBottleBitmap8, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        enemyPopBottleBitmap9 = Bitmap.createScaledBitmap(enemyPopBottleBitmap9, Enemy1.getRoundedWidth(), Enemy1.getRoundedHeight(),false);
        knightMove0 = Bitmap.createScaledBitmap(knightMove0, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove1 = Bitmap.createScaledBitmap(knightMove1, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove2 = Bitmap.createScaledBitmap(knightMove2, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove3 = Bitmap.createScaledBitmap(knightMove3, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove4 = Bitmap.createScaledBitmap(knightMove4, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove5 = Bitmap.createScaledBitmap(knightMove5, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove6 = Bitmap.createScaledBitmap(knightMove6, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove7 = Bitmap.createScaledBitmap(knightMove7, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove8 = Bitmap.createScaledBitmap(knightMove8, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightMove9 = Bitmap.createScaledBitmap(knightMove9, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch0 = Bitmap.createScaledBitmap(knightPunch0, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch1 = Bitmap.createScaledBitmap(knightPunch1, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch2 = Bitmap.createScaledBitmap(knightPunch2, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch3 = Bitmap.createScaledBitmap(knightPunch3, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch4 = Bitmap.createScaledBitmap(knightPunch4, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch5 = Bitmap.createScaledBitmap(knightPunch5, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch6 = Bitmap.createScaledBitmap(knightPunch6, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch7 = Bitmap.createScaledBitmap(knightPunch7, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch8 = Bitmap.createScaledBitmap(knightPunch8, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);
        knightPunch9 = Bitmap.createScaledBitmap(knightPunch9, Knight.getRoundedWidth(), Knight.getRoundedHeight(),false);

        grassPlatformBitmap = Bitmap.createScaledBitmap(grassPlatformBitmap, GrassPlatform.getRoundedWidth(), GrassPlatform.getRoundedHeight(), false);

        jumpNoPushBitmap = Bitmap.createScaledBitmap(jumpNoPushBitmap, JumpButtonDown.getRoundedWidth(), JumpButtonDown.getRoundedHeight(), false);

        jumpPushBitmap = Bitmap.createScaledBitmap(jumpPushBitmap, JumpButtonDown.getRoundedWidth(), JumpButtonDown.getRoundedHeight(), false);

        dpadLeftBitmap = Bitmap.createScaledBitmap(dpadLeftBitmap, dpadLeft.getRoundedWidth(), dpadLeft.getRoundedHeight(), false);

        dpadRightBitmap = Bitmap.createScaledBitmap(dpadRightBitmap, dpadRight.getRoundedWidth(), dpadRight.getRoundedHeight(), false);

        portalBitmap = Bitmap.createScaledBitmap(portalBitmap, Portal.getRoundedWidth(), Portal.getRoundedHeight(), false);

        punchButtonBitmap = Bitmap.createScaledBitmap(punchButtonBitmap, PunchButton.getRoundedWidth(), PunchButton.getRoundedHeight(),false);



        charBitmapArray = new Bitmap[]{charBitmap0, charBitmap1, charBitmap2, charBitmap3, charBitmap4, charBitmap5, charBitmap6, charBitmap7, charBitmap8, charBitmap9};
        charWalkBitmapArray = new Bitmap[]{charWalkBitmap0, charWalkBitmap1, charWalkBitmap2, charWalkBitmap3, charWalkBitmap4, charWalkBitmap5, charWalkBitmap6, charWalkBitmap7, charWalkBitmap8, charWalkBitmap9};
        charJabBitmapArray = new Bitmap[]{charJabBitmap0,charJabBitmap1,charJabBitmap2,charJabBitmap3,charJabBitmap4,charJabBitmap5,charJabBitmap6,charJabBitmap7,charJabBitmap8,charJabBitmap9};
        popBottleArray = new Bitmap[]{enemyPopBottleBitmap0, enemyPopBottleBitmap1,enemyPopBottleBitmap2,enemyPopBottleBitmap3,enemyPopBottleBitmap4,enemyPopBottleBitmap5,enemyPopBottleBitmap6,enemyPopBottleBitmap7,enemyPopBottleBitmap8,enemyPopBottleBitmap9};
        knightMoveBitmapArray = new Bitmap[]{knightMove0,knightMove1,knightMove2,knightMove3,knightMove4,knightMove5,knightMove6,knightMove7,knightMove8,knightMove9};
        knightPunchBitmapArray = new Bitmap[]{knightPunch0, knightPunch1,knightPunch2,knightPunch3,knightPunch4,knightPunch5,knightPunch6,knightPunch7,knightPunch8,knightPunch9};

    }


}


