package com.mycompany.irr00_group_project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThemeTest {

    @Test
    void getDisplayNameReturnsCorrectDisplayName() {
        assertEquals("Light", Theme.LIGHT.getDisplayName());
        assertEquals("Dark", Theme.DARK.getDisplayName());
        assertEquals("High Contrast", Theme.HIGH_CONTRAST.getDisplayName());
    }

    @Test
    void fromStringWithDisplayNameReturnsCorrectTheme() {
        assertEquals(Theme.LIGHT, Theme.fromString("Light"));
        assertEquals(Theme.DARK, Theme.fromString("Dark"));
        assertEquals(Theme.HIGH_CONTRAST, Theme.fromString("High Contrast"));
    }

    @Test
    void fromStringWithEnumNameReturnsCorrectTheme() {
        assertEquals(Theme.LIGHT, Theme.fromString("LIGHT"));
        assertEquals(Theme.DARK, Theme.fromString("DARK"));
        assertEquals(Theme.HIGH_CONTRAST, Theme.fromString("HIGH_CONTRAST"));
    }

    @Test
    void fromStringIsCaseInsensitive() {
        assertEquals(Theme.LIGHT, Theme.fromString("light"));
        assertEquals(Theme.DARK, Theme.fromString("dark"));
        assertEquals(Theme.HIGH_CONTRAST, Theme.fromString("high contrast"));
        assertEquals(Theme.LIGHT, Theme.fromString("LiGhT"));
    }

    @Test
    void fromStringWithInvalidStringReturnsLightAsDefault() {
        assertEquals(Theme.LIGHT, Theme.fromString("InvalidTheme"));
        assertEquals(Theme.LIGHT, Theme.fromString(""));
        assertEquals(Theme.LIGHT, Theme.fromString("NONEXISTENT"));
    }

    @Test
    void allThemesCanBeIteratedAndConverted() {
        for (Theme theme : Theme.values()) {
            assertEquals(theme, Theme.fromString(theme.name()));
            assertEquals(theme, Theme.fromString(theme.getDisplayName()));
        }
    }

}
