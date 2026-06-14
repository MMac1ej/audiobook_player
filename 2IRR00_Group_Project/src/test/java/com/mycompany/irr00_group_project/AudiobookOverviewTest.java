package com.mycompany.irr00_group_project;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/** 
 * Class that test AudiobookOverview.
 */
public class AudiobookOverviewTest {

    /** 
     * Starts up Javafx before all tests. 
     */
    @BeforeAll
    static void startJavaFx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    /** Tests if all audiobooks are shown when the default message is set.*/
    @Test
    void emptySearchShowsAllAudiobooks() throws IOException {
        AudiobookOverview overview = new AudiobookOverview(() -> {});

        TextField searchBar = new TextField();
        VBox bookHolder = new VBox();

        overview.search(searchBar, bookHolder);

        assertTrue(bookHolder.getChildren().size() > 0);
    }

    /** Tests if no audiobooks are shown when no titles match the search request. */
    @Test
    void searchWithUnknownTitleShowsNoAudiobooks() throws IOException {
        AudiobookOverview overview = new AudiobookOverview(() -> {});

        TextField searchBar = new TextField(".....DefinitelyNotExistingBook.........");
        VBox bookHolder = new VBox();

        overview.search(searchBar, bookHolder);

        assertEquals(0, bookHolder.getChildren().size());
    }
}