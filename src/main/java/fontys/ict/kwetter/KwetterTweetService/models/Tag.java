package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "Tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<Tweet> tweets = new HashSet<>();

    public Tag() {
    }

    public Tag(Long id, String tag, Set<Tweet> tweets) {
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

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
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
