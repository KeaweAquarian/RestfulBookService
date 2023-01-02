package com.mcgill.cccs425.a1.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;


import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Keawe Aquarian
 */

@Path("/book")
public class BookResorce {
    //Create thread safe list.
    static CopyOnWriteArrayList list = new CopyOnWriteArrayList<>();

    //Create instance of Book database
    BookDatabase db = new BookDatabase(list);
    //Create object mapper to convert json to book.
    private static final ObjectMapper mapper = new ObjectMapper();

    //Basic get command to read booklist.
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String bookList() {
        return list.toString();
    }


    // Read command to find if a title is in list and its index.
    @Path("/title")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String findTitle(@QueryParam("title") String name) {

        if (db.findName(name)== -1) return "Book title not in database.";
        else return "Title found with id " + db.findName(name) ;
    }

    //Read command to return json representation of a Book at specified id.
    @GET
    @Path("id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@QueryParam("id") int id) {
        Book book = db.getBook(id);
        if (book != null) {
            return Response.ok(book, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // A list of all Books in JSON
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CopyOnWriteArrayList entireBookList() {

        return db.getBookList();
    }

    //Consumes a title and date in json and converts it to a book object and adds it to list. Produces message response of results.
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String handleBookRequest(String json) throws Exception {
        //Create a date adapter to convert string date to Date.
        BookDatabase.DateAdapter dateAdapter = new BookDatabase.DateAdapter();
        //Use object mapper to access json string elements.
        JsonNode json2 = mapper.readTree(json);

        //Check if list already contains book.
        if (db.contains(json2.get("title").asText())){
            return "Title is already in booklist.";
        }
        //Add Book and respond with success message
        return  json2.get("title").asText() + " added with id "+ db.add(json2.get("title").asText(), dateAdapter.unmarshal(json2.get("date").asText()));
    }

    //Update existing booklist id.
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean update(String json) throws Exception {
        //Create a date adapter to convert string date to Date.
        BookDatabase.DateAdapter dateAdapter = new BookDatabase.DateAdapter();
        //Use object mapper to access json string elements.
        JsonNode json2 = mapper.readTree(json);
        //Update Book at index.
       return db.updateById(json2.get("id").asInt(), json2.get("title").asText(), dateAdapter.unmarshal(json2.get("date").asText()));


    }

    //Delete existing Book in booklist by id.
    @DELETE
    public Response delete(@QueryParam("id") int id) {
        if (db.deleteByInt(id)) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }
}
