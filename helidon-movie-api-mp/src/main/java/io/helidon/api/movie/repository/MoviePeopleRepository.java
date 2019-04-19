package io.helidon.api.movie.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import io.helidon.api.movie.entity.MoviePeople;

@ApplicationScoped
public class MoviePeopleRepository {

    private final DataSource dataSource;

    /**
     * Creates a new {@link TablesResource}.
     *
     * @param dataSource the {@link DataSource} to use to acquire database table
     *                   names; must not be {@code null}
     *
     * @exception NullPointerException if {@code dataSource} is {@code
     * null}
     */
    @Inject
    public MoviePeopleRepository(@Named("oracle") final DataSource dataSource) {
        super();
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    public List<MoviePeople> searchMoviePeople(String filmography) {
        List<MoviePeople> moviePeopleArray = new ArrayList<MoviePeople>();
        String clause = "";
        if(!filmography.equals(""))
        clause = "WHERE FILMOGRAPHY LIKE '%' || ? || '%'";
        
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT * FROM MOVIE_PEOPLE " + clause + " ORDER BY ID DESC");
        
        try {
            Connection conn = this.dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(queryBuffer.toString());

            if(!filmography.equals(""))
                ps.setString(1, filmography);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                MoviePeople moviePeople = new MoviePeople();
                moviePeople.setId(rs.getInt("id"));
                moviePeople.setName(Optional.ofNullable(rs.getString("name")).orElse(""));
                moviePeople.setRole(Optional.ofNullable(rs.getString("role")).orElse(""));
                moviePeople.setFilmography(Optional.ofNullable(rs.getString("filmography")).orElse(""));
                
                moviePeopleArray.add(moviePeople);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moviePeopleArray;
    }

    public MoviePeople findMoviePeopleByid(int id) {
        MoviePeople moviePeople = new MoviePeople();

        try {
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM MOVIE_PEOPLE WHERE ID = ?");

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                moviePeople.setId(rs.getInt("id"));
                moviePeople.setName(Optional.ofNullable(rs.getString("name")).orElse(""));
                moviePeople.setRole(Optional.ofNullable(rs.getString("role")).orElse(""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return moviePeople;
    }
}