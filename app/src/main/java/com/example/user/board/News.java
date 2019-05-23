package com.example.user.board;


public class News {

    private String imageuri;
    private String content,faculty, fineprint,dating;

    public News()
    {

    }
    public News(String content,String dating,String fineprint,String faculty,String imageuri) {
        this.content = content;
        this.fineprint = fineprint;
        this.faculty = faculty;
        this.dating = dating;
        this.imageuri = imageuri;
    }


    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getFaculty()
    {
        return faculty;
    }

    public void setFaculty(String faculty)
    {
        this.faculty = faculty;
    }

    public String getDate()
    {
        return dating;
    }
    public void setDate(String dating)
    {
        this.dating = dating;
    }
    public String getFineprint()
    {
        return fineprint;
    }
    public void setFineprint(String fineprint)
    {
        this.fineprint = fineprint;
    }
    public String getImageuri()
    {
        return imageuri;
    }
    public void setImageuri(String imageuri)
    {
        this.imageuri = imageuri;
    }
}




