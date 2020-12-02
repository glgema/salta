package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ggl.salta.clases.Database;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ggl.salta.GameScreen.altura;
import static com.ggl.salta.GameScreen.contMonedas;

public class GameOver implements Screen {


    private Stage stage;
    Aplication game;

    SpriteBatch batch = new SpriteBatch();

    // FONT
    public static BitmapFont font;
    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    TextButton.TextButtonStyle textButtonStyle;

    private BitmapFont font1;

    int monedasTotales;
    int alt;
    int altMaxima;

    @Override
    public void show() {

        monedasTotales = Aplication.db.getTrofeos();
        altMaxima = Aplication.db.getAltura();
        alt = altura;
        altura = 0;

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(false);
        //table.setFillParent(true);
        stage.addActor(table);


        int size = (int)(Gdx.graphics.getHeight() * 0.05f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));
        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = font;

        size = (int)(Gdx.graphics.getHeight() * 0.025f);
        parameter.size = size;
        parameter.color = Color.BLACK;
        font1 = generator.generateFont(parameter);


        TextButton playButton = new TextButton("Volver a\n jugar", textButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ChoosePjScreen());
                dispose();
            }
        });

        TextButton quitButton = new TextButton("Inicio", textButtonStyle);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
                VisUI.dispose();
                // Salir del juego
            }
        });

        // Añade filas a la tabla y añade los componentes
        table.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() * 0.3f);
        table.row();
        table.add(playButton).bottom().width(700).height(300).pad(30);
        table.row();
        table.add(quitButton).bottom().width(700).height(150).pad(30);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font1.draw(batch, "Monedas conseguidas: " + contMonedas, Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.8f);
        font1.draw(batch, "Monedas totales:   " + monedasTotales , Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.75f);

        font1.draw(batch, "Altura alcanzada:  " + alt, Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.7f);
        font1.draw(batch, "Altura maxima: " + altMaxima , Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.65f);

        batch.end();
        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();
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

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
