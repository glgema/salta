package com.ggl.salta.clases;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.ggl.salta.GameScreen.PPM;

public class Enemigo extends Sprite {

    public Rectangle rect;
    private int dir;

    public Enemigo(Vector2 pos, Texture texture, int dir) {
        super(texture);
        this.rect = new Rectangle(pos.x, pos.y, texture.getWidth() / PPM, texture.getHeight() / PPM);
        super.setRegion(new TextureRegion(getTexture(), 0, 0, getTexture().getWidth(), getTexture().getHeight()));
        setBounds(0, 0, getTexture().getWidth() / PPM, getTexture().getHeight() / PPM);
        super.setPosition(pos.x, pos.y);
        this.dir = dir;
    }

    public void movimiento(float delta){

        float mov = Gdx.graphics.getWidth() * 0.002f * delta;

        if(dir == 1){
            rect.setPosition(new Vector2(rect.x + mov , rect.y ));
            setPosition(rect.x, rect.y);
        }else {
            rect.setPosition(new Vector2(rect.x - mov, rect.y));
            setPosition(rect.x, rect.y);
        }
    }
}
