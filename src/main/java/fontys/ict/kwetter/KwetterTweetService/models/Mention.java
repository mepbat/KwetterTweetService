package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;

@Entity
@Table(name = "Mention")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    private Tweet tweet;

    @Column(unique = false, name ="account_id")
    private Long accountId;

    public Mention() {
    }

    public Mention(Long id, Tweet tweet, Long accountId) {
        this.id = id;
        this.tweet = tweet;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
