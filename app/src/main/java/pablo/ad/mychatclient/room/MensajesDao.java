package pablo.ad.mychatclient.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MensajesDao {

    @Insert
    long insert(Mensajes mensaje);

    @Query("select * from mensaje")
    LiveData<List<Mensajes>> getAllMensajes();

}
