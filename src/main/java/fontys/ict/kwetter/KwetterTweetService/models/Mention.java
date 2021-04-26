package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Mention")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(nullable = false)
    private List<Tweet> tweets;

    @Column(nullable = false, unique = true)
    private String username;

    public Mention() {
    }

    public Mention(Long id, List<Tweet> tweets, String username) {
        this.id = id;
        this.tweets = tweets;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
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
                ", tweet count=" + tweets.size() +
                ", username='" + username + '\'' +
                '}';
    }
}
