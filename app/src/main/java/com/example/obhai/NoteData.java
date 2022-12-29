package com.example.obhai;

public class NoteData {

    String Title;
    String Content;

    public NoteData() {
    }

    public NoteData(String title, String content) {
        Title = title;
        Content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
