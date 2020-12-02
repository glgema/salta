package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;


public class PreferencesScreen implements Screen {

    private Stage stage;
    SpriteBatch batch = new SpriteBatch();
    CheckBox sonido;
    CheckBox musica;
    Slider volumen;

    private OrthographicCamera camera;
    ExtendViewport viewport;


    // FONT
    public static BitmapFont font;
    public static BitmapFont font2;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    public static GlyphLayout layout;

    public static Preferences prefs = Gdx.app.getPreferences("salta");

    Aplication game;

    PreferencesScreen(Aplication game){
        this.game = game;
    }

    @Override
    public void show() {

        layout = new GlyphLayout();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(50,50,camera);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera.setToOrtho(false, w,h );
        camera.update();

        stage = new Stage(viewport,batch);
        VisTable table = new VisTable(false);
        stage.addActor(table);

        table.setPosition(camera.viewportWidth/2 , camera.viewportHeight/2);


        int size = (int)(Gdx.graphics.getHeight() * 0.019f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));
        CheckBox.CheckBoxStyle checkBoxStyle = skin.get("default", CheckBox.CheckBoxStyle.class);
        checkBoxStyle.font = font;

        parameter.color = new Color(0.886f,0.760f,0.274f ,1);
        font2 = generator.generateFont(parameter);


        musica = new CheckBox("Musica", checkBoxStyle);
        musica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        sonido = new CheckBox("Efectos de\n sonido", checkBoxStyle);
        sonido.setChecked(prefs.getBoolean("sound"));
        sonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.putBoolean("sound",sonido.isChecked());
            }
        });

       volumen = new Slider(0, 20, 1, false,skin);
       volumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        TextButton volver = new TextButton("Volver", skin);
        volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.flush();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
            }
        });

        table.row();
        table.add(musica).bottom().width(700).height(50).pad(50);
        table.row();
        table.add(sonido).bottom().width(700).height(50).pad(50);
        table.row();
        table.add(volumen).bottom().width(200).height(50);
        table.row();
        table.add(volver).bottom().width(200).height(50);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.zoom = 7;
        camera.update();

        batch.begin();

        layout.setText(font2,"VOLUMEN");
        font2.draw(batch,"VOLUMEN",camera.viewportWidth/2 - layout.width/2, camera.viewportHeight/2 - layout.height/2);

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
