package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.Random;

import static java.lang.Math.round;

public class MainScreen implements Screen {
    private Stage stage;
    private Aplication game;


    // constantes
    int TILE_WIDTH = 64;
    int TILES_IN_CAMERA_WIDTH = 9;
    int TILES_IN_CAMERA_HEIGHT = 19;
    public static float PPM = 100;

    static final float STEP_TIME = 1f / 60f;

    static final int VELOCITY_ITERATIONS = 6;

    static final int POSITION_ITERATIONS = 2;

    Array<Texture> personajes = new Array<>();

    SpriteBatch batch;

    OrthographicCamera camera;

    World world;

    Box2DDebugRenderer debugRenderer;

    float accumulator = 0;

    Body ground;
    Body paredIzq;
    Body paredDer;
    Body techo;

    Array<Body> fruitBodies = new Array<>();

    Array<Sprite> fruitSprites = new Array<>();


    public MainScreen(Aplication game) {
        this.game = game;
    }


    private void generateFruit() {
        for (int i = 0; i < personajes.size; i++) {

            Sprite sprite = new Sprite(personajes.get(i));
            sprite.setSize(sprite.getWidth() / PPM , sprite.getHeight() /PPM);
            fruitSprites.add(sprite);

            fruitBodies.add(createBody( 0));
        }
    }

    private Body createBody( float rotation) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / PPM, 0 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1;

        CircleShape shape = new CircleShape();
        shape.setRadius((personajes.get(0).getWidth()/2) / PPM);

        fdef.shape = shape;

        body.createFixture(fdef).setUserData(this);

        body.setTransform(50/PPM,50 /PPM, rotation);

        return body;
    }
    @Override
    public void show() {
        Box2D.init();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();

        camera.setToOrtho(false, (TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM, ((TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM)  * (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()) );
        camera.update();


        personajes.add( new Texture("pelotas/pj1.png"));
        personajes.add( new Texture("pelotas/pj2.png"));
        personajes.add( new Texture("pelotas/pj3.png"));
        personajes.add( new Texture("pelotas/pj4.png"));
        personajes.add( new Texture("pelotas/pj5.png"));

        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer();

        generateFruit();

        Skin skin = new Skin(Gdx.files.internal("skins/neonui/neon-ui.json"));

        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(false);
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playButton = new TextButton("JUGAR",skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ChoosePjScreen());
                dispose();
            }
        });

        TextButton quitButton = new TextButton("AJUSTES",skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
                dispose();
                VisUI.dispose();
                // Salir del juego
            }
        });

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(playButton).center().width(600).height(200).pad(5);
        table.row();
        table.add(quitButton).center().width(600).height(150).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Step the physics world.
        world.step(1/60f,6,2);

        debugRenderer.render(world,camera.combined);

        // open the sprite batch buffer for drawing
        batch.begin();

        // iterate through each of the fruits
        for (int i = 0; i < fruitBodies.size; i++) {

            // get the physics body of the fruit
            Body body = fruitBodies.get(i);

            // get the position of the fruit from Box2D
            Vector2 position = body.getPosition();

            // get the degrees of rotation by converting from radians
            float degrees = (float) Math.toDegrees(body.getAngle());

            // draw the fruit on the screen
            drawSprite(fruitSprites.get(i), position.x - fruitSprites.get(i).getWidth()/2, position.y - fruitSprites.get(i).getWidth()/2, degrees);
        }

        // close the buffer - this is what actually draws the sprites
        batch.end();


        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();


        // gravedad
        int vg=round( Gdx.input.getAccelerometerX());
        int vh=round( Gdx.input.getAccelerometerY());
        System.out.println(vg);
        world.setGravity(new Vector2(-vg,-vh));

    }
    private void drawSprite(Sprite sprite, float x, float y, float degrees) {
        sprite.setPosition(x, y);

        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width, height);

        batch.setProjectionMatrix(camera.combined);

        createGround();
    }

    private void createGround() {
        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(camera.viewportWidth, 1 / PPM);

        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        ground.setTransform(10 / PPM, 10 / PPM, 0);

        // techo

        techo = world.createBody(bodyDef);
        techo.createFixture(fixtureDef);

        techo.setTransform( 10 / PPM, camera.viewportHeight -1 / PPM, 0);

        // pared izq

        shape.setAsBox(1 / PPM, camera.viewportHeight);

        fixtureDef.shape = shape;
        paredIzq = world.createBody(bodyDef);
        paredIzq.createFixture(fixtureDef);

        paredIzq.setTransform(10 / PPM, 10 / PPM, 0);

        // pared der

        fixtureDef.shape = shape;
        paredDer = world.createBody(bodyDef);
        paredDer.createFixture(fixtureDef);

        paredDer.setTransform(camera.viewportWidth - 1 / PPM, 10 / PPM, 0);

        shape.dispose();
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

        world.dispose();

        debugRenderer.dispose();
    }
}