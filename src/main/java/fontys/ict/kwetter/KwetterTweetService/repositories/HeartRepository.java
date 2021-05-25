package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
    
import javax.transaction.Transactional;

public interface HeartRepository extends JpaRepository<Heart,Long> {
    boolean existsByTweetIdAndUserId(long tweetId, long userId);

    @Transactional
    void deleteAllByTweetIdAndUserId(long tweetId, long userId);
}
