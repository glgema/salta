package com.ggl.salta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ggl.salta.clases.Database;

public class Aplication extends Game {

	public static Database db;
	public Aplication(Database db){
		this.db = db;
		db.onCreate();
	}

	@Override
	public void create () {
		((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
