package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Tweet")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String tweet;
    @Column
    private Timestamp timestamp;
    @Column(name = "account_id",nullable = false)
    private Long accountId;
    @OneToMany(mappedBy = "tweet")
    private List<Mention> mentions;

    @OneToMany(mappedBy = "tweet")
    private List<Tag> tags;

    public Tweet() {
    }

    public Tweet(Long id, String tweet, Timestamp timestamp, Long accountId, List<Mention> mentions, List<Tag> tags) {
        this.id = id;
        this.tweet = tweet;
        this.timestamp = timestamp;
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

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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
