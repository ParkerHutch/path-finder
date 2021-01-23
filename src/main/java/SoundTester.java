import java.io.File;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.media.AudioClip;

public class SoundTester {

	public static void main(String[] args) throws Exception {
		

		AudioClip plonkSound = new AudioClip(Paths.get("beep1.mp3").toUri().toString());
		plonkSound.play();
		Thread.sleep(1000); // might not need this in actual program
		System.out.println("here");

		/*
		File soundFile = new File("beep1.wav");
		Clip clip = AudioSystem.getClip();
		AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
		clip.open(inputStream);
		System.out.println("here");
		while (true) {
				clip.stop();
				clip.setFramePosition(0);
				clip.start();
		}*/
	    

	    
		/*
		FileInputStream in = null;

		try {
			
			in = new FileInputStream("test.txt");
			
			//System.out.println(in.read);
			
		} finally {
			if (in != null) {
				in.close();
			}
		}
		*/
	}
}
