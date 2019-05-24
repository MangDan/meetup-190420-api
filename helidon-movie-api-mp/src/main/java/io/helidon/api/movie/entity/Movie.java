package io.helidon.api.movie.entity;

/**
 * @author Dan
 */
public class Movie{

    private int id;
    private String title;
    private int vote_count;
    private int vote_average;
    private String poster_path;
    private String release_date;
    private String overview;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie id(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public int getVote_count() {
        return this.vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Movie vote_count(int vote_count) {
        this.vote_count = vote_count;
        return this;
    }

    public int getVote_average() {
        return this.vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public Movie vote_average(int vote_average) {
        this.vote_average = vote_average;
        return this;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Movie poster_path(String poster_path) {
        this.poster_path = poster_path;
        return this;
    }

    public String getRelease_date() {
        return this.release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Movie release_date(String release_date) {
        this.release_date = release_date;
        return this;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Movie overview(String overview) {
        this.overview = overview;
        return this;
    }
}
