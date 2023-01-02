package com.mcgill.cccs425.a1.book;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 * @author Keawe Aquarian
 */
public class BookDatabase {


    //Create thread safe list.
    private static CopyOnWriteArrayList<Book> bookList;
    //Create thread safe Integer
    private static AtomicInteger id = new AtomicInteger();
    //construct BookDatabase and assign instance of list to booklist.
    public BookDatabase(CopyOnWriteArrayList list) {
        bookList = list;
    }

    //Return id by book title
    public int findName(String name){
        for (Book list:bookList) {
            if(list.getTitle().equals(name))
                return list.getId();

        }
        return -1;
    }

    //Return book by id.
    public Book getBook(int id){
            for (Book list :bookList
             ) {
        if (list.getId() == id){
            return bookList.get(id);
        }
    }return null;
}


    //Add a book to booklist.
    public int add(String title, Date date){
        Book b = new Book(id.getAndAdd(1), title, date);
        bookList.add(b);
        return bookList.get(bookList.size()-1).getId();
    }

    //Return the entire list.
    public CopyOnWriteArrayList getBookList(){
        CopyOnWriteArrayList list = new CopyOnWriteArrayList(bookList);
        return list;
    }

    //Check if list contains a title.
    public boolean contains(String title){
        for (Book list :bookList
             ) {
            if (list.getTitle().equals(title)){
                return true;
            }
        }
       return false;
    }

    //Delete a book by title.
    public boolean deleteByName(String title){
        for (Book list :bookList
        ) {
            if (list.getTitle().equals(title)){
                bookList.remove(title);
                return true;
            }
        }
        return false;
    }

    //Delete a book by id.
    public boolean deleteByInt(int id) {
        for (Book list :bookList
        ) {
            if (list.getId() == id) {
                bookList.remove(list);
                return true;
            }
        }
        return false;
    }

    //Update a book matching title.
    public boolean updateByName(String title, Date date) {
        for (Book list :bookList
        ) {
            if (list.getTitle().equals(title)) {
                int newId = list.getId();

                bookList.set(newId, new Book(newId, title, date));
                return true;
            }
        }
        return false;
    }

    //update a book by id.
    public boolean updateById(int id, String title, Date date) {
        for (Book list :bookList
        ) {
            if (list.getId() == id) {
                bookList.set(id, new Book(id, title, date));
                return true;
            }
        }
        return false;
    }

    //Create safe date format.
    public static class DateAdapter extends XmlAdapter<String, Date> {
        private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
        private static final String DATE_PATTERN_WITH_SEC = "yyyy-MM-dd'T'HH:mm:ss";
        private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_WITH_SEC);

        @Override
        public String marshal(Date v) throws Exception {
            dateFormat.setTimeZone(UTC);
            String dateF = dateFormat.format(v);
            return dateF;
        }

        @Override
        public Date unmarshal(String v) throws Exception {
            dateFormat.setTimeZone(UTC);
            Date date = dateFormat.parse(v);
            return date;
        }
    }

}