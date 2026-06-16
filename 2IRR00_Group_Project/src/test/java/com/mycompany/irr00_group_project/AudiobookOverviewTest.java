package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.gui.AudiobookOverview;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that test AudiobookOverview.
 */
public class AudiobookOverviewTest {

    /** Tests if all audiobooks are shown when the search query is empty.*/
    @Test
    void emptySearchShowsAllAudiobooks() {
        Library library = new Library();
        library.addAudiobook(new Audiobook("Dune", Path.of("/tmp/dune")));
        library.addAudiobook(new Audiobook("Lord of the Rings", Path.of("/tmp/lotr")));
        library.addAudiobook(new Audiobook("Secret History", Path.of("/tmp/secret-history")));
        library.addAudiobook(new Audiobook("The Hitchhiker's Guide to the Galaxy",
                Path.of("/tmp/hhgttg")));

        AudiobookOverview overview = new AudiobookOverview();
        overview.setLibrary(library);
        overview.search("");

        assertTrue(overview.getCurrentAudiobooks().size() > 0);
    }

    /** Tests if no audiobooks are shown when no titles match the search request. */
    @Test
    void searchWithUnknownTitleShowsNoAudiobooks() {
        Library library = new Library();
        library.addAudiobook(new Audiobook("Dune", Path.of("/tmp/dune")));
        library.addAudiobook(new Audiobook("Lord of the Rings", Path.of("/tmp/lotr")));
        library.addAudiobook(new Audiobook("Secret History", Path.of("/tmp/secret-history")));
        library.addAudiobook(new Audiobook("The Hitchhiker's Guide to the Galaxy",
                Path.of("/tmp/hhgttg")));

        AudiobookOverview overview = new AudiobookOverview();
        overview.setLibrary(library);
        overview.search("NonexistingBook.123.456.789. :)");

        assertEquals(0, overview.getCurrentAudiobooks().size());
    }
}
