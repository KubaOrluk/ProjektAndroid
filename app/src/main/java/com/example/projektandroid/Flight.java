package com.example.projektandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.projektandroid.GameView.screenRatioX;
import static com.example.projektandroid.GameView.screenRatioY;

public class Flight {

    public boolean isGoingUp = false;
    int x, y, width, height, toShoot = 0, shootCounter = 1;
    boolean wingBool = false;
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5;
    private GameView gameView;

    Flight (GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX); //rzutowanie, screenRatioX jest floatem
        height = (int) (height * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false); //resize
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);
    }

    Bitmap getFlight () { //funkcja zwraca bitmape

        if(toShoot != 0) {
            if (shootCounter == 1){
                shootCounter++;
                return shoot1;
            }

            if (shootCounter == 2){
                shootCounter++;
                return shoot2;
            }

            if (shootCounter == 3){
                shootCounter++;
                return shoot3;
            }

            if (shootCounter == 4){
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();

            return shoot5;
        }

        if (wingBool) {
            wingBool = true;
            return flight1;
        }

        wingBool = false;

        return flight2;
    }
}
