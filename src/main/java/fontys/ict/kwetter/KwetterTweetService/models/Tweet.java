package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Tweet")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @Column
    private Date date;
    @Column(name = "account_id",nullable = false)
    private Long accountId;
    @OneToMany(mappedBy = "tweet")
    private List<Mention> mentions;

    @OneToMany(mappedBy = "tweet")
    private List<Tag> tags;

    public Tweet() {
    }

    public Tweet(Long id, String text, Date date, Long accountId, List<Mention> mentions, List<Tag> tags) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.accountId = accountId;
        this.mentions = mentions;
        this.tags = tags;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public List<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mention> mentions) {
        this.mentions = mentions;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
