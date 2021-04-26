package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String tag;

    @ManyToMany
    @JoinColumn(nullable = false)
    private List<Tweet> tweets;

    public Tag() {
    }

    public Tag(Long id, String tag, List<Tweet> tweets) {
        this.id = id;
        this.tag = tag;
        this.tweets = tweets;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", tweet count=" + tweets.size() +
                '}';
    }
}
