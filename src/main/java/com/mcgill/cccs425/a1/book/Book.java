
package com.mcgill.cccs425.a1.book;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Keawe Aquarian
 */

@XmlRootElement(name = "book")
public class Book implements Serializable
{
    //booklist index
    private int id;
    //Book title
    private String title;

    //Date entered for book.
    @XmlJavaTypeAdapter(BookDatabase.DateAdapter.class)
    private Date date;

    //Empty constructor.
    public Book() {  }

    //Constructor with elements.
    public Book(int id, String title, Date date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public Date getCreationDate() {
        return date;
    }

    public void setCreationDate(Date creationDate) {
        this.date = creationDate;
    }

    public void setId(int id) {
        id = id;
    }

    public void setTitle(String title) {
        title = title;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id = " + id +
                ", title = '" + title + '\'' +
                ", date = " + date +
                '}';
    }


}

