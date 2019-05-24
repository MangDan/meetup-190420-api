package io.helidon.api.movie.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.helidon.api.movie.entity.Movie;
import io.helidon.api.movie.entity.MoviePeople;
import io.helidon.api.movie.repository.MoviePeopleRepository;
import io.helidon.api.movie.repository.MovieRepository;

@ApplicationScoped
public class MovieServiceImpl implements MovieService {

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private MoviePeopleRepository moviePeopleRepository;

    @Override
    public List<Movie> searchMovies(String title) {
        return movieRepository.searchMovies(title);
    }

    @Override
    public Movie findMovieByid(int id) {
        return movieRepository.findMovieByid(id);
    }

    @Override
    public List<MoviePeople> searchMoviePeople(String name) {
        return moviePeopleRepository.searchMoviePeople(name);
    }

    @Override
    public MoviePeople findMoviePeopleByid(int id) {
        return moviePeopleRepository.findMoviePeopleByid(id);
    }
}