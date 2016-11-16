package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/1.
 */

public class history extends BmobObject {
    private String Name;
    private String ImageUrl;
    private String Userid;
    private String MovieId;
    private String Movie_1080;
    private String Movie_720;
    private String Plot;
    private String Info;
    private String type;
    private String Score;
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getMovie_1080() {
        return Movie_1080;
    }

    public void setMovie_1080(String movie_1080) {
        Movie_1080 = movie_1080;
    }

    public String getMovie_720() {
        return Movie_720;
    }

    public void setMovie_720(String movie_720) {
        Movie_720 = movie_720;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getMovieId() {
        return MovieId;
    }

    public void setMovieId(String movieId) {
        MovieId = movieId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
