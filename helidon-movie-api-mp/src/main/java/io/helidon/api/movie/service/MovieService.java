package io.helidon.api.movie.service;

import java.util.List;

import io.helidon.api.movie.entity.Movie;
import io.helidon.api.movie.entity.MoviePeople;

public interface MovieService {
    public List<Movie> searchMovies(String title);
    public Movie findMovieByid(int id);
    public List<MoviePeople> searchMoviePeople(String name);
    public MoviePeople findMoviePeopleByid(int id);
}