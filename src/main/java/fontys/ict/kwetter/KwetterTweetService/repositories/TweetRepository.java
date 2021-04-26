package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet,Long> {
    List<Tweet> findTweetsByAccountIdInOrderByDateDesc(Collection<Long> accountIds);
    List<Tweet> findTop10ByOrderByDateDesc();
    List<Tweet> findTop10ByUsernameOrderByDateDesc(String username);
    Optional<Tweet> findFirstByUsernameOrderByDateDesc(String username);
    List<Tweet> findByIdInOrderByDateDesc(Collection<Long> ids);
    List<Tweet> findAllByTags(Tag tag);
}
