package fontys.ict.kwetter.KwetterTweetService.models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Tweet")
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
    @Column
    private String username;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tweet_mention",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "mention_id")
    )
    private Set<Mention> mentions = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tweet_tag",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags= new HashSet<>();

    public Tweet() {
    }

    public Tweet(Long id, String text, Date date, Long accountId, String username, Set<Mention> mentions, Set<Tag> tags) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.accountId = accountId;
        this.username = username;
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

    public Set<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(Set<Mention> mentions) {
        this.mentions = mentions;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", accountId=" + accountId +
                ", username='" + username + '\'' +
                ", mentions=" + mentions.size() +
                ", tags=" + tags.size() +
                '}';
    }
}
