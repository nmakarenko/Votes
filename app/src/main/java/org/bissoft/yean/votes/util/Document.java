package org.bissoft.yean.votes.util;


/**
 * Contains information about document.
 * @author      Nataliia Makarenko
 */
public class Document {
    /** Name of the document. */
    String name;
    /** Path of the document. */
    String path;

    /**
     * Constructs a new instance of {@code Document}.
     *
     * @param name the name of this {@code Document}.
     * @param path the path of this {@code Document}.
     */
    public Document(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Gets name of the document.
     * @return name of the document
     */
    public String getName() {
        return name;
    }

    /**
     * Gets path of the document.
     * @return path of the document
     */
    public String getPath() {
        return path;
    }
}

