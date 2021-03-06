package com.example.projektandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, bgSpeed = 10, score = 0, meters = 0, framesCounter = 0;
    public static float screenRatioX, screenRatioY; //aby inne klasy mialy dostep
    private Paint paint;
    private Virus[] viruses;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool dingPool, chimesPool, chordPool;
    private List<Antivirus> Antiviruses;
    private int ding, chimes, chord;
    private WinFlag winFlag;
    private GameActivity activity;
    private Background background1, background2;
    Typeface winfont = ResourcesCompat.getFont(getContext(), R.font.winfont);

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        dingPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        chimesPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        chordPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        ding = dingPool.load(activity, R.raw.ding, 1);
        chimes = chimesPool.load(activity, R.raw.chimes, 1);
        chord = chordPool.load(activity, R.raw.chord, 1);

        this.screenX = screenX; //ustawiamy wartosc naszych prywatnych obiektow
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources()); //obiekty klasy background
        background2 = new Background(screenX, screenY, getResources());

        winFlag = new WinFlag(this, screenY, getResources());

        Antiviruses = new ArrayList<>();

        background2.x = screenX; //modyfikujemy szerokosc tla ustawiajac wartosc szerokosci ekranu

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        paint.setTypeface(winfont);
        paint.setTextAlign(Paint.Align.CENTER);

        viruses = new Virus[4];

        for (int i = 0; i < 4; i++){
            Virus virus = new Virus(getResources());
            viruses[i] = virus;
        }

        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying) { //petla dziala gry trwa rozgrywka
            update();
            draw();
            sleep();
        }
    }

    private void update () { //zmieniamy pozycje naszego tla
        background1.x -= bgSpeed; //przesuniecie o 10 pikseli
        background2.x -= bgSpeed;

        if(background1.x + background1.background.getWidth() < 0) { //gdy nasze tlo calkowicie wychodzi poza ekran
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if(winFlag.isGoingUp)
            winFlag.y -= 30 * screenRatioY;
        else
            winFlag.y += 30 * screenRatioY;

        if (winFlag.y < 0) //dzieki temu nie wyjdziemy poza ekran
            winFlag.y = 0;

        if (winFlag.y > screenY - winFlag.height) //dzieki temu nie wyjdziemy poza ekran
            winFlag.y = screenY - winFlag.height;

        List<Antivirus> trash = new ArrayList<>();
        for (Antivirus antivirus : Antiviruses) {
            if(antivirus.x > screenX)
                trash.add(antivirus);

            antivirus.x += 50 * screenRatioX;

            for (Virus virus : viruses){

                if(Rect.intersects(virus.getCollisionShape(), antivirus.getCollisionShape() )){

                    score++;
                    virus.x = -500;
                    antivirus.x = screenX + 500;
                    virus.wasShot = true;
                    if (!prefs.getBoolean("isMute", false))
                        chimesPool.play(chimes, 1, 1, 0, 0, 1);
                }
            }
        }

        for(Antivirus antivirus : trash)
            Antiviruses.remove(antivirus);

        for (Virus virus : viruses) {

            virus.x -= virus.speed;

            if(virus.x + virus.width < 0){

                if (!virus.wasShot) {
                    isGameOver = true;
                    if (!prefs.getBoolean("isMute", false))
                        chordPool.play(chord, 1, 1, 0, 0, 1);
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                virus.speed = random.nextInt(bound); //nadanie randomowej predkosci

                if (virus.speed < 10 * screenRatioX)
                    virus.speed = (int) (10 * screenRatioX);

                virus.x = screenX;
                virus.y = random.nextInt(screenY - virus.height);

                virus.wasShot = false;
            }

            if (Rect.intersects(virus.getCollisionShape(), winFlag.getCollisionShape())) {

                isGameOver = true;
                if (!prefs.getBoolean("isMute", false))
                    chordPool.play(chord, 1, 1, 0, 0, 1);
                return;
            }
        }

        if(framesCounter == 20){
            meters++;
            framesCounter = 0;
        }
        else framesCounter++;
    }

    private void draw () {
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas(); //ta funkcja zwraca nam nasze plotno ktore jest teraz wyswietlane na ekranie
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Virus virus : viruses)
                canvas.drawBitmap(virus.getVirus(), virus.x, virus.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);
            canvas.drawText(meters + "m", screenX / 2f, screenY - 64, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(winFlag.getDead(), winFlag.x, winFlag.y, paint);
                canvas.drawText(getContext().getString(R.string.gameover), screenX / 2f, screenY / 2f, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(winFlag.getWinSprite(), winFlag.x, winFlag.y, paint);

            for (Antivirus antivirus : Antiviruses)
                canvas.drawBitmap(antivirus.Antivirus, antivirus.x, antivirus.y, paint);
            getHolder().unlockCanvasAndPost(canvas); //dzieki temu mozemy rysowac nasze obiekty na ekranie
        }
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3500);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() { //funkcja zapisuj??ca najwy??szy wynik gracza i odleglosc

        if (prefs.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
        if (prefs.getInt("distance", 0) < meters){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("distance", meters);
            editor.apply();
        }
    }

    private void sleep () {
        try {
            Thread.sleep(17); //odswiezenie co 17ms, czyli 60 fps
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () { //funkcja wyjscia z pauzy
        isPlaying = true;
        thread = new Thread(this);
        thread.start(); //rozpoczynajac watek uruchamiamy funkcje run()
    }

    public void pause() { //funkcja pauzy
        try { //zabicie watku wyrzuca wyjatek
            isPlaying = false;
            thread.join(); //zabicie watku
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) { //klikamy lewa strone ekranu
                    winFlag.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                winFlag.isGoingUp = false;
                if (event.getX() > screenX / 2) //klikamy prawa strone ekranu
                    winFlag.toShoot++;
                break;
        }
        return true; //uruchamiamy gdy uzytkownik dotknie ekranu
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            dingPool.play(ding, 1, 1, 0, 0, 1);

        Antivirus antivirus = new Antivirus(getResources());
        antivirus.x = winFlag.x + winFlag.width;
        antivirus.y = winFlag.y + (winFlag.height/4);
        Antiviruses.add(antivirus);
    }
}
