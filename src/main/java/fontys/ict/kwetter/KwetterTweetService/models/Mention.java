package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;

@Entity
@Table(name = "Mention")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

    @Column
    private String username;

    public Mention() {
    }

    public Mention(Long id, Tweet tweet, String username) {
        this.id = id;
        this.tweet = tweet;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Mention{" +
                "id=" + id +
                ", tweet=" + tweet.getId() +
                ", username='" + username + '\'' +
                '}';
    }
}
