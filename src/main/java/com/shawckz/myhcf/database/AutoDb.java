/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.shawckz.myhcf.database.search.DBSearch;
import org.bson.Document;

public interface AutoDB {

    void push(AutoDBable a);

    void fetch(AutoDBable a, DBSearch search);

    void delete(AutoDBable a);

    void fromDocument(AutoDBable a, Document document);

    Document toDocument(AutoDBable a);

}