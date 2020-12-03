package com.ggl.salta.clases;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class Aspecto extends Sprite {

    public int precio;
    public boolean desbloqueado;
    public Texture textureB;
    public Texture textureD;

    public Aspecto(int precio, boolean desbloqueado, Texture textureB, Texture textureD) {
        this.precio = precio;
        this.desbloqueado = desbloqueado;

        this.textureB = textureB;
        this.textureD = textureD;
    }
}
