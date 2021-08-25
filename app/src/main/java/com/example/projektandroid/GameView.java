package com.example.projektandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private float screenRatioX, screenRatioY;
    private Paint paint;
    private Background background1, background2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        this.screenX = screenX; //ustawiamy wartosc naszych prywatnych obiektow
        this.screenY = screenY;

        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources()); //obiekty klasy background
        background2 = new Background(screenX, screenY, getResources());

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
    }

    private void draw () {
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas(); //ta funkcja zwraca nam nasze plotno ktore jest teraz wyswietlane na ekranie
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

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
}
