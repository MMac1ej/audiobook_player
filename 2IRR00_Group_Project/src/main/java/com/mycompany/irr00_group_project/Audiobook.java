package com.mycompany.irr00_group_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents an audiobook consisting of an ordered list of episodes. */
public class Audiobook {

    private final String title;
    private final List<Episode> episodes;

    /**
     * Creates an empty Audiobook with the given title.
     *
     * @param title the display name of this audiobook
     */
    public Audiobook(String title) {
        this.title = title;
        this.episodes = new ArrayList<>();
    }

    /** Returns the display name of this audiobook. */
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * Adds an episode to the end of this audiobook.
     *
     * @param ep the episode to add
     * @throws IllegalArgumentException if episode is null
     */
    public void addEpisode(Episode ep) {
        if (ep == null) {
            throw new IllegalArgumentException("Episode cannot be null");
        }
        episodes.add(ep);
    }

    /**
     * Returns the episode at the specified index.
     *
     * @param index zero-based position in the episode list
     * @return the episode at the given index
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public Episode getEpisode(int index) {
        return episodes.get(index);
    }

    /** Returns the number of episodes in this audiobook. */
    public int getEpisodeCount() {
        return episodes.size();
    }

    /** Returns an unmodifiable view of the episode list. */
    public List<Episode> getEpisodes() {
        return Collections.unmodifiableList(episodes);
    }

}