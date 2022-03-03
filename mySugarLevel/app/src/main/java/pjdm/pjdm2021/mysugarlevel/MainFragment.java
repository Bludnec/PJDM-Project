package pjdm.pjdm2021.mysugarlevel;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class MainFragment extends Fragment implements View.OnClickListener{

    private ImageButton btSettings;
    private ImageButton btMenu;
    private ImageButton btAdd;
    private GraphView graph;
    private Calendar calendar;
    private int cibo;
    private int insulina;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btSettings = view.findViewById(R.id.btSettings);
        btMenu = view.findViewById(R.id.btMenu);
        btAdd = view.findViewById(R.id.btAdd);
        btSettings.setOnClickListener(this);
        btMenu.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        graph = view.findViewById(R.id.graph);

        /* Disegno il grafico */
        drawGraph();
    }

    public void drawGraph(){
        graph.removeAllSeries();
        /* Prendo i valori dal DB e li disegno*/
        graph.addSeries(getSerials());
    }
    private LineGraphSeries<DataPoint> getSerials() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        int id_utente = 1;
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(calendar.getTime());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = getResources().getString(R.string.urlLoadValoreByUnaData) + "?id_utente="+ id_utente + "&data="+date;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener <JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray ja = response;
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jb = ja.getJSONObject(j);
                        int valore = jb.getInt("valore");
                        String orario = jb.getString("orario");

                        /* Converto l'orario in Double per poterlo inserire nella funzione
                         * di creazione dei DataPoint */
                        String[] orarioSplit = orario.split(":");
                        String newOrario = orarioSplit[0] + "." + orarioSplit[1];
                        Double doubOrario = Double.parseDouble(newOrario);

                        /* Appendo, uno alla volta, i punti nella series del GraphView */
                        series.appendData(new DataPoint(doubOrario,valore), false, 15);
                    }

                    /* Setto l'asse delle x con la quantità di valori da mettere sul grafico
                    * (in base alla quantità di valori presi durante la richiesta*/
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(series.getLowestValueX());
                    graph.getViewport().setMaxX(series.getHighestValueX());

                    /* Setto l'asse delle y (valore più alto di glicemia) in base al valore
                    * massimo che prendo dalla richiesta e ci aggiungo 40 (numero arbitrario)*/
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(series.getHighestValueY()+40);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);

        /* Rendo animato il grafico all'apertura*/
        series.setAnimated(true);

        /* Quando clicco su un punto, esce un toast con i valori glicemici e orario del punto scelto*/
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Orario: " + dataPoint.getX() + " Valore glicemico: "+dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });

        /* Rendo visibile i punti degli inserimenti */
        series.setDrawDataPoints(true);
        return series;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btSettings){
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.btMenu){
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_menuFragment);
        }

        if(view.getId() == R.id.btAdd){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Aggiungi valori");

            /* Genero l'albero delle view | Radice = rootView */
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.input_dialog,view.findViewById(android.R.id.content),false);

            EditText etValore = rootView.findViewById(R.id.etValore);
            EditText etOra = rootView.findViewById(R.id.etOra);
            CheckBox cbInsulina = rootView.findViewById(R.id.cbInsulina);
            CheckBox cbCibo = rootView.findViewById(R.id.cbCibo);

            builder.setView(rootView);

            /* Dialog per inserire nuovi valori */
            builder.setPositiveButton("Salva", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    /* Inserire i valodi nel DB*/
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    String data = sdf.format(new Date());
                    String valore = String.valueOf(etValore.getText());
                    String orario = String.valueOf(etOra.getText());

                    /* Controllo completamento dei campi valore e orario */
                    if(valore.isEmpty()){
                        Toast toast = Toast.makeText(getContext(), "Errore inserimento: valore non inserito.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    if(orario.isEmpty()){
                        Toast toast = Toast.makeText(getContext(), "Errore inserimento: orario non inserito.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    /* Controllo inserimento corretto dell'orario */
                    try {
                        String control = String.valueOf(etOra.getText());
                        String[] listOra = control.split(":");
                        if(Integer.parseInt(listOra[0]) < 0 || Integer.parseInt(listOra[0]) > 24 || Integer.parseInt(listOra[1]) < 0 || Integer.parseInt(listOra[1]) > 59 || listOra[1].length() != 2 ){
                            Toast toast = Toast.makeText(getContext(), "Errore inserimento: orario non valido.", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        if(listOra[0].length() == 1){
                            /* Ex. Da 9:30 -> 09:30*/
                            orario = "0" + orario;
                        }
                    }catch (ArrayIndexOutOfBoundsException e){
                        Toast toast = Toast.makeText(getContext(), "Errore inserimento: formato orario non valido.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    if (cbInsulina.isChecked()) {
                        insulina = 1;
                    }
                    if (cbCibo.isChecked()) {
                        cibo = 1;
                    }

                    int id_utente = 1;
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    String url = getResources().getString(R.string.urlValore) + "?id_utente=" + id_utente + "&data=" + data + "&orario=" + orario + "&valore=" + valore + "&insulina=" + insulina + "&cibo=" + cibo;

                    JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getActivity(), "Inserimento andato a buon fine.", Toast.LENGTH_LONG);
                                }
                                }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Inserimento non andato a buon fine.", Toast.LENGTH_LONG);
                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(jsonObjRequest);

                    dialog.dismiss();
                    Toast toast = Toast.makeText(getActivity(), "Inserimento andato a buon fine.", Toast.LENGTH_LONG);
                    toast.show();
                    drawGraph();
                }
            }).setNegativeButton("Cancella", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }
}