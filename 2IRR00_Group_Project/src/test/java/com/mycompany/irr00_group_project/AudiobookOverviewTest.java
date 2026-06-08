package com.mycompany.irr00_group_project;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class AudiobookOverviewTest {

    @BeforeAll
    static void startJavaFx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void searchWithDefaultMessageShowsAllAudiobooks() throws IOException {
        AudiobookOverview overview = new AudiobookOverview();

        TextField searchBar = new TextField(AudiobookOverview.DEFAULT_MESSAGE);
        VBox bookHolder = new VBox();

        overview.search(searchBar, bookHolder);

        assertTrue(bookHolder.getChildren().size() > 0);
    }

    @Test
    void searchWithUnknownTitleShowsNoAudiobooks() throws IOException {
        AudiobookOverview overview = new AudiobookOverview();

        TextField searchBar = new TextField(".....DefinitelyNotExistingBook.........");
        VBox bookHolder = new VBox();

        overview.search(searchBar, bookHolder);

        assertEquals(0, bookHolder.getChildren().size());
    }
}
