package com.ggl.salta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ggl.salta.clases.Personaje;
import com.ggl.salta.clases.Plataforma;

import static java.lang.Math.round;

public class GameScreen implements Screen {

    // constantes
    int TILE_WIDTH = 64;
    int TILES_IN_CAMERA_WIDTH = 9;
    int TILES_IN_CAMERA_HEIGHT = 15;
    public static float PPM = 100;

    Texture texturePersonaje;
    Texture texturePlataforma;
    Personaje personaje;
    Array<Plataforma> plataformas;


    // FONDO
    Array<Texture> texturasFondos = new Array<>();
    Array<Sprite> fondos = new Array<>();


    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;


    // CAMERA
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public GameScreen(){

        // fondos
        texturasFondos.add(new Texture("fondo1.png"));
        texturasFondos.add(new Texture("fondo2.png"));
        texturasFondos.add(new Texture("fondo3.png"));



        camera = new OrthographicCamera();

        camera.setToOrtho(false, (TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM, (TILES_IN_CAMERA_HEIGHT * TILE_WIDTH) / PPM);
        camera.update();

        map = new TmxMapLoader().load("endless.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        batch = renderer.getBatch();


        texturePersonaje = new Texture("pelota.png");
        texturePlataforma = new Texture("plataforma.png");

        world = new World(new Vector2(0,-10f),true);
        b2dr = new Box2DDebugRenderer();

        personaje = new Personaje(texturePersonaje,new Vector2(32,32), world);
        plataformas = new Array<>();
    }

    public void generarPlataformas(){
        if(plataformas.size < 4)
            for(int i = 0 ; i < 5 ; i++)
                plataformas.add(new Plataforma(new Vector2(MathUtils.random(0,camera.viewportWidth - texturePlataforma.getWidth() / PPM),(camera.position.y + camera.viewportHeight/2) + 3f * i),texturePlataforma));
    }

    int cont = 0;
    float altura;
    public void generarFondo(){


        int anchoFondo = Math.round(texturasFondos.get(cont).getWidth() / PPM);

        if(fondos.size < 3){
            Sprite sprite = new Sprite(texturasFondos.get(cont),0,0,anchoFondo,anchoFondo);

            sprite.setPosition(camera.position.x - camera.viewportWidth/2,altura + anchoFondo);
            fondos.add(sprite);

            altura = sprite.getY();

            if(++cont == 3)
                cont = 0;
        }
    }

    public void generarFondoInicial(){

        int anchoFondo = Math.round(texturasFondos.get(cont).getWidth() / PPM);


        Sprite sprite = new Sprite(texturasFondos.get(cont),0,0,anchoFondo,anchoFondo);

        sprite.setPosition(camera.position.x - camera.viewportWidth/2,camera.position.y - camera.viewportHeight/2);
        fondos.add(sprite);

        altura = sprite.getY();

        if(++cont == 3)
            cont = 0;

    }


    public void generarPlataformasInicio(){
        for(int i = 1 ; i < 3 ; i++)
            plataformas.add(new Plataforma(new Vector2(MathUtils.random(0, camera.viewportWidth - texturePlataforma.getWidth() / PPM), (personaje.b2body.getPosition().y) + 0.9f * i), texturePlataforma));


        for(int i = 1 ; i < 6 ; i++)
            plataformas.add(new Plataforma(new Vector2(MathUtils.random(0, camera.viewportWidth - texturePlataforma.getWidth() / PPM), (personaje.b2body.getPosition().y) + 3f * i), texturePlataforma));
    }

    @Override
    public void show() {
        generarPlataformasInicio();
        generarFondoInicial();
    }

    @Override
    public void render(float delta) {
        actualizar();
        pintar();
    }

    public void pintar(){
        handleCamera();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render();
        b2dr.render(world,camera.combined);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Sprite fondo : fondos)
            fondo.draw(batch);

        personaje.draw(batch);

        for (Plataforma plataforma : plataformas)
            plataforma.draw(batch);

        batch.end();

    }

    private void handleCamera() {

        ///
        if(personaje.b2body.getPosition().y > camera.position.y)
            camera.position.set(new Vector2(camera.position.x,personaje.b2body.getPosition().y ),0);


        camera.update();
        renderer.setView(camera);
    }

    public void actualizar(){
        b2dr.render(world, camera.combined);
        world.step(1/60f,6,2);



        // Alinea el sprite del pj al body de box2d
        personaje.setPosition(personaje.b2body.getPosition().x - personaje.getWidth()/2, personaje.b2body.getPosition().y - personaje.getHeight()/2);

        //
        if (Gdx.input.justTouched())
            personaje.saltar();


        // SUELO
        if(personaje.b2body.getPosition().y <= (camera.position.y - camera.viewportHeight/2))
            personaje.saltar();


        // gravedad segun giro
        int vg=round( Gdx.input.getAccelerometerX());
        System.out.println(" Inclinacion Vertical: "+vg);
        personaje.b2body.setLinearVelocity(new Vector2(-vg,personaje.b2body.getLinearVelocity().y));

        // tp margenes
        if(personaje.b2body.getPosition().x <= camera.position.x - camera.viewportWidth/2)
            personaje.b2body.setTransform((camera.position.x - camera.viewportWidth/2) + camera.viewportWidth ,personaje.b2body.getPosition().y,0);
        else if(personaje.b2body.getPosition().x >= camera.position.x + camera.viewportWidth/2)
            personaje.b2body.setTransform(camera.position.x - camera.viewportWidth/2,personaje.b2body.getPosition().y,0);

        // remove
        for (Plataforma plataforma : plataformas){
            if(plataforma.rect.contains(new Vector2(personaje.b2body.getPosition().x,personaje.b2body.getPosition().y - 35 / PPM)) && personaje.b2body.getLinearVelocity().y <= 0)
                personaje.saltar();
            if(plataforma.getY() <= (camera.position.y - camera.viewportHeight/2))
                plataformas.removeValue(plataforma,false);
        }

        for(Sprite fondo : fondos)
            if(fondo.getY() + fondo.getTexture().getWidth()/ PPM <= (camera.position.y - camera.viewportHeight/2))
                fondos.removeValue(fondo,false);

        //
        generarPlataformas();
        generarFondo();
        System.out.println("TAMAÑO ARRAYY "+plataformas.size);
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
        batch.dispose();
        texturePersonaje.dispose();
    }
}

