package rajendrapatil.api.notes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rajendrapatil.api.notes.NoteOuter.Note;
import rajendrapatil.api.notes.NoteOuter.Notes;
import rajendrapatil.api.notes.UserNotes;
import redis.clients.jedis.Jedis;

public class UserNotesImpl implements UserNotes {

  private final Jedis jedis;

  public UserNotesImpl(Jedis jedis) {
    this.jedis = jedis;
  }

  @Override
  public Notes getUserNotes(String userId) {
    // time, content
    Map<String, String> notesMap = jedis.hgetAll("user#" + userId);
    List<Note> userNotes = new ArrayList<>();
    for (String time : notesMap.keySet()) {
      userNotes.add(
          Note.newBuilder()
              .setTime(Long.parseLong(time))
              .setContent(notesMap.get(time))
              .build());
    }
    return Notes.newBuilder().addAllNotes(userNotes).build();
  }

  @Override
  public boolean addUserNote(String userId, Note note) {
    jedis.hset("user#" + userId, "" + note.getTime(), note.getContent());
    return true;
  }

  @Override
  public boolean deleteUserNote(String userId, String time) {
    jedis.hdel("user#" + userId, time);
    return false;
  }

  @Override
  public boolean updateUserNote(String userId, Note note) {
    jedis.hset("user#" + userId, "" + note.getTime(), note.getContent());
    return true;
  }
}
