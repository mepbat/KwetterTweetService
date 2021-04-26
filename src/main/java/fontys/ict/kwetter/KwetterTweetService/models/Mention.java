package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Mention")
@Table(name = "Mention")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "mentions")
    private Set<Tweet> tweets = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String username;

    public Mention() {
    }

    public Mention(Long id, Set<Tweet> tweets, String username) {
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

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
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
