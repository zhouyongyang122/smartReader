package com.qudiandu.smartdub.ui.main.model;

import com.qudiandu.smartdub.ui.main.model.bean.SRBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZY on 17/3/31.
 */

public class SRBookSelectManager {

    private static SRBookSelectManager instance;

    private Map<String, SRBook> addBooks = new HashMap<String, SRBook>();

    private SRBookSelectManager() {

    }

    public static SRBookSelectManager getInstance() {
        if (instance == null) {
            instance = new SRBookSelectManager();
        }
        return instance;
    }

    public void addBook(SRBook book) {
        if (book == null) {
            return;
        }

        String key = book.getGrade_id() + ":" + book.getBook_id();
        if (!addBooks.containsKey(key)) {
            addBooks.put(key, book);
        }
    }

    public void removeBook(SRBook book) {
        if (book == null) {
            return;
        }
        String key = book.getGrade_id() + ":" + book.getBook_id();
        addBooks.remove(key);
    }

    public List<SRBook> getAddBooks() {
        List<SRBook> books = new ArrayList<SRBook>();
        if (addBooks.size() > 0) {
            books.addAll(addBooks.values());
        }
        return books;
    }

    public int getAddBooksSize() {
        return addBooks.size();
    }

    public void clearAddBooks() {
        addBooks.clear();
    }
}
