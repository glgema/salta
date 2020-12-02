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
        //super(texture);
        this.precio = precio;
        this.desbloqueado = desbloqueado;

        this.textureB = textureB;
        this.textureD = textureD;
    }
<<<<<<< HEAD
=======


>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
}
