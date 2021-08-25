package com.example.projektandroid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res) { //szerokosc, wysokosc, zasoby katalogu drawable
        background = BitmapFactory.decodeResource(res,R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false); //skalujemy tlo odpowiednio do ekranu telefonu
    }

}
