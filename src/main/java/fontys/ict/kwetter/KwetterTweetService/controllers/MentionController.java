package fontys.ict.kwetter.KwetterTweetService.controllers;

import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import fontys.ict.kwetter.KwetterTweetService.repositories.MentionRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/mention")
public class MentionController {
    private final MentionRepository mentionRepository;
    private final AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public MentionController(MentionRepository mentionRepository, AmqpTemplate rabbitTemplate) {
        this.mentionRepository = mentionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping(value = "/{mentionId}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<Mention> getMention(@PathVariable("mentionId") Long mentionId) {
        return mentionRepository.findById(mentionId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Mention> getAll() {
        return mentionRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody Mention createMention(@RequestBody Mention mention){
        return mentionRepository.save(mention);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public @ResponseBody Mention updateMention(@RequestBody Mention mention){
        return mentionRepository.save(mention);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public @ResponseBody void deleteMention(@RequestBody Mention mention){
        mentionRepository.delete(mention);
    }
}
