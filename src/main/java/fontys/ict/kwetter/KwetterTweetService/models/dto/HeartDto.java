package fontys.ict.kwetter.KwetterTweetService.models.dto;

public class HeartDto {
    private long tweetId;
    private long userId;

    public HeartDto() {
    }

    public HeartDto(long tweetId, long userId) {
        this.tweetId = tweetId;
        this.userId = userId;
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
