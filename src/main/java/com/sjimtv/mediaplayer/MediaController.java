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
        if (isPause) displayMessage("Paused");
        else displayMessage("Play");
    }

    public void setRepeat(boolean isRepeat) {
        controlApi.setRepeat(isRepeat);
    }


    public void setVolume(float volume) {
        int volumePercentage = (int) (volume * 100);
        audioApi.setVolume(volumePercentage);
        displayVolume(volumePercentage);
    }

    public void adjustVolume(int adjustPercentage){
        int adjustedVolume = (int) (App.mediaStatus.getVolume() * 100) + adjustPercentage;
        if (adjustedVolume < 0) adjustedVolume = 0;
        if (adjustedVolume > 100) adjustedVolume = 100;
        System.out.println(adjustedVolume + "");
        audioApi.setVolume(adjustedVolume);
        displayVolume(adjustedVolume);

    }


    public void setPosition(float position) {
        controlApi.setPosition(position);
    }

    public void skipTime(boolean forward){
        float currentPosition = App.mediaStatus.getPosition();
        if (forward) currentPosition += 0.01;
        else currentPosition -= 0.01;

        setPosition(currentPosition);
    }

    public void displayMessage(String string) {
        marqueeApi.set(Marquee.marquee()
                .position(MarqueePosition.CENTRE)
                .text(string)
                .size(60)
                .timeout(5000)
                .enable());
    }

    private void displayVolume(int volume){
        marqueeApi.set(Marquee.marquee()
                .position(MarqueePosition.TOP_RIGHT)
                .text("\r\nVolume: " + volume + "         ")
                .size(80)
                .timeout(1000)
                .enable());
    }


}
