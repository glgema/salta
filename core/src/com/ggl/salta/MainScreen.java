package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
<<<<<<< HEAD
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
=======
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
<<<<<<< HEAD
=======
import com.badlogic.gdx.utils.viewport.ExtendViewport;
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

<<<<<<< HEAD
import static java.lang.Math.floor;
=======
import java.util.Random;

>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
import static java.lang.Math.round;

public class MainScreen implements Screen {
    private Stage stage;
    private Aplication game;

<<<<<<< HEAD
    public static float PPM = 100;

    Array<Texture> pelotas = new Array<>();
=======

    // constantes
    int TILE_WIDTH = 64;
    int TILES_IN_CAMERA_WIDTH = 9;
    int TILES_IN_CAMERA_HEIGHT = 19;
    public static float PPM = 100;

    static final float STEP_TIME = 1f / 60f;

    static final int VELOCITY_ITERATIONS = 6;

    static final int POSITION_ITERATIONS = 2;

    Array<Texture> personajes = new Array<>();
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

    SpriteBatch batch;

    OrthographicCamera camera;

    World world;

    Box2DDebugRenderer debugRenderer;

<<<<<<< HEAD
=======
    float accumulator = 0;

>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    Body ground;
    Body paredIzq;
    Body paredDer;
    Body techo;

<<<<<<< HEAD
    Array<Body> pelotasBodies = new Array<>();

    Array<Sprite> pelotasSprites = new Array<>();

    TextButton.TextButtonStyle textButtonStyle;


    // font
    public static BitmapFont font;
    public static BitmapFont font2;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
=======
    Array<Body> fruitBodies = new Array<>();

    Array<Sprite> fruitSprites = new Array<>();
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c


    public MainScreen(Aplication game) {
        this.game = game;
    }


<<<<<<< HEAD
    private void generatePelotas() {
        for (int i = 0; i < pelotas.size; i++) {

            Sprite sprite = new Sprite(pelotas.get(i));
            if (i == 2 || i == 4)
                sprite.setSize(sprite.getWidth() / PPM , sprite.getHeight() /PPM);
            else
                sprite.setSize(sprite.getWidth()*2 / PPM , sprite.getHeight()*2 /PPM);

            pelotasSprites.add(sprite);

            pelotasBodies.add(createBody( 0,i));
        }
    }

    private Body createBody( float rotation, int i) {
=======
    private void generateFruit() {
        for (int i = 0; i < personajes.size; i++) {

            Sprite sprite = new Sprite(personajes.get(i));
            sprite.setSize(sprite.getWidth() / PPM , sprite.getHeight() /PPM);
            fruitSprites.add(sprite);

            fruitBodies.add(createBody( 0));
        }
    }

    private Body createBody( float rotation) {
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / PPM, 0 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 1;
<<<<<<< HEAD
        fdef.density = 0.1f;

        CircleShape shape = new CircleShape();
        if (i == 2 || i == 4)
            shape.setRadius((pelotas.get(0).getWidth()/2) / PPM);
        else
            shape.setRadius((pelotas.get(0).getWidth()) / PPM);

        fdef.shape = shape;

        body.setSleepingAllowed(false);
        body.setAwake(true);
        body.setActive(true);


=======

        CircleShape shape = new CircleShape();
        shape.setRadius((personajes.get(0).getWidth()/2) / PPM);

        fdef.shape = shape;

>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        body.createFixture(fdef).setUserData(this);

        body.setTransform(50/PPM,50 /PPM, rotation);

        return body;
    }
    @Override
    public void show() {
        Box2D.init();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();

<<<<<<< HEAD
        camera.setToOrtho(false, Gdx.graphics.getWidth() / PPM, Gdx.graphics.getHeight() / PPM );
        camera.update();

        pelotas.add( new Texture("pelotas/pj1.png"));
        pelotas.add( new Texture("pelotas/pj2.png"));
        pelotas.add( new Texture("pelotas/pj3.png"));
        pelotas.add( new Texture("pelotas/pj4.png"));
        pelotas.add( new Texture("pelotas/pj5.png"));
=======
        camera.setToOrtho(false, (TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM, ((TILES_IN_CAMERA_WIDTH * TILE_WIDTH) / PPM)  * (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()) );
        camera.update();


        personajes.add( new Texture("pelotas/pj1.png"));
        personajes.add( new Texture("pelotas/pj2.png"));
        personajes.add( new Texture("pelotas/pj3.png"));
        personajes.add( new Texture("pelotas/pj4.png"));
        personajes.add( new Texture("pelotas/pj5.png"));
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer();

<<<<<<< HEAD
        generatePelotas();


        // FONT
        int size = (int)(Gdx.graphics.getHeight() * 0.05f);

        parameter.size = size;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);

        size = (int)(Gdx.graphics.getHeight() * 0.035f);
        parameter.size = size;
        font2 = generator.generateFont(parameter);

        Skin skin = new Skin(Gdx.files.internal("skins/flatearthui/flat-earth-ui.json"));

        textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        textButtonStyle.font = font;


        float btWidth = Gdx.graphics.getWidth()*0.7f;
        float btHeight = Gdx.graphics.getHeight()*0.10f;
        float btPad = Gdx.graphics.getWidth()*0.02f;
=======
        generateFruit();

        Skin skin = new Skin(Gdx.files.internal("skins/neonui/neon-ui.json"));
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();

        VisImage image = new VisImage(new Texture("trofeo_salta.png"));
        stage.addActor(image);

        VisTable table = new VisTable(false);
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playButton = new TextButton("JUGAR",textButtonStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ChoosePjScreen());
                dispose();
            }
        });

        TextButton settings = new TextButton("AJUSTES",textButtonStyle);
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PreferencesScreen(game));
                dispose();
                // Salir del juego
            }
        });

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(image).center().width(btWidth).height(btWidth).padBottom(btPad*6);
        table.row();
        table.add(playButton).center().width(btWidth).height(btHeight).pad(btPad);
        table.row();
        table.add(settings).center().width(btWidth).height(btHeight).pad(btPad);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Step the physics world.
        world.step(1/60f,6,2);

        debugRenderer.render(world,camera.combined);

        // open the sprite batch buffer for drawing
        batch.begin();

        // iterate through each of the fruits
<<<<<<< HEAD
        for (int i = 0; i < pelotasBodies.size; i++) {

            // get the physics body of the fruit
            Body body = pelotasBodies.get(i);
=======
        for (int i = 0; i < fruitBodies.size; i++) {

            // get the physics body of the fruit
            Body body = fruitBodies.get(i);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

            // get the position of the fruit from Box2D
            Vector2 position = body.getPosition();

            // get the degrees of rotation by converting from radians
            float degrees = (float) Math.toDegrees(body.getAngle());

            // draw the fruit on the screen
<<<<<<< HEAD
            drawSprite(pelotasSprites.get(i), position.x - pelotasSprites.get(i).getWidth()/2, position.y - pelotasSprites.get(i).getWidth()/2, degrees);
        }

=======
            drawSprite(fruitSprites.get(i), position.x - fruitSprites.get(i).getWidth()/2, position.y - fruitSprites.get(i).getWidth()/2, degrees);
        }

        // close the buffer - this is what actually draws the sprites
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
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

<<<<<<< HEAD
        sprite.setOrigin( sprite.getWidth()/2,  sprite.getHeight()/2);

=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
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

<<<<<<< HEAD
        ground.setTransform(0 / PPM, -1 / PPM, 0);
=======
        ground.setTransform(10 / PPM, 10 / PPM, 0);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

        // techo

        techo = world.createBody(bodyDef);
        techo.createFixture(fixtureDef);

<<<<<<< HEAD
        techo.setTransform( 0 / PPM, camera.viewportHeight +1/PPM, 0);
=======
        techo.setTransform( 10 / PPM, camera.viewportHeight -1 / PPM, 0);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

        // pared izq

        shape.setAsBox(1 / PPM, camera.viewportHeight);

        fixtureDef.shape = shape;
        paredIzq = world.createBody(bodyDef);
        paredIzq.createFixture(fixtureDef);

<<<<<<< HEAD
        paredIzq.setTransform(-1 / PPM, 0 / PPM, 0);
=======
        paredIzq.setTransform(10 / PPM, 10 / PPM, 0);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

        // pared der

        fixtureDef.shape = shape;
        paredDer = world.createBody(bodyDef);
        paredDer.createFixture(fixtureDef);

<<<<<<< HEAD
        paredDer.setTransform(camera.viewportWidth , 0 / PPM, 0);
=======
        paredDer.setTransform(camera.viewportWidth - 1 / PPM, 10 / PPM, 0);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c

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