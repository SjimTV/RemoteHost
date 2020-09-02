package com.sjimtv.mediaplayer;

import com.sjimtv.App;
import com.sjimtv.showStructure.Episode;
import uk.co.caprica.vlcj.player.base.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.File;

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

    public void updateStatus(Status status){
        if(status.isNewMedia()){
            playEpisode(status.getShowIndex(), status.getEpisodeNumber());
        }
        setPause(status.isPaused());

        App.mediaStatus.setStatus(status);
    }

    public void playMedia(String path) {
        mediaPlayer.submit(() -> mediaApi.play(path));
    }

    public void playEpisode(Episode episode) {
        playMedia(episode.getPath());
        setSubtitles(episode.getSubtitlePath());
        displayMessage("Now Playing:\n" + episode.getName());
    }

    public void playEpisode(int showIndex, int episode){
        Episode ep = App.shows.get(showIndex).getEpisodes().get(episode);
        playEpisode(ep);
    }

    private void setSubtitles(String subtitlePath){
        File subtitleFile = new File(subtitlePath);
        if (subtitleFile.exists()) {
            if (!mediaPlayer.subpictures().setSubTitleFile(subtitleFile)) {
                System.out.println("Can't load subtitle: " + subtitlePath);
            }
        }
        else System.out.println("Can't find subtitle: " + subtitlePath);
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
