package rajendrapatil.api.notes.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import java.net.URISyntaxException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rajendrapatil.api.notes.NoteOuter.Note;
import rajendrapatil.api.notes.NoteOuter.Notes;
import rajendrapatil.api.notes.UserNotes;
import rajendrapatil.api.notes.impl.UserNotesImpl;
import redis.clients.jedis.JedisPool;

@RestController
@CrossOrigin(maxAge = 3600)
public class NotesController {

  final UserNotes userNotes;

  NotesController(@Autowired JedisPool jedisPool) throws URISyntaxException {
    userNotes = new UserNotesImpl(jedisPool);
  }

  @RequestMapping(value = "/{userId}/notes", method = RequestMethod.GET, produces = "application/json")
  public Notes getNotes(@PathVariable String userId) {
    return userNotes.getUserNotes(userId);
  }

  @RequestMapping(value = "/{userId}/notes/add", method = RequestMethod.PUT, produces = "application/json")
  public Note addNote(@PathVariable String userId, @RequestBody Map<String, String> noteData)
      throws InvalidProtocolBufferException {
    Note note = Note.newBuilder()
        .setTime(Long.parseLong(noteData.get("time")))
        .setContent(noteData.get("content"))
        .build();
    userNotes.addUserNote(userId, note);
    return note;
  }

  @RequestMapping(value = "/{userId}/notes/{time}/delete", method = RequestMethod.DELETE)
  public void deleteNote(@PathVariable String userId, @PathVariable String time) {
    userNotes.deleteUserNote(userId, time);
  }

  @RequestMapping(value = "/{userId}/notes/{time}/update", method = RequestMethod.PUT, produces = "application/json")
  public Note updateNote(@PathVariable String userId, @PathVariable String time,
      @RequestBody Map<String, String> noteData) {
    Note note = Note.newBuilder().setTime(Long.parseLong(time)).setContent(noteData.get("content"))
        .build();
    userNotes.updateUserNote(userId, note);
    return note;
  }
}
