package com.example.projektandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.projektandroid.GameView.screenRatioX;
import static com.example.projektandroid.GameView.screenRatioY;

public class WinFlag {

    public boolean isGoingUp = false;
    int x, y, width, height, toShoot = 0, hourglassCounter = 1, winCounter = 0;
    Bitmap win1, win2, win3, win4, winerr1, winerr2, winerr3, winerr4;
    Bitmap hourglass1, hourglass2, hourglass3, hourglass4, hourglass5;
    private GameView gameView;

    WinFlag(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        win1 = BitmapFactory.decodeResource(res, R.drawable.win1);
        win2 = BitmapFactory.decodeResource(res, R.drawable.win2);
        win3 = BitmapFactory.decodeResource(res, R.drawable.win3);
        win4 = BitmapFactory.decodeResource(res, R.drawable.win4);

        width = win1.getWidth();
        height = win1.getHeight();

        width *= 1.5;
        height *= 1.5;

        width = (int) (width * screenRatioX); //rzutowanie, screenRatioX jest floatem
        height = (int) (height * screenRatioY);

        win1 = Bitmap.createScaledBitmap(win1, width, height, false); //resize
        win2 = Bitmap.createScaledBitmap(win2, width, height, false);
        win3 = Bitmap.createScaledBitmap(win3, width, height, false); //resize
        win4 = Bitmap.createScaledBitmap(win4, width, height, false);

        hourglass1 = BitmapFactory.decodeResource(res, R.drawable.hourglass1);
        hourglass2 = BitmapFactory.decodeResource(res, R.drawable.hourglass2);
        hourglass3 = BitmapFactory.decodeResource(res, R.drawable.hourglass3);
        hourglass4 = BitmapFactory.decodeResource(res, R.drawable.hourglass4);
        hourglass5 = BitmapFactory.decodeResource(res, R.drawable.hourglass5);

        hourglass1 = Bitmap.createScaledBitmap(hourglass1, width, height, false);
        hourglass2 = Bitmap.createScaledBitmap(hourglass2, width, height, false);
        hourglass3 = Bitmap.createScaledBitmap(hourglass3, width, height, false);
        hourglass4 = Bitmap.createScaledBitmap(hourglass4, width, height, false);
        hourglass5 = Bitmap.createScaledBitmap(hourglass5, width, height, false);

        winerr1 = BitmapFactory.decodeResource(res, R.drawable.winerr1);
        winerr1 = Bitmap.createScaledBitmap(winerr1, width, height, false);
        winerr2 = BitmapFactory.decodeResource(res, R.drawable.winerr2);
        winerr2 = Bitmap.createScaledBitmap(winerr2, width, height, false);
        winerr3 = BitmapFactory.decodeResource(res, R.drawable.winerr3);
        winerr3 = Bitmap.createScaledBitmap(winerr3, width, height, false);
        winerr4 = BitmapFactory.decodeResource(res, R.drawable.winerr4);
        winerr4 = Bitmap.createScaledBitmap(winerr4, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);
    }

    Bitmap getWinSprite() { //funkcja zwraca bitmape

        if(toShoot != 0) {
            if (hourglassCounter == 1){
                hourglassCounter++;
                return hourglass1;
            }

            if (hourglassCounter == 2){
                hourglassCounter++;
                return hourglass2;
            }

            if (hourglassCounter == 3){
                hourglassCounter++;
                return hourglass3;
            }

            if (hourglassCounter == 4){
                hourglassCounter++;
                return hourglass4;
            }

            hourglassCounter = 1;
            toShoot--;
            gameView.newBullet();

            return hourglass5;
        }

        if (winCounter == 0) {
            winCounter++;
            return win1;
        }

        if (winCounter == 1) {
            winCounter++;
            return win2;
        }

        if (winCounter == 2) {
            winCounter++;
            return win3;
        }

        winCounter = 0;

        return win4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        if (winCounter == 0) return winerr1;

        if (winCounter == 1) return winerr2;

        if (winCounter == 2) return winerr3;

        return winerr4;
    }
}
