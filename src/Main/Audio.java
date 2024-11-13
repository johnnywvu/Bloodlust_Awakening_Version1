package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Audio {

    Clip clip;
    URL soundURL[] = new URL[30];

    public Audio(){
        soundURL[0] = getClass().getResource("/audio/music/EntertheDungeon.wav");
        soundURL[1] = getClass().getResource("/audio/sfx/pickup.wav");
        soundURL[2] = getClass().getResource("/audio/sfx/coin_pickup.wav");
        soundURL[3] = getClass().getResource("/audio/sfx/unable.wav");
        soundURL[4] = getClass().getResource("/audio/sfx/buff-up.wav");
        soundURL[5] = getClass().getResource("/audio/sfx/speech.wav");
        soundURL[6] = getClass().getResource("/audio/sfx/tutorialCompleted.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception e){

        }
    }
    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
