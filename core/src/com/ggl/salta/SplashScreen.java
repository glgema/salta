package com.ggl.salta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
<<<<<<< HEAD
import com.badlogic.gdx.audio.Sound;
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;


public class SplashScreen implements Screen {
    private Texture splashTexture;
    private Image splashImage;
    private Stage stage;

    private boolean splashDone = false;

    private Aplication game;

    public static Array<Texture> fondos = new Array<>();
    public static Array<Texture> personajes = new Array<>();
    public static Array<Texture> powerUps = new Array<>();

<<<<<<< HEAD
    public static Sound botar;

=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    public SplashScreen(Aplication game) {
        this.game = game;

        splashTexture = new Texture(Gdx.files.internal("splash.png"));
        splashImage = new Image(splashTexture);
        stage = new Stage();

        cargarArrays();
<<<<<<< HEAD
        botar = Gdx.audio.newSound(Gdx.files.internal("sonidos/botar.mp3"));
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    }

    public void cargarArrays(){
        fondos.add(new Texture("fondos/fondo0.png"));
        fondos.add(new Texture("fondos/fondo1.png"));
        fondos.add(new Texture("fondos/fondo2.png"));
        fondos.add(new Texture("fondos/fondo3.png"));
        fondos.add(new Texture("fondos/fondo4.png"));

        personajes.add( new Texture("pelotas/pj1.png"));
        personajes.add( new Texture("pelotas/pj2.png"));
        personajes.add( new Texture("pelotas/pj3.png"));
        personajes.add( new Texture("pelotas/pj4.png"));
        personajes.add( new Texture("pelotas/pj5.png"));

        powerUps.add(new Texture("powerups/botas.png"));
        powerUps.add(new Texture("powerups/baloncesto.png"));
        powerUps.add(new Texture("powerups/tenis.png"));
        powerUps.add(new Texture("powerups/voleibol.png"));
        powerUps.add(new Texture("powerups/bate.png"));
    }

    @Override
    public void show() {

        Table table = new Table();
        table.setFillParent(true);
        table.center();


        splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f),
                Actions.delay(1.5f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        splashDone = true;
                    }
                })
        ));

<<<<<<< HEAD

        float width = Gdx.graphics.getWidth()*0.8f;
        float height = width * splashImage.getHeight() / splashImage.getWidth();



        table.row().width(width).height(height);
=======
        table.row().height(splashTexture.getHeight()*0.8f).width(splashTexture.getWidth()*0.8f);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        table.add(splashImage).center();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_CLEAR_VALUE);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();


        if (splashDone) {
            game.setScreen(new MainScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        splashTexture.dispose();
        stage.dispose();
    }
}
