package io.helidon.api.movie.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import io.helidon.api.movie.entity.Movie;

@ApplicationScoped
public class MovieRepository {

    @Inject
    @Named("oracle")
    private DataSource dataSource;

    /**
     * Creates a new {@link TablesResource}.
     *
     * @param dataSource the {@link DataSource} to use to acquire database table
     *                   names; must not be {@code null}
     *
     * @exception NullPointerException if {@code dataSource} is {@code
     * null}
     */
    // @Inject
    // public MovieRepository(@Named("oracle") final DataSource dataSource) {
    //     super();
    //     this.dataSource = dataSource;
    // }

    public List<Movie> searchMovies(String title) {
        List<Movie> movieArray = new ArrayList<Movie>();
        String clause = "";
        if(!title.equals(""))
        clause = "AND TITLE LIKE ? || '%'";
        
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("SELECT ID, TITLE, POSTER_PATH, VOTE_COUNT, VOTE_AVERAGE, RELEASE_DATE FROM MOVIE WHERE  ROWNUM <= 50 " + clause + " ORDER BY ID DESC");

        System.out.println(queryBuffer.toString());
        try {
            Connection conn = this.dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(queryBuffer.toString());

            if(!title.equals(""))
                ps.setString(1, title);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(Optional.ofNullable(rs.getString("title")).orElse(""));
                movie.setPoster_path(Optional.ofNullable(rs.getString("poster_path")).orElse(""));
                movie.setVote_count(Optional.ofNullable(rs.getInt("vote_count")).orElse(0));
                movie.setVote_average(Optional.ofNullable(rs.getInt("vote_average")).orElse(0));
                movie.setRelease_date(Optional.ofNullable(rs.getString("release_date")).orElse(""));
                movieArray.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieArray;
    }

    public Movie findMovieByid(int id) {
        Movie movie = new Movie();

        try {
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM MOVIE WHERE ID = ?");

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                movie.setId(rs.getInt("id"));
                movie.setTitle(Optional.ofNullable(rs.getString("title")).orElse(""));
                movie.setVote_count(Optional.ofNullable(rs.getInt("vote_count")).orElse(0));
                movie.setVote_average(Optional.ofNullable(rs.getInt("vote_average")).orElse(0));
                movie.setPoster_path(Optional.ofNullable(rs.getString("poster_path")).orElse(""));
                movie.setRelease_date(Optional.ofNullable(rs.getString("release_date")).orElse(""));
                movie.setOverview(Optional.ofNullable(rs.getString("overview")).orElse(""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movie;
    }
}