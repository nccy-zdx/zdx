package lab14;

import java.util.ArrayList;

import lab14lib.Generator;
import lab14lib.GeneratorAudioAnimator;
import lab14lib.GeneratorAudioVisualizer;
import lab14lib.GeneratorDrawer;
import lab14lib.GeneratorPlayer;
import lab14lib.MultiGenerator;

public class Main {
	public static void main(String[] args) {
		/** Your code here. */
		Generator g1=new SineWaveGenerator(200);
		Generator g2=new SineWaveGenerator(201);
		ArrayList<Generator> generators=new ArrayList<>();
		generators.add(g1);
		generators.add(g2);
		MultiGenerator mg=new MultiGenerator(generators);
		GeneratorAudioVisualizer gav=new GeneratorAudioVisualizer(mg);
		gav.drawAndPlay(4096, 100);
	}
} 