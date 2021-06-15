package pablo.ad.mychatclient.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Mensajes.class}, version = 1, exportSchema = false)
public abstract class MensajesDB extends RoomDatabase {

    public abstract MensajesDao getMensajesDao();

    private volatile static MensajesDB INSTANCE;

    public static MensajesDB getDb(final Context context) {

        if (INSTANCE == null) {
            synchronized (MensajesDB.class) {
                if (INSTANCE == null) {
                    INSTANCE =  Room.databaseBuilder(context.getApplicationContext(),
                            MensajesDB.class, "basededatos.sqlite")
                            .addCallback(callback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }


    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MensajesDao mensajesDao = INSTANCE.getMensajesDao();
                }
            }).start();
        }
    };

}
