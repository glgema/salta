package com.ggl.salta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class MainScreen implements Screen {

    // constantes
    int TILE_WIDTH = 64;
    int TILES_IN_CAMERA_WIDTH = 9;
    int TILES_IN_CAMERA_HEIGHT = 19;
    public static float PPM = 100;

    Texture texturePersonaje;
    Texture texturePlataforma;
    Personaje personaje;
    Array<Plataforma> plataformas;

    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;


    // CAMERA
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public MainScreen(){

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
                plataformas.add(new Plataforma(new Vector2(MathUtils.random(0,camera.viewportWidth - texturePlataforma.getWidth() / PPM),personaje.b2body.getPosition().y + 3 * i),texturePlataforma));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        actualizar();
        pintar();
    }

    public void pintar(){
        handleCamera();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render();
        b2dr.render(world,camera.combined);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        personaje.draw(batch);

        for (Plataforma plataforma : plataformas)
            plataforma.draw(batch);

        batch.end();

    }

    private void handleCamera() {

        ///
        if(personaje.b2body.getPosition().y > camera.position.y + camera.viewportHeight / 2000)
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
        System.out.println(" Gravedad Vertical: "+vg);
        personaje.b2body.setLinearVelocity(new Vector2(-vg,personaje.b2body.getLinearVelocity().y));


        //
        for (Plataforma plataforma : plataformas){
            if(plataforma.rect.contains(new Vector2(personaje.b2body.getPosition().x,personaje.b2body.getPosition().y - 15 / PPM)) && personaje.b2body.getLinearVelocity().y <= 0)
                personaje.saltar();
            if(plataforma.getY() <= (camera.position.y - camera.viewportHeight/2))
                plataformas.removeValue(plataforma,false);
        }

        //
        generarPlataformas();
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
