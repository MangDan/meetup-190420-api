/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.api.movie.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import io.helidon.api.movie.service.MovieService;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message: curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe: curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting curl -X PUT -H "Content-Type: application/json" -d
 * '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 */

@Path("/api/search/v1")
@ApplicationScoped
public class MovieController {

    @Inject
    private MovieService movieService;
    
    @Path("/movies")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovies(@DefaultValue("") @QueryParam("title") String title) {
        return Response.ok(movieService.searchMovies(title)).build();
    }

    @GET
    @Path("/movies/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMovieById(@PathParam("id") int id) {
        ResponseBuilder builder = Response.status(Status.OK);
        builder.header("Location", "/api/search/v1/movie/"+id);

        return builder.type(MediaType.APPLICATION_JSON).entity(movieService.findMovieByid(id)).build();
    }

    @GET
    @Path("/movie-people")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMoviePeople(@QueryParam("filmography") String filmography) {
        return Response.ok(movieService.searchMoviePeople(filmography)).build();
    }

    @GET
    @Path("/movie-people/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findMoviePeopleById(@PathParam("id") int id) {
        return Response.ok(movieService.findMoviePeopleByid(id)).build();
    }
}