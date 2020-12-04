package rajendrapatil.api.notes;

import java.util.List;
import rajendrapatil.api.notes.NoteOuter.Note;
import rajendrapatil.api.notes.NoteOuter.Notes;

public interface UserNotes {

  public Notes getUserNotes(String userId);

  public boolean addUserNote(String userId, Note note);

  public boolean deleteUserNote(String userId, String time);

  public boolean updateUserNote(String userId, Note note);
}
