package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet,Long> {
    List<Tweet> getTweetsByAccountIdInOrderByDateDesc(Collection<Long> accountIds);
    List<Tweet> findTop10ByOrderByDateDesc();
}
