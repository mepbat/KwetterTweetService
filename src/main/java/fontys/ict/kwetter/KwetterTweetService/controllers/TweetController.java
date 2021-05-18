package fontys.ict.kwetter.KwetterTweetService.controllers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fontys.ict.kwetter.KwetterTweetService.models.HibernateProxyTypeAdapter;
import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import fontys.ict.kwetter.KwetterTweetService.models.Tag;
import fontys.ict.kwetter.KwetterTweetService.models.Tweet;
import fontys.ict.kwetter.KwetterTweetService.models.dto.TweetDto;
import fontys.ict.kwetter.KwetterTweetService.repositories.MentionRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.TagRepository;
import fontys.ict.kwetter.KwetterTweetService.repositories.TweetRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/tweet")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final MentionRepository mentionRepository;
    private final TagRepository tagRepository;
    private final Gson gson;

    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public TweetController(TweetRepository tweetRepository, MentionRepository mentionRepository, TagRepository tagRepository, AmqpTemplate rabbitTemplate) {
        this.tweetRepository = tweetRepository;
        this.mentionRepository = mentionRepository;
        this.tagRepository = tagRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.gson = initiateGson();
    }

    @RequestMapping(value = "/{tweetId}", method = RequestMethod.GET)
    public ResponseEntity<?> getTweet(@PathVariable("tweetId") Long tweetId) {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if (tweet.isEmpty()) {
            return new ResponseEntity<>("Tweet not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gson.toJson(tweet.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/getTimeline", method = RequestMethod.POST)
    public ResponseEntity<?> getTimeline(@RequestBody Collection<Long> accountIds) {
        List<Tweet> tweets = tweetRepository.findTop10ByAccountIdInOrderByDateDesc(accountIds);
        return new ResponseEntity<>(gson.toJson(tweets), HttpStatus.OK);
    }

    @RequestMapping(value = "/getByTag/{tag}", method = RequestMethod.GET)
    public ResponseEntity<?> getTweetsByTag(@PathVariable("tag") String tag) {
        Optional<Tag> optionalTag = tagRepository.findByTag(tag);
        if(optionalTag.isEmpty()){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(tweetRepository.findAllByTags(optionalTag.get())), HttpStatus.OK);
    }

    @RequestMapping(value = "/getMostRecentTweetsByUsername/{username}")
    public ResponseEntity<?> getMostRecentTweetsByUsername(@PathVariable String username) {
        return new ResponseEntity<>(gson.toJson(tweetRepository.findTop10ByUsernameOrderByDateDesc(username)), HttpStatus.OK);
    }

    @RequestMapping(value = "/getLastTweet/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getLastTweet(@PathVariable("username") String username) {
        Optional<Tweet> tweet = tweetRepository.findFirstByUsernameOrderByDateDesc(username);
        if (tweet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(tweet.get()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(gson.toJson(tweetRepository.findTop10ByOrderByDateDesc()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createTweet(@RequestBody TweetDto tweetDto) {
        if (tweetDto.getText() == null || tweetDto.getText().isEmpty()) {
            return new ResponseEntity<>("Cannot post an empty tweet.", HttpStatus.BAD_REQUEST);
        }
        if (tweetDto.getText().length() > 140) {
            return new ResponseEntity<>("Character limit of tweet Exceeded.", HttpStatus.BAD_REQUEST);
        }

        Tweet tweet = new Tweet();
        tweet.setAccountId(tweetDto.getId());
        tweet.setDate(tweetDto.getDate());
        tweet.setText(tweetDto.getText());
        tweet.setUsername(tweetDto.getUsername());
        tweet.setTags(new HashSet<>());
        tweet.setMentions(new HashSet<>());
        tweet = tweetRepository.save(tweet);
        extractsMentionsFromTweet(tweet);
        extractTagsFromTweet(tweet);

        Tweet tweetResult = tweetRepository.save(tweet);
        return new ResponseEntity<>(gson.toJson(tweetResult), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateTweet(@RequestBody Tweet tweet) {
        return new ResponseEntity<>(gson.toJson(tweetRepository.save(tweet)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity<?> deleteTweet(@RequestBody Tweet tweet) {
        tweetRepository.delete(tweet);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Gson initiateGson() {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        boolean exclude = false;
                        try {
                            exclude = EXCLUDE.contains(f.getName());

                        } catch (Exception ignore) {
                        }
                        return exclude;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });
        return b.create();
    }

    private static final List<String> EXCLUDE = new ArrayList<>() {{
        add("tweet");
        add("tweets");

    }};


    private void extractsMentionsFromTweet(Tweet tweet) {
        Matcher matcherMention = Pattern.compile("(@[^@\\s]*)")
                .matcher(tweet.getText());
        while (matcherMention.find()) {
            Mention mention = new Mention();
            mention.setUsername(matcherMention.group().substring(1));
            Optional<Mention> optionalMention = mentionRepository.findByUsername(mention.getUsername());
            if (optionalMention.isPresent()) {
                mention = optionalMention.get();
            } else {
                mention.setTweets(Collections.singleton(tweet));
            }
            Set<Mention> mentions = tweet.getMentions();
            Mention finalMention = mention;
            if (mentions.stream().anyMatch(x -> x.getUsername().equals(finalMention.getUsername()))) {
                continue;
            }
            mentions.add(mention);
            tweet.setMentions(mentions);
            if (matcherMention.hitEnd()) break;
        }
    }

    private void extractTagsFromTweet(Tweet tweet){
        // Check for tags
        Matcher matcherTag = Pattern.compile("(#[^#\\s]*)")
                .matcher(tweet.getText());
        while (matcherTag.find()) {
            Tag tag = new Tag();
            tag.setTag(matcherTag.group().substring(1));
            Optional<Tag> optionalTag = tagRepository.findByTag(tag.getTag());
            if(optionalTag.isPresent()){
                tag = optionalTag.get();
            } else{
                tag.setTweets(Collections.singleton(tweet));
            }
            Set<Tag> tags = tweet.getTags();
            Tag finalTag = tag;
            if(tags.stream().anyMatch(x -> x.getTag().equals(finalTag.getTag()))){
                continue;
            }
            tags.add(tag);
            tweet.setTags(tags);
            if (matcherTag.hitEnd()) break;
        }
    }
}
