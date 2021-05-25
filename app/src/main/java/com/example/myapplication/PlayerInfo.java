package com.example.myapplication;

public class PlayerInfo extends Object {
    private int Health;
    private int Speed;
    private boolean Weapon;

    public PlayerInfo(int blockX, int blockY, int height, int width, double blockSize){
        super(blockX,blockY,height,width,blockSize);
    }

    public int getHealth(){
        return Health;
    }
    public int getSpeed(){
        return Speed;
    }
    public void setHealth(int health){
        this.Health = health;
    }
    public void setSpeed(int speed){
        this.Speed = speed;
    }

    public void deductHealth(){
        this.Health--;
    }
    public void addHealth(){
        this.Health++;
    }
    public void increaseSpeed(){
        this.Speed++;
    }

}

