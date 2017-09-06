package com.qudiandu.smartreader.base.event;

/**
 * Created by ZY on 17/9/5.
 */

public class SREventSelectedStudyBook {

    public int classId;

    public int bookId;

    public SREventSelectedStudyBook(int classId, int bookId) {
        this.classId = classId;
        this.bookId = bookId;
    }
}
