package pjdm.pjdm2021.mysugarlevel;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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


public class AndGiornFragment extends Fragment implements View.OnClickListener{

    private ImageButton btAndGiornBack;
    private Button btCalendarioAG;
    private DatePickerDialog datePickerDialog;
    private GraphView graphAG;

    public AndGiornFragment() {// Required empty public constructor
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        graphAG = view.findViewById(R.id.graphAG);
        btAndGiornBack = view.findViewById(R.id.btAndGiornBack);
        btAndGiornBack.setOnClickListener(this);
        btCalendarioAG = view.findViewById(R.id.btCalendarioAG);
        btCalendarioAG.setOnClickListener(this);

        /*Imposto la data corrente sul pulsante del calendario*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        String currentDateandTime = sdf.format(new Date());
        btCalendarioAG.setText(currentDateandTime);
        initDatePicker();

        /* Disegno il grafico */
        drawGraph();
    }

    public void drawGraph(){
        /* Cancella il vecchio grafico */
        graphAG.removeAllSeries();
        /* Ridisegno il nuovo grafico prendendo i nuovi punti */
        graphAG.addSeries(getSerials());
    }

    private LineGraphSeries<DataPoint> getSerials() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        int id_utente = 1;

        /* Splitto la data in yyyy/MM/dd per fare la query nel DB */
        String oldDate = String.valueOf(btCalendarioAG.getText());
        String[] split = oldDate.split("/");
        String newDate = split[2] + "/" + split[1] + "/" + split[0];

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = getResources().getString(R.string.urlLoadValoreByUnaData) + "?id_utente="+ id_utente + "&data="+newDate;

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
                    graphAG.getViewport().setXAxisBoundsManual(true);
                    graphAG.getViewport().setMinX(series.getLowestValueX());
                    graphAG.getViewport().setMaxX(series.getHighestValueX());

                    /* Setto l'asse delle y (valore più alto di glicemia) in base al valore
                     * massimo che prendo dalla richiesta e ci aggiungo 40 (numero arbitrario)*/
                    graphAG.getViewport().setYAxisBoundsManual(true);
                    graphAG.getViewport().setMinY(0);
                    graphAG.getViewport().setMaxY(series.getHighestValueY()+40);

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

        /* Animazione del grafico */
        series.setAnimated(true);

        /* Quando clicco su un punto, esce un toast con i valori glicemici e orario del punto scelto */
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


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;String date = "";
                if(month<10){
                    date = dayOfMonth + "/0" + month + "/" + year;
                }
                else {
                    date = dayOfMonth + "/" + month + "/" + year;
                }
                btCalendarioAG.setText(date);

                /* Ridisegno dopo aver cambiato data al calendario */
                drawGraph();
            }
        };

        /* Inizialmente, imposto la data del calendario a quella corrente */
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener,year,month,day);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_and_giorn, container, false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btAndGiornBack){
            NavHostFragment.findNavController(this).navigate(R.id.action_andGiornFragment_to_menuFragment);
        }
        if(v.getId() == R.id.btCalendarioAG){
            datePickerDialog.show();
        }
    }
}