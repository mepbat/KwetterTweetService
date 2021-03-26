package fontys.ict.kwetter.KwetterTweetService.controllers;

import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import fontys.ict.kwetter.KwetterTweetService.models.dto.TweetDto;
import fontys.ict.kwetter.KwetterTweetService.repositories.TweetRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8081","http://localhost:8082","http://localhost:8083"})
@RequestMapping(value = "/tweet")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public TweetController(TweetRepository tweetRepository, AmqpTemplate rabbitTemplate) {
        this.tweetRepository = tweetRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping(value = "/{tweetId}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<Tweet> getTweet(@PathVariable("tweetId") Long tweetId) {
        return tweetRepository.findById(tweetId);
    }

    @RequestMapping(value = "/{accountIds}", method = RequestMethod.GET)
    public @ResponseBody
    List<Tweet> getTimeline(@PathVariable("accountIds") Collection<Long> accountIds) {
        return tweetRepository.getTweetsByAccountIdInOrderByDateDesc(accountIds).subList(0,10); //Get the 10 most recent tweets from user and its follows
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Tweet> getAll() {
        return tweetRepository.findTop10ByOrderByDateDesc();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createTweet(@RequestBody TweetDto tweetDto){
        if(tweetDto.getText() == null || tweetDto.getText().isEmpty()){
            return new ResponseEntity<>("Cannot post an empty tweet.", HttpStatus.BAD_REQUEST);
        }
        if(tweetDto.getText().length() > 140){
            return new ResponseEntity<>("Character limit of tweet Exceeded.", HttpStatus.BAD_REQUEST);
        }
        //TODO check for mentions and tags
        // Regex to find mentions
        Matcher matcher = Pattern.compile("(@[^@\\s]*)")
                .matcher(tweetDto.getText());

        Tweet tweet = new Tweet();
        tweet.setAccountId(tweetDto.getId());
        tweet.setDate(tweetDto.getDate());
        tweet.setText(tweetDto.getText());
        return new ResponseEntity<>(tweetRepository.save(tweet), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public @ResponseBody Tweet updateTweet(@RequestBody Tweet tweet){
        return tweetRepository.save(tweet);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public @ResponseBody void deleteTweet(@RequestBody Tweet tweet){
        tweetRepository.delete(tweet);
    }


}
