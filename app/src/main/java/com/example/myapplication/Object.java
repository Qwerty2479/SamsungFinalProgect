package com.example.myapplication;

public class Object {

    private int positionX;
    private int positionY;
    private int Height;
    private int Width;
    private int BlockHeight;
    private int BlockWidth;
    private int BlockX;
    private int BlockY;
    private int Health;
    private int Speed;
    private int Weapon;


    public Object(int blockX, int blockY, int height, int width, double blockSize){
        this.positionX = (int) Math.round(blockSize * blockX);
        this.positionY = (int) Math.round(blockSize * blockY);
        this.Height = (int) Math.round(blockSize * height);
        this.Width = (int) Math.round(blockSize * width);
        this.BlockHeight = height;
        this.BlockWidth = width;
        this.BlockX = blockX;
        this.BlockY = blockY;

    }
    public int getPositionX(){
        return positionX;
    }
    public int getPositionY(){
        return positionY;
    }
    public int getRoundedHeight(){
        return Height;
    }
    public int getRoundedWidth(){
        return Width;
    }
    public int getBlockHeight(){
        return BlockHeight;
    }
    public int getBlockWidth(){
        return BlockWidth;
    }
    public int getBlockX(){
        return BlockX;
    }
    public int getBlockY(){
        return BlockY;
    }

    public void setPositionX(int newPositionX){
        this.positionX = newPositionX;
    }
    public void setPositionY(int newPositionY){
        this.positionY = newPositionY;
    }
    public void setRoundedHeight(int newRoundedHeight){
        this.Height = newRoundedHeight;
    }
    public void setRoundedWidth(int newRoundedWidth){
        this.Width = newRoundedWidth;
    }
    public void setBlockHeight(int newBlockHeight){
        this.BlockHeight = newBlockHeight;
    }
    public void setBlockWidth(int newBlockWidth){
        this.BlockWidth = newBlockWidth;
    }
    public void setBlockX(int newBlockX){
        this.BlockX = newBlockX;
    }
    public void setBlockY(int newBlockY){
        this.BlockY = newBlockY;
    }


}


