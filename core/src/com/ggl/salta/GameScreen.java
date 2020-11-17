package com.ggl.salta;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ggl.salta.clases.Enemigo;
import com.ggl.salta.clases.Moneda;
import com.ggl.salta.clases.Personaje;
import com.ggl.salta.clases.Plataforma;

import static java.lang.Math.*;

public class GameScreen implements Screen {

    // HUD
    private BitmapFont fontAltura;
    private BitmapFont fontContMonedas;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private SpriteBatch batchHUD;

    // constantes
    int TILE_WIDTH = 64;
    int TILES_IN_CAMERA_WIDTH = 9;
    int TILES_IN_CAMERA_HEIGHT = 19;
    public static float PPM = 100;

    Texture texturePersonaje;
    Texture texturePlataforma;
    Texture texturePlataformaRota;
    Texture textureMoneda;
    Texture textEnemigo;
    Personaje personaje;
    Array<Plataforma> plataformas;
    Array<Moneda> monedas;
    Array<Enemigo> enemigos;
    int altura;
    int contMonedas = 0;

    // FONDO
    Texture fondoTexture;
    Array<Sprite> fondosArray;

    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    // CAMERA
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    int size = (int)(Gdx.graphics.getHeight() * 0.025f);
    int border = (int)(Gdx.graphics.getHeight() * 0.003f);
    public GameScreen(){
        // HUD
        parameter.size = this.size;
        parameter.color = Color.WHITE;
        parameter.borderWidth = this.border;
        fontAltura = generator.generateFont(parameter);
        fontContMonedas = generator.generateFont(parameter);
        batchHUD = new SpriteBatch();

        // fondos
        fondoTexture = new Texture("fondo.png");
        fondosArray = new Array<>();

        camera = new OrthographicCamera();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //camera.setToOrtho(false, (TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM, (TILES_IN_CAMERA_HEIGHT * TILE_WIDTH) / PPM );
        camera.setToOrtho(false, (TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM, ((TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM)  * (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()) );

        camera.update();

        map = new TmxMapLoader().load("endless.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        batch = renderer.getBatch();

        texturePersonaje = new Texture(Gdx.files.internal("pelota.png"),true);
        texturePersonaje.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        texturePlataforma = new Texture(Gdx.files.internal("plataforma.png"),true);
        texturePlataformaRota = new Texture("plataforma2.png");

        textureMoneda = new Texture(Gdx.files.internal("trofeo.png"),true);
        textureMoneda.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        textEnemigo = new Texture(Gdx.files.internal("s.png"),true);

        world = new World(new Vector2(0,-10f),true);
        b2dr = new Box2DDebugRenderer();

        personaje = new Personaje(texturePersonaje,new Vector2(32,32), world);
        plataformas = new Array<>();
        monedas = new Array<>();
        enemigos = new Array<>();
        altura = 0;
    }

    float alturaUltimaPlat;
    public void generarPlataformas(){
        if(plataformas.size < 4)
            for(int i = 0 ; i < 5 ; i++) {
                float platX = MathUtils.random(0, camera.viewportWidth - texturePlataforma.getWidth() / PPM);
                //float platY = alturaUltimaPlat + camera.viewportHeight/3;
                float platY = alturaUltimaPlat + personaje.rect.getHeight()*5;

                int tipoPlat;
                tipoPlat = MathUtils.random(0,4);

                Plataforma plat;
                if(MathUtils.randomBoolean())
                    plat = new Plataforma(new Vector2(platX, platY), texturePlataforma, false, tipoPlat);
                else
                    plat = new Plataforma(new Vector2(platX, platY), texturePlataformaRota, true, tipoPlat);
                plataformas.add(plat);

                alturaUltimaPlat = platY;

                // generar monedas
                if(MathUtils.randomBoolean()){
                    float x = plat.getX() + plat.getWidth()/2 - textureMoneda.getWidth()/ PPM / 4;
                    float y = plat.getY() + plat.getHeight() * 2 ;
                    monedas.add(new Moneda(new Vector2(x,y),textureMoneda ));
                }
                
            }
    }

    int cont = 0;
    float alturaUltimoFondo;
    public void generarFondo(){

        int anchoFondo = Math.round(camera.viewportWidth);

        if(fondosArray.size < 3){
            Sprite sprite = new Sprite(fondoTexture);
            sprite.setRegion(new TextureRegion(fondoTexture,0,0,fondoTexture.getWidth() ,fondoTexture.getHeight()));
            sprite.setBounds(0,0,anchoFondo,anchoFondo);

            sprite.setPosition(camera.position.x - camera.viewportWidth/2,alturaUltimoFondo + anchoFondo);
            fondosArray.add(sprite);

            alturaUltimoFondo = sprite.getY();

            if(++cont == 3)
                cont = 0;
        }
    }

    public void generarEnemigo(){
        Vector2 pos = null;
        int dir = 0;
        if(enemigos.size<1) {
            float randomHeight = MathUtils.random(camera.position.y, camera.position.y + camera.viewportHeight / 2) + personaje.rect.height*2;
            if (MathUtils.randomBoolean()) {
                dir = 1;
                pos = new Vector2(camera.position.x - camera.viewportWidth / 2- textEnemigo.getWidth() / PPM, randomHeight);
            } else
                pos = new Vector2(camera.position.x + camera.viewportWidth / 2, randomHeight);

            enemigos.add(new Enemigo(pos, textEnemigo, dir));
        }
    }

    public void generarFondoInicial(){
        alturaUltimoFondo =  -camera.viewportWidth*2;
    }

    public void generarPlataformasInicio(){
        // todo arreglar esta puta basura

        for(int i = 1 ; i < 3 ; i++)
            plataformas.add(new Plataforma(new Vector2(MathUtils.random(0, camera.viewportWidth - texturePlataforma.getWidth() / PPM), (personaje.b2body.getPosition().y) + 0.9f * i), texturePlataforma , false, 0));


        for(int i = 1 ; i < 6 ; i++) {
            plataformas.add(new Plataforma(new Vector2(MathUtils.random(0, camera.viewportWidth - texturePlataforma.getWidth() / PPM), (personaje.b2body.getPosition().y) + 3f * i), texturePlataforma, false,0));
            alturaUltimaPlat = (personaje.b2body.getPosition().y) + 3f * i;
        }
    }

    @Override
    public void show() {
        generarPlataformasInicio();
        generarFondoInicial();
    }

    @Override
    public void render(float delta) {
        actualizar(delta);
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

        for (Sprite fondo : fondosArray)
            fondo.draw(batch);

        personaje.draw(batch);

        for (Plataforma plataforma : plataformas)
            plataforma.draw(batch);

        for (Moneda moneda : monedas)
            moneda.draw(batch);

        for(Enemigo enemigo : enemigos)
            enemigo.draw(batch);

        batch.end();

        batchHUD.begin();

        // HUD
        fontAltura.draw(batchHUD, altura+" m",x , y);
        batchHUD.draw(textureMoneda, 50, Gdx.graphics.getHeight() - 200);
        fontContMonedas.draw(batchHUD, "" + contMonedas, 150,  Gdx.graphics.getHeight() - 150);

        batchHUD.end();
    }
    float x = Gdx.graphics.getWidth() * 0.05f;
    float y = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() * 0.025f;

    private void handleCamera() {

        //
        if(personaje.b2body.getPosition().y > camera.position.y)
            camera.position.set(new Vector2(camera.position.x,personaje.b2body.getPosition().y ),0);

        ///caida
        /*
        if(personaje.b2body.getPosition().y < camera.position.y - camera.viewportHeight/2)
            camera.position.y -= 5/PPM;
*/
        camera.update();
        renderer.setView(camera);

        altura = Math.round(camera.position.y * 10);
    }


    public Vector3 getTouchPosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void actualizar(float delta){
        b2dr.render(world, camera.combined);
        world.step(1/60f,6,2);

        // Alinea el sprite del pj al body de box2d
        personaje.setPosition(personaje.b2body.getPosition().x - personaje.getWidth()/2, personaje.b2body.getPosition().y - personaje.getHeight()/2);
        personaje.rect.setPosition(personaje.getX(),personaje.getY());

        //TIRAR ENEMIGO
        if(Gdx.input.isTouched()) {
            Vector2 touch = new Vector2(getTouchPosInGameWorld().x, getTouchPosInGameWorld().y);
            for (Enemigo enemigo : enemigos)
                if (enemigo.rect.contains(touch))
                    enemigos.removeValue(enemigo, false);
        }

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
        for (Plataforma plataforma : plataformas) {
            if (plataforma.rect.contains(new Vector2(personaje.b2body.getPosition().x, personaje.b2body.getPosition().y - 35 / PPM)) && personaje.b2body.getLinearVelocity().y <= 0){
                personaje.saltar();
                if(plataforma.rectPowerups!= null ) {
                    if (personaje.rect.overlaps(plataforma.rectPowerups)) {
                        personaje.cogerPorwerups(plataforma.tipo);
                    }
                }
                if (plataforma.seBorra())
                plataformas.removeValue(plataforma, false);
            }
            if(plataforma.getY() <= (camera.position.y - camera.viewportHeight/2))
                plataformas.removeValue(plataforma,false);
        }

        for(Enemigo enemigo : enemigos){
            enemigo.movimiento(delta);
            if(enemigo.getY() <= (camera.position.y - camera.viewportHeight/2))
                enemigos.removeValue(enemigo, false);
        }

        for (Moneda moneda : monedas){
            if(moneda.rect.overlaps(personaje.rect)) {
                monedas.removeValue(moneda, false);
                contMonedas = contMonedas + 1;
            }
            if(moneda.getY() <= (camera.position.y - camera.viewportHeight/2))
                monedas.removeValue(moneda,false);
        }

        for(Sprite fondo : fondosArray)
            if(fondo.getY() + fondo.getTexture().getWidth()/ PPM <= (camera.position.y - camera.viewportHeight/2))
                fondosArray.removeValue(fondo,false);

        //
        generarPlataformas();
        generarFondo();
        generarEnemigo();
    }

    @Override
    public void resize(int width, int height){
        //camera.viewportWidth = width/ (bestI*bestJ);
        camera.viewportHeight = camera.viewportWidth * height/width;
        camera.position.set(new Vector2(camera.viewportWidth/2f , camera.viewportHeight/2f),0);
        camera.update();
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

