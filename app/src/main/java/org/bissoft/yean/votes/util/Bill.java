package org.bissoft.yean.votes.util;

/**
 * Contains information about bill.
 * @author      Nataliia Makarenko
 */
public class Bill {
    /** If the bill was not voted. */
    public static final int NOT_VOTED = 0;

    /** If the deputy has voted for this bill. */
    public static final int VOTED_FOR = 1;

    /** If the deputy has voted against this bill. */
    public static final int VOTED_AGAINST = 2;

    /** If the deputy has refrained from voting for this bill. */
    public static final int REFRAINED_FROM_VOTING = 3;

    /** If the bill was accepted. */
    public static final int ACCEPTED = 4;
    /** If the bill was declined. */
    public static final int DECLINED = 5;
    /** If the bill has not been voted for yet. */
    public static final int IN_QUEUE = 6;

    /** Title of the bill. */
    private String title;
    /** Corrects of the bill. */
    private String corrects;
    /** What choice deputy has made while voting for the bill. */
    private int choice;
    /** Whether or not the bill was accepted. */
    private int accepted;

    /** Number of votes for the bill. */
    private int votesFor;
    /** Number of votes against the bill. */
    private int votesAgainst;
    /** Number of votes which mean that deputies were refrained
     * from voting for the bill.
     */
    private int votesRefrained;

    /**
     * Constructs a new instance of {@code Bill}.
     *
     * @param title the title of this {@code Bill}.
     * @param corrects corrects of this {@code Bill}.
     * @param choice the choice param of this {@code Bill}.
     * @param accepted the accepted param of this {@code Bill}.
     * @param votesFor number of votes for this {@code Bill}.
     * @param votesAgainst number of votes against this {@code Bill}.
     * @param votesRefrained number of votes refrained from voting for this {@code Bill}.
     */
    public Bill(String title, String corrects, int choice, int accepted, int votesFor, int votesAgainst, int votesRefrained) {
        this.title = title;
        this.corrects = corrects;
        this.accepted = accepted;
        this.choice = choice;
        this.votesFor = votesFor;
        this.votesAgainst = votesAgainst;
        this.votesRefrained = votesRefrained;
    }

    /**
     * Gets the title of the bill.
     * @return the title of the bill
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets corrects of the bill.
     * @return corrects of the bill
     */
    public String getCorrects() {
        return corrects;
    }

    /**
     * Gets choice of the bill.
     * @return the choice of the bill
     */
    public int getChoice() {
        return choice;
    }
    /**
     * Gets the accepted param of the bill.
     * @return the accepted param of the bill
     */
    public int getAccepted() {
        return accepted;
    }
    /**
     * Gets number of votes for the bill.
     * @return number of votes for the bill
     */
    public int getVotesFor() {
        return votesFor;
    }
    /**
     * Gets number of votes against the bill.
     * @return number of votes against the bill
     */
    public int getVotesAgainst() {
        return votesAgainst;
    }
    /**
     * Gets number of votes refrained from voting for the bill.
     * @return number of votes refrained from voting for the bill
     */
    public int getVotesRefrained() {
        return votesRefrained;
    }
}

