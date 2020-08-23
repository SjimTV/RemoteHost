package com.sjimtv.mediaplayer;

import com.sjimtv.showStructure.Episode;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaController {
    private final EmbeddedMediaPlayer mediaPlayer;
    private final MediaApi mediaApi;
    private final ControlsApi controlApi;
    private final MarqueeApi marqueeApi;
    private final AudioApi audioApi;

    public MediaController(EmbeddedMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        mediaApi = mediaPlayer.media();
        controlApi = mediaPlayer.controls();
        marqueeApi = mediaPlayer.marquee();
        audioApi = mediaPlayer.audio();
    }

    public void playMedia(String path) {
        mediaPlayer.submit(() -> mediaApi.play(path));
    }

    public void playEpisode(Episode episode) {
        playMedia(episode.getPath());
    }

    public void pause() {
        controlApi.pause();
    }

    public void setPause(boolean isPause) {
        controlApi.setPause(isPause);
    }

    public void stop() {
        controlApi.stop();
    }

    public void setRepeat(boolean isRepeat) {
        controlApi.setRepeat(isRepeat);
    }

    public void setMute(boolean isMute) {
        audioApi.setMute(isMute);
    }

    public void setVolume(float volume) {
        int volumePercentage = (int) (volume * 100);
        audioApi.setVolume(volumePercentage);
    }

    public void setPosition(float position) {
        controlApi.setPosition(position);
    }

    public void displayMessage(String string) {
        marqueeApi.set(Marquee.marquee()
                .position(MarqueePosition.CENTRE)
                .text(string)
                .size(60)
                .timeout(5000)
                .enable());
    }


}
