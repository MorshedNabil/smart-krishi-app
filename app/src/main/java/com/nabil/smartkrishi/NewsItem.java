package com.nabil.smartkrishi;

public class NewsItem {
    private final String title;
    private final String description;
    private final String date;
    // Optional: Add an image URL or resource ID
    private final String imageUrl;

    public NewsItem(String title, String description, String date, String imageUrl) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
