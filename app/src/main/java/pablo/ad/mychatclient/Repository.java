package pablo.ad.mychatclient;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import pablo.ad.mychatclient.room.Mensajes;
import pablo.ad.mychatclient.room.MensajesDB;
import pablo.ad.mychatclient.room.MensajesDao;

public class Repository {
    private MensajesDao mensajesDao;
    private Context context;
    private MensajesDB mensajesDB;

    public Repository(Context context) {
        mensajesDB = MensajesDB.getDb(context);
        this.context = context;
        mensajesDao = mensajesDB.getMensajesDao();
    }

    public void insertaMsg(Mensajes msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mensajesDao.insert(msg);
            }
        }).start();

    }

    public LiveData<List<Mensajes>> leeHistorial(){

        return mensajesDao.getAllMensajes();
    }

}
