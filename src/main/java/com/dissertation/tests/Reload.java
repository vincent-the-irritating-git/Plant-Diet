
import com.dissertation.AddDishController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.dissertation.server_side.Database;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class Reload {
  AddDishController ad;
  Database          db;

  @Test
  void reset() {
    db = new Database();
    if (db.getConnection() != null) {
      db.closeDatabase();
    }
    db.openDatabase();
    db.resetDatabase("src//main//resources//dissertation//tabledef", "src//main//resources//dissertation//blankdata");
    db.closeDatabase();
  }
}