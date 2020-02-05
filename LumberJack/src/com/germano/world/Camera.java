package com.germano.world;

import java.awt.image.BufferedImage;

public class Camera {

	/************ Atributos ************/

	public static int x;
	public static int y;
	 
	/************************************/ 

	/************ Lógica ************/

	// Evita que a camera olhe fora do mapa
	public static int clamp(int Atual, int Min, int Max) {
		if (Atual < Min) {
			Atual = Min;
		} if (Atual > Max) {
			Atual = Max;
		}
		return Atual;
	}
	
	/************************************/ 

}
