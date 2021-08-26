package com.example.projektandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY; //aby inne klasy mialy dostep
    private Paint paint;
    private List<Bullet> bullets;
    private Flight flight;
    private Background background1, background2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX; //ustawiamy wartosc naszych prywatnych obiektow
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources()); //obiekty klasy background
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background2.x = screenX; //modyfikujemy szerokosc tla ustawiajac wartosc szerokosci ekranu

        paint = new Paint();
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
        background1.x -= 10*screenRatioX; //przesuniecie o 10 pikseli, uwzgledniajac rozdzielczosc
        background2.x -= 10*screenRatioX;

        if(background1.x + background1.background.getWidth() < 0) { //gdy nasze tlo calkowicie wychodzi poza ekran
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if(flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0) //dzieki temu nie wyjdziemy poza ekran
            flight.y = 0;

        if (flight.y > screenY - flight.height) //dzieki temu nie wyjdziemy poza ekran
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();
        for (Bullet bullet: bullets) {
            if(bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;
        }

        for(Bullet bullet: trash)
            bullets.remove(bullet);
    }

    private void draw () {
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas(); //ta funkcja zwraca nam nasze plotno ktore jest teraz wyswietlane na ekranie
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet: bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            getHolder().unlockCanvasAndPost(canvas); //dzieki temu mozemy rysowac nasze obiekty na ekranie
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
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2) //klikamy prawa strone ekranu
                    flight.toShoot++;
                break;
        }
        return true; //uruchamiamy gdy uzytkownik dotknie ekranu
    }

    public void newBullet() {
        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height/2);
        bullets.add(bullet);
    }
}
