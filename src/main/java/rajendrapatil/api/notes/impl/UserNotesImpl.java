package rajendrapatil.api.notes.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;
import rajendrapatil.api.notes.NoteOuter.Note;
import rajendrapatil.api.notes.NoteOuter.Notes;
import rajendrapatil.api.notes.UserNotes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class UserNotesImpl implements UserNotes {

  private static final String PREFIX = "user#";

  private final JedisPool jedisPool;

  public UserNotesImpl(JedisPool pool) {
    this.jedisPool = pool;
  }

  @Override
  public Notes getUserNotes(String userId) {
    Jedis jedis = jedisPool.getResource();
    // time, content
    Map<String, String> notesMap = jedis.hgetAll(key(userId));
    jedis.close();
    List<Note> userNotes = new ArrayList<>();
    for (Entry<String, String> entry : notesMap.entrySet()) {
      userNotes.add(Note.newBuilder().setTime(Long.parseLong(entry.getKey())).setContent(entry.getValue()).build());
    }
    return Notes.newBuilder().addAllNotes(userNotes).build();
  }

  @Override
  public boolean addUserNote(String userId, Note note) {
    Jedis jedis = jedisPool.getResource();
    // Format is user#userId, time, contents
    jedis.hset(key(userId), "" + note.getTime(), note.getContent());
    jedis.close();
    return true;
  }

  @Override
  public boolean deleteUserNote(String userId, String time) {
    Jedis jedis = jedisPool.getResource();
    jedis.hdel(key(userId), time);
    jedis.close();

    return false;
  }

  @Override
  public boolean updateUserNote(String userId, Note note) {
    return addUserNote(userId, note);
  }

  private static String key(String userId) {
    return PREFIX + userId;
  }
}
