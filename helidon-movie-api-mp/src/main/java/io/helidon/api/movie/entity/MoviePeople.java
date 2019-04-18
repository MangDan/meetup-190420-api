package io.helidon.api.movie.entity;

/**
 * @author Dan
 */
public class MoviePeople{

    private int id;
    private String name;
    private String role;
    private String filmography;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MoviePeople id(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MoviePeople name(String name) {
        this.name = name;
        return this;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MoviePeople role(String role) {
        this.role = role;
        return this;
    }

    public String getFilmography() {
        return this.filmography;
    }

    public void setFilmography(String filmography) {
        this.filmography = filmography;
    }

    public MoviePeople filmography(String filmography) {
        this.filmography = filmography;
        return this;
    }
}
