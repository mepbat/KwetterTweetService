package fontys.ict.kwetter.KwetterTweetService.repositories;

import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    @Query(value = "SELECT TOP(10) tag.tag\n" +
            "FROM dbo.tag\n" +
            "INNER JOIN dbo.tweet ON tweet.id = tag.tweet_id\n" +
            "WHERE tweet.date > DATEADD(HH, -24, GETDATE())\n" +
            "GROUP BY tag.tag\n" +
            "ORDER BY COUNT(tag.tag) DESC",
    nativeQuery = true)
    List<String> getTrends();

    List<Tag> findAllByTagContaining(String tag);

    Optional<Tag> findByTag(String tag);
}
