package fontys.ict.kwetter.KwetterTweetService.controllers;

import fontys.ict.kwetter.KwetterTweetService.models.Mention;
import fontys.ict.kwetter.KwetterTweetService.repositories.MentionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8081","http://localhost:8082","http://localhost:8083"})
@RequestMapping(value = "/mention")
public class MentionController {
    private final MentionRepository mentionRepository;

    public MentionController(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
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
