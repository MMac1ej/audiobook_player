package com.mycompany.irr00_group_project;

/** Enum representing the available themes for the application.
 * @author Tymon Szczepański, 2330113
 */
public enum Theme {
    LIGHT("Light", Constants.STYLE_SETTINGS_LIGHT),
    DARK("Dark", Constants.STYLE_SETTINGS_DARK),
    HIGH_CONTRAST("High Contrast", Constants.STYLE_SETTINGS_HIGH_CONTRAST);

    private final String displayName;
    private final String cssPath;

    Theme(String displayName, String cssPath) {
        this.displayName = displayName;
        this.cssPath = cssPath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssPath() {
        return cssPath;
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
