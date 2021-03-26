package fontys.ict.kwetter.KwetterTweetService.models.dto;

import java.util.Date;

public class TweetDto {
    private Long id;
    private String text;
    private Date date;

    public TweetDto() {
    }

    public TweetDto(Long id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
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
}
