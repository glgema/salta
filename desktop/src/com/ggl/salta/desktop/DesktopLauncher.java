package com.ggl.salta.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ggl.salta.Aplication;
import com.ggl.salta.clases.Database;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1280;
		config.width = 720;
<<<<<<< HEAD
		new LwjglApplication(new Aplication(new DatabaseDesktop()),config);
=======
		//new LwjglApplication(new Aplication(new DatabaseDesktop()), "Example", 6, 6,config);
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
	}
}
