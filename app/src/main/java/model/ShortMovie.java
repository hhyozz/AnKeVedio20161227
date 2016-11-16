package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/31.
 */

public class ShortMovie extends BmobObject {
    private String content;
    private String moviename;
    private String movie_720;
    private String ImageUrl;
    private String plot;
    private String keyword;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
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

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
