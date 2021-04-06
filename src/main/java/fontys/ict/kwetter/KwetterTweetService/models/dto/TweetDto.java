package fontys.ict.kwetter.KwetterTweetService.models.dto;

import java.util.Date;

public class TweetDto {
    private Long id;
    private String text;
    private Date date;
    private String username;

    public TweetDto() {
    }

    public TweetDto(Long id, String text, Date date, String username) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
