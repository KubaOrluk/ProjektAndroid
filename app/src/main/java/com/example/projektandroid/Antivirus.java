package com.example.projektandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.projektandroid.GameView.screenRatioX;
import static com.example.projektandroid.GameView.screenRatioY;

public class Antivirus {

    int x, y, width, height;

    Bitmap Antivirus;

    Antivirus(Resources res) {
        Antivirus = BitmapFactory.decodeResource(res, R.drawable.antivirus);

        width = Antivirus.getWidth();
        height = Antivirus.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width * screenRatioX); //rzutowanie, screenRatioX jest floatem
        height = (int) (height * screenRatioY);

        Antivirus = Bitmap.createScaledBitmap(Antivirus, width, height, false);
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
