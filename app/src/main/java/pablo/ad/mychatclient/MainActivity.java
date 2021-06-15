package pablo.ad.mychatclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import pablo.ad.mychatclient.room.Mensajes;

public class MainActivity extends AppCompatActivity {

    public static String username = "PabloEnAndroid";

    public Socket cliente;
    public DataInputStream flujoE;
    public DataOutputStream flujoS;

    private Button btSend;
    private TextInputEditText campoTexto;
    private static TextView pantalla;

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btSend = findViewById(R.id.button);
        campoTexto = findViewById(R.id.textInputEditText);
        pantalla = findViewById(R.id.tvPantalla);

        repository = new Repository(getApplicationContext());

        pantalla.setText("");
        //rellenar de mensajes
        repository.leeHistorial().observe(this, new Observer<List<Mensajes>>() {
            @Override
            public void onChanged(List<Mensajes> mensajes) {
                pantalla.setText("");
                for (Mensajes msg: mensajes) {
                    nuevoMensaje(msg.getMsg());
                }
            }
        });




        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarTexto(campoTexto.getText().toString());
                campoTexto.setText("");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cliente = new Socket("10.0.2.2",5000);
                    flujoE = new DataInputStream(cliente.getInputStream());
                    flujoS = new DataOutputStream(cliente.getOutputStream());


                    String text;
                    while(true){
                        try {
                            text = flujoE.readUTF();
                            if(text.contains(":")){
                                repository.insertaMsg(new Mensajes(text));
                            }
                            Log.v("xyz", text);
                        } catch (IOException ex) {
                            Log.v("xyz", "ERROR leyendo mensajes");
                        }
                    }

                } catch (IOException ex) {
                    Log.v("xyz", "No conectado ERROR: " + ex.toString());
                }
            }
        }).start();

    }


    private void enviarTexto(String msg){
        if(msg.compareTo("") != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        flujoS.writeUTF(username + ": " + msg);
                    } catch (IOException ex) {
                        Log.v("xyz", "ERROR enviando mensajes");
                    }
                }
            }).start();
        }
    }

    private static void nuevoMensaje(String text){
        pantalla.append("\n " + text);
        /*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pantalla.append("\n " + text);

            }
        });*/

    }


}