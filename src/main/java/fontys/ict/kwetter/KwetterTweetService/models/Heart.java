package fontys.ict.kwetter.KwetterTweetService.models;

import fontys.ict.kwetter.KwetterTweetService.models.dto.HeartDto;

import javax.persistence.*;

@Entity(name = "Heart")
@Table(name = "Heart")
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long tweetId;
    @Column
    private long userId;

    public Heart() {
    }

    public Heart(long id, long tweetId, long userId) {
        this.id = id;
        this.tweetId = tweetId;
        this.userId = userId;
    }

    public Heart(HeartDto heartDto){
        this.tweetId = heartDto.getTweetId();
        this.userId = heartDto.getUserId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
