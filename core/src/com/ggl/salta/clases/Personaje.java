package com.ggl.salta.clases;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.ggl.salta.MainScreen.PPM;

public class Personaje extends Sprite {

    public Vector2 posicion;

    // Box2d
    public World world;
    public Body b2body;

    public Personaje(Texture texture, Vector2 posicion, World world) {
        super(texture);
        super.setRegion(new TextureRegion(getTexture(),0,0,getTexture().getWidth(),getTexture().getHeight()));
        setBounds(0,0,getTexture().getWidth() / PPM,getTexture().getHeight() / PPM);
        this.posicion = posicion;

        this.world = world;

        definePersonaje();
    }


    public void definePersonaje(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(posicion.x / PPM, posicion.y / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((super.getTexture().getWidth()/2) / PPM, (super.getTexture().getHeight()/2) / PPM);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void saltar(){
        b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 8.5f));
        //b2body.applyLinearImpulse(new Vector2(0,9f), b2body.getWorldCenter(), true);
    }
}
