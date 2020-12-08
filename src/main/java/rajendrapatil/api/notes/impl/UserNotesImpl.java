package rajendrapatil.api.notes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rajendrapatil.api.notes.NoteOuter.Note;
import rajendrapatil.api.notes.NoteOuter.Notes;
import rajendrapatil.api.notes.UserNotes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class UserNotesImpl implements UserNotes {

  private final JedisPool jedisPool;

  public UserNotesImpl(JedisPool pool) {
    this.jedisPool = pool;
  }

  @Override
  public Notes getUserNotes(String userId) {
    Jedis jedis = jedisPool.getResource();
    // time, content
    Map<String, String> notesMap = jedis.hgetAll("user#" + userId);
    jedis.close();
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
    Jedis jedis = jedisPool.getResource();
    // Format is user#userId, time, contents
    jedis.hset("user#" + userId, "" + note.getTime(), note.getContent());
    jedis.close();
    return true;
  }

  @Override
  public boolean deleteUserNote(String userId, String time) {
    Jedis jedis = jedisPool.getResource();
    jedis.hdel("user#" + userId, time);
    jedis.close();

    return false;
  }

  @Override
  public boolean updateUserNote(String userId, Note note) {
    Jedis jedis = jedisPool.getResource();
    jedis.hset("user#" + userId, "" + note.getTime(), note.getContent());
    jedis.close();
    return true;
  }
}
