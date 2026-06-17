package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/** Manages a collection of audiobooks and supports loading them from the filesystem. */
public class Library {

    /** Default folder where audiobooks are expected to be stored. */
    public static final Path DEFAULT_FOLDER =
            Paths.get(System.getProperty("user.home"), "Audiobooks");

    private final List<Audiobook> audiobooks;

    /** Creates an empty Library. */
    public Library() {
        this.audiobooks = new ArrayList<>();
    }

    /**
     * Scans the given folder for audiobooks.
     * Each immediate subdirectory becomes one Audiobook; each audio file
     * (.mp3, .wav, .m4a) within a subdirectory becomes one Episode.
     * Only subdirectories that contain at least one audio file are added.
     *
     * @param folder the root directory to scan
     * @throws IOException if the folder cannot be read
     */
    public void loadFromFolder(Path folder) throws IOException {
        audiobooks.clear();
        try (Stream<Path> dirs = Files.list(folder)) {
            dirs.filter(Files::isDirectory).forEach(this::loadAudiobook);
        }
    }

    private void loadAudiobook(Path dir) {
        Audiobook book = new Audiobook(dir.getFileName().toString(), dir);
        try (Stream<Path> files = Files.walk(dir)) {
            files.filter(Library::isAudioFile)
                 .sorted()
                 .forEach(p -> book.addEpisode(new Episode(p.getFileName().toString(), p)));
        } catch (IOException e) {
            return;
        }
        if (book.getEpisodeCount() > 0) {
            audiobooks.add(book);
        }
    }

    /** Returns true if the path points to a supported audio file (.mp3 or .wav). */
    public static boolean isAudioFile(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".mp3") || name.endsWith(".wav");
    }

    /**
     * Adds an audiobook to the library.
     *
     * @param book the audiobook to add
     * @throws IllegalArgumentException if book is null
     */
    public void addAudiobook(Audiobook book) {
        if (book == null) {
            throw new IllegalArgumentException("Audiobook cannot be null");
        }
        audiobooks.add(book);
    }

    /** Returns an unmodifiable view of all audiobooks in this library. */
    public List<Audiobook> getAudiobooks() {
        return Collections.unmodifiableList(audiobooks);
    }

    /** Returns the number of audiobooks in this library. */
    public int getAudiobookCount() {
        return audiobooks.size();
    }

    /**
     * Searches audiobooks by title using a case-insensitive substring match.
     * A null or empty query returns all audiobooks.
     *
     * @param query the search string
     * @return a list of matching audiobooks
     */
    public List<Audiobook> search(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.unmodifiableList(audiobooks);
        }
        String lower = query.toLowerCase();
        List<Audiobook> result = new ArrayList<>();
        for (Audiobook book : audiobooks) {
            if (book.getTitle().toLowerCase().contains(lower)) {
                result.add(book);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Return the correct audiobook when given the path as a string.
     * 
     * @param path the path in String form
     * @return the corresponding audiobook
     */
    public Audiobook findAudiobookByPathString(String path) {
        for (Audiobook audiobook : getAudiobooks()) {
            if (audiobook.getPath().toString().equals(path)) {
                return audiobook;
            }
        }
        return null;
    }

    /**
     * Returns the audiobook that contains the episode with the given file path,
     * or {@code null} if no match is found.
     *
     * @param episodeFilePath the episode file path as a string
     * @return the containing audiobook, or {@code null}
     */
    public Audiobook findAudiobookByEpisodePath(String episodeFilePath) {
        for (Audiobook book : getAudiobooks()) {
            for (Episode episode : book.getEpisodes()) {
                if (episode.getFilePath().toString().equals(episodeFilePath)) {
                    return book;
                }
            }
        }
        return null;
    }

}