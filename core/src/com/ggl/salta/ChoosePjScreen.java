package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ggl.salta.clases.Aspecto;
import com.ggl.salta.clases.Database;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ChoosePjScreen implements Screen {

    Stage stage;

    Array<Aspecto> aspectos;

    private BitmapFont fontContMonedas;
    private BitmapFont fontPrecio;
    Texture textureMoneda;

    Aplication game;
    VisImage image;

    int contador;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private SpriteBatch batchHUD;


    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);


        stage = new Stage();

        aspectos = new Array<>();
        aspectos.add(new Aspecto(0,true, new Texture("elegirPelotas/pj1.png")));
        aspectos.add(new Aspecto(50,false, new Texture("elegirPelotas/pj2.png")));
        aspectos.add(new Aspecto(100,false, new Texture("elegirPelotas/pj3.png")));
        aspectos.add(new Aspecto(150,false, new Texture("elegirPelotas/pj4.png")));
        aspectos.add(new Aspecto(150,false, new Texture("elegirPelotas/pj5.png")));
        
        contador = Aplication.db.getSelection();
        image = new VisImage(aspectos.get(contador).getTexture());

        textureMoneda = new Texture(Gdx.files.internal("trofeo.png"),true);
        textureMoneda.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);


        VisTable table = new VisTable(false);
        table.setFillParent(true);
        stage.addActor(table);

        //VisLabel lPuntos = new VisLabel("Puntos");
        int size = (int)(Gdx.graphics.getHeight() * 0.025f);
        int border = (int)(Gdx.graphics.getHeight() * 0.003f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderWidth = border;
        fontContMonedas = generator.generateFont(parameter);
        fontPrecio = generator.generateFont(parameter);
        batchHUD = new SpriteBatch();

        VisTextButton bAceptar = new VisTextButton("Aceptar");
            bAceptar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Aplication.db.setSelection(contador);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                dispose();
                VisUI.dispose();
            }
        });

        VisTextButton bAtras = new VisTextButton("Atras");
        bAtras.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
                VisUI.dispose();
            }
        });

        VisTextButton bDerecha = new VisTextButton(">");
        bDerecha.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               contador++;
               if(contador >= aspectos.size)
                   contador = 0;

               image.setDrawable(aspectos.get(contador).getTexture());
            }
        });

        VisTextButton bIzquierda = new VisTextButton("<");
        bIzquierda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contador--;
                if(contador <= -1)
                    contador = aspectos.size-1;

                image.setDrawable(aspectos.get(contador).getTexture());
            }
        });

        // Añade filas a la tabla y añade los componentes
        float pad = Gdx.graphics.getWidth() * 0.025f;
        table.row();
        table.add(bIzquierda).width(Gdx.graphics.getWidth()*0.1f).height(Gdx.graphics.getWidth()*0.1f).pad(pad).padTop(pad*20);
        table.add(image).width(Gdx.graphics.getWidth()*0.5f).height(Gdx.graphics.getWidth()*0.5f).pad(pad).padTop(pad*20);
        table.add(bDerecha).width(Gdx.graphics.getWidth()*0.1f).height(Gdx.graphics.getWidth()*0.1f).pad(pad).padTop(pad*20);
        table.row().colspan(3);
        table.add(bAceptar).width(Gdx.graphics.getWidth()*0.5f).height(Gdx.graphics.getWidth()*0.2f).pad(pad).padTop(pad*5);
        table.row().colspan(3);
        table.add(bAtras).width(Gdx.graphics.getWidth()*0.5f).height(Gdx.graphics.getWidth()*0.2f).pad(pad);
        Gdx.input.setInputProcessor(stage);

        trofeos = Aplication.db.getTrofeos();
    }
    int trofeos;

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batchHUD.begin();

        float widthMoneda = Gdx.graphics.getWidth()*0.06f;

        batchHUD.draw(textureMoneda, Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.06f, widthMoneda, widthMoneda);
        fontContMonedas.draw(batchHUD, "" + trofeos, Gdx.graphics.getWidth() * 0.07f + widthMoneda,  Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.035f );

        batchHUD.draw(textureMoneda, Gdx.graphics.getWidth()/2 - widthMoneda*2, image.getY() - Gdx.graphics.getHeight()*0.045f, widthMoneda, widthMoneda);
        fontPrecio.draw(batchHUD, "" + aspectos.get(contador).precio , Gdx.graphics.getWidth()/2 ,  image.getY() - Gdx.graphics.getHeight()*0.025f);


        batchHUD.end();

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width, height);
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
        // Libera los recursos de la escena
        stage.dispose();
    }
}