package org.bissoft.yean.votes.util;

/**
 * Contains information about the reporter.
 * @author      Nataliia Makarenko
 */
public class Reporter {
    /** Name of the reporter. */
    String name;
    /** Post of the reporter. */
    String post;

    /**
     * Constructs a new instance of {@code Reporter}.
     *
     * @param name the name of this {@code Reporter}.
     * @param post the post of this {@code Reporter}.
     */
    public Reporter(String name, String post) {
        this.name = name;
        this.post = post;
    }

    /**
     * Gets name of the reporter.
     * @return name of the reporter
     */
    public String getName() {
        return name;
    }

    /**
     * Gets post of the reporter.
     * @return post of the reporter
     */
    public String getPost() {
        return post;
    }
}

