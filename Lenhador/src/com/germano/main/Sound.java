package com.germano.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	/************ Atributos ************/

	private AudioClip clip;
	public static final Sound musicBackground = new Sound("/Background.wav");
	public static final Sound woodHurt = new Sound("/WoodHurt.wav");
	public static final Sound playerHurt = new Sound("/PlayerHurt.wav");

	/***********************************/

	/************ Lógica ************/

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {

		}
	}

	// Executa o som uma vez
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {

		}
	}

	// Executa o som repetidamente
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch (Throwable e) {

		}
	}

	/***********************************/

}
