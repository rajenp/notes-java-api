package rajendrapatil.api.notes.controller;

import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping(value = "/{userId}/notes", produces = "application/json")
  public Notes getNotes(@PathVariable String userId) {
    return userNotes.getUserNotes(userId);
  }

  @PutMapping(value = "/{userId}/notes/add", produces = "application/json")
  public Note addNote(@PathVariable String userId, @RequestBody Map<String, String> noteData) {
    Note note = Note.newBuilder()
        .setTime(Long.parseLong(noteData.get("time")))
        .setContent(noteData.get("content"))
        .build();
    userNotes.addUserNote(userId, note);
    return note;
  }

  @PutMapping(value = "/{userId}/notes/{time}/update", produces = "application/json")
  public Note updateNote(@PathVariable String userId, @PathVariable String time,
      @RequestBody Map<String, String> noteData) {
    Note note = Note.newBuilder().setTime(Long.parseLong(time)).setContent(noteData.get("content"))
        .build();
    userNotes.updateUserNote(userId, note);
    return note;
  }

  @DeleteMapping(value = "/{userId}/notes/{time}/delete")
  public void deleteNote(@PathVariable String userId, @PathVariable String time) {
    userNotes.deleteUserNote(userId, time);
  }
}
