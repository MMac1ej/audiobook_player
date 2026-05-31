package com.mycompany.irr00_group_project;

/** Enum representing the available themes for the application.
 * @author Tymon Szczepański, 2330113
 */
public enum Theme {
    LIGHT("Light"),
    DARK("Dark"),
    HIGH_CONTRAST("High Contrast");

    private final String displayName;

    Theme(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string to a Theme enum value. 
     * The string can match either the display name or the enum name, 
     * case-insensitively.
     * @param themeStr the string to convert
     * @return the corresponding Theme enum value, or LIGHT if no match is found
    */
    public static Theme fromString(String themeStr) {
        for (Theme theme : Theme.values()) {
            if (
                theme.getDisplayName().equalsIgnoreCase(themeStr) 
                || theme.name().equalsIgnoreCase(themeStr)) {
                return theme;
            }
        }
        return LIGHT; // default theme
    }
}
