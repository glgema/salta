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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ggl.salta.clases.Aspecto;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ggl.salta.MainScreen.font;
import static com.ggl.salta.MainScreen.font2;

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

    TextButton.TextButtonStyle textButtonStyle;

    TextButton bAceptar;


    int size;

    public void cargarArray(){
        aspectos = new Array<>();
        aspectos.add(new Aspecto(0,Aplication.db.estaDesbloqueado(0), new Texture("elegirPelotas/pj1.png"), new Texture("elegirPelotas/pj1.png")));
        aspectos.add(new Aspecto(50,Aplication.db.estaDesbloqueado(1),new Texture("elegirPelotas/pj2b.png"), new Texture("elegirPelotas/pj2.png")));
        aspectos.add(new Aspecto(100,Aplication.db.estaDesbloqueado(2), new Texture("elegirPelotas/pj3b.png"), new Texture("elegirPelotas/pj3.png")));
        aspectos.add(new Aspecto(150,Aplication.db.estaDesbloqueado(3), new Texture("elegirPelotas/pj4b.png"), new Texture("elegirPelotas/pj4.png")));
        aspectos.add(new Aspecto(200,Aplication.db.estaDesbloqueado(4), new Texture("elegirPelotas/pj5b.png"), new Texture("elegirPelotas/pj5.png")));

    }
    @Override
    public void show() {

        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();

        cargarArray();

        contador = Aplication.db.getSelection();
        if(aspectos.get(contador).desbloqueado)
            image = new VisImage(aspectos.get(contador).textureD);
        else
            image = new VisImage(aspectos.get(contador).textureB);

        textureMoneda = new Texture(Gdx.files.internal("trofeo.png"),true);
        textureMoneda.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        VisTable table = new VisTable(false);
        table.setFillParent(true);
        stage.addActor(table);

        int border = (int)(Gdx.graphics.getHeight() * 0.003f);
        int size = (int)(Gdx.graphics.getHeight() * 0.025f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        parameter.borderWidth = border;
        fontContMonedas = generator.generateFont(parameter);
        fontPrecio = generator.generateFont(parameter);
        batchHUD = new SpriteBatch();

        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));


        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = MainScreen.font;


        bAceptar = new TextButton("", textButtonStyle);
            bAceptar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(Aplication.db.estaDesbloqueado(contador)) {
                    Aplication.db.setSelection(contador);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                    dispose();
                    VisUI.dispose();
                }else if(Aplication.db.getTrofeos() >= aspectos.get(contador).precio) {
                    Aplication.db.desbloquear(contador);
                    Aplication.db.setTrofeos(Aplication.db.getTrofeos() - aspectos.get(contador).precio);
                    trofeos = Aplication.db.getTrofeos();
                    cargarArray();
                    image.setDrawable(aspectos.get(contador).textureD);
                }
            }
        });

        TextButton bAtras = new TextButton("Atras",textButtonStyle);
        bAtras.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen(game));
                dispose();
                VisUI.dispose();
            }
        });

        TextButton bDerecha = new TextButton(">",textButtonStyle);
        bDerecha.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               contador++;
               if(contador >= aspectos.size)
                   contador = 0;

                if(aspectos.get(contador).desbloqueado)
                    image.setDrawable(aspectos.get(contador).textureD);
                else
                    image.setDrawable(aspectos.get(contador).textureB);
            }
        });

        TextButton bIzquierda = new TextButton("<",textButtonStyle);
        bIzquierda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contador--;
                if(contador <= -1)
                    contador = aspectos.size-1;

                System.out.println(contador+" "+aspectos.get(contador).desbloqueado);
                if(aspectos.get(contador).desbloqueado)
                    image.setDrawable(aspectos.get(contador).textureD);
                else
                    image.setDrawable(aspectos.get(contador).textureB);
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batchHUD.begin();

        float widthMoneda = Gdx.graphics.getWidth()*0.06f;

        batchHUD.draw(textureMoneda, Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.06f, widthMoneda, widthMoneda);
        fontContMonedas.draw(batchHUD, "" + trofeos, Gdx.graphics.getWidth() * 0.07f + widthMoneda,  Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*0.035f );

        if(!Aplication.db.estaDesbloqueado(contador)) {
            batchHUD.draw(textureMoneda, Gdx.graphics.getWidth() / 2 - widthMoneda * 2, image.getY() - Gdx.graphics.getHeight() * 0.045f, widthMoneda, widthMoneda);
            fontPrecio.draw(batchHUD, "" + aspectos.get(contador).precio, Gdx.graphics.getWidth() / 2, image.getY() - Gdx.graphics.getHeight() * 0.025f);
        }

        batchHUD.end();

        stage.act(dt);
        stage.draw();


        if(Aplication.db.estaDesbloqueado(contador)) {
            bAceptar.setText("ACEPTAR");
            textButtonStyle.font = font;
            bAceptar.setStyle(textButtonStyle);
        }else {
            bAceptar.setText("DESBLOQUEAR");
            textButtonStyle.font = font2;
            bAceptar.setStyle(textButtonStyle);
        }
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }
}