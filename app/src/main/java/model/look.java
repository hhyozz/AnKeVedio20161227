package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/31.
 */

public class look extends BmobObject {
    private String Userid;
    private String moviename;
    private String tvId;
    private String plot;
    private String movie_1080;
    private String movie_720;
    private String ImageUrl;
    private String MovieInfo;
    private String score;
    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getMovie_1080() {
        return movie_1080;
    }

    public void setMovie_1080(String movie_1080) {
        this.movie_1080 = movie_1080;
    }

    public String getMovie_720() {
        return movie_720;
    }

    public void setMovie_720(String movie_720) {
        this.movie_720 = movie_720;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getMovieInfo() {
        return MovieInfo;
    }

    public void setMovieInfo(String movieInfo) {
        MovieInfo = movieInfo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getTvId() {
        return tvId;
    }

    public void setTvId(String tvId) {
        this.tvId = tvId;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }
}
