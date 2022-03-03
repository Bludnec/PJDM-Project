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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class ValMedioFragment extends Fragment implements View.OnClickListener{

    private ImageButton btValMedioBack;
    private Button btCalendarioVM;
    private DatePickerDialog datePickerDialog;
    private TextView tvMattina;
    private TextView tvPomeriggio;
    private TextView tvNotte;

    private int countMattina;
    private int countM;
    private int countPomeriggio;
    private int countP;
    private int countNotte;
    private int countN;

    public ValMedioFragment() {// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btValMedioBack = view.findViewById(R.id.btValMedioBack);
        btValMedioBack.setOnClickListener(this);

        tvMattina = view.findViewById(R.id.tvMattina);
        tvPomeriggio = view.findViewById(R.id.tvPomeriggio);
        tvNotte = view.findViewById(R.id.tvNotte);

        btCalendarioVM = view.findViewById(R.id.btCalendarioVM);
        btCalendarioVM.setOnClickListener(this);

        /*Imposto la data corrente sul pulsante*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        String currentDateandTime = sdf.format(new Date());
        btCalendarioVM.setText(currentDateandTime);
        initDatePicker();

        /* Calcolo le medie in base alla data corrente */
        calcolo();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = "";
                if(month<10){
                    date = dayOfMonth + "/0" + month + "/" + year;
                }
                else {
                    date = dayOfMonth + "/" + month + "/" + year;
                }
                btCalendarioVM.setText(date);

                /* Ricalcolo le medie in base al giorno selezionato */
                calcolo();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_val_medio, container, false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btValMedioBack){
            NavHostFragment.findNavController(this).navigate(R.id.action_valMedioFragment_to_menuFragment);
        }
        if(v.getId() == R.id.btCalendarioVM){
            datePickerDialog.show();
        }
    }

    public void calcolo() {
        countMattina = 0;
        countM= 0;
        countPomeriggio= 0;
        countP= 0;
        countNotte= 0;
        countN= 0;
        int id_utente = 1;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String currentString = (String) btCalendarioVM.getText();
        String[] separated = currentString.split("/");

        String url = getResources().getString(R.string.urlLoadValoreByUnaData) + "?id_utente=" + id_utente + "&data=" + separated[2] + "-" + separated[1] + "-" + separated[0];

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray ja = response;
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jb = ja.getJSONObject(j);
                        int valore = jb.getInt("valore");
                        String orario = jb.getString("orario");
                        String[] orarioSep = orario.split(":");
                        if(Integer.parseInt(orarioSep[0]) <= 11 ){
                            countMattina += jb.getInt("valore");
                            countM++;
                        }
                        if(Integer.parseInt(orarioSep[0]) > 11 && Integer.parseInt(orarioSep[0])  < 18  ){
                            countPomeriggio += jb.getInt("valore");
                            countP++;
                        }
                        if(Integer.parseInt(orarioSep[0]) >= 18) {
                            countNotte += jb.getInt("valore");
                            countN++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(countM == 0) {
                    tvMattina.setText("Non ci sono abbastanza valori.");
                } else{
                    countMattina = countMattina / countM;
                    tvMattina.setText(String.valueOf(countMattina));
                }
                if(countP == 0) {
                    tvPomeriggio.setText("Non ci sono abbastanza valori.");
                } else{
                    countPomeriggio = countPomeriggio / countP;
                    tvPomeriggio.setText(String.valueOf(countPomeriggio));
                }
                if(countN == 0) {
                    tvNotte.setText("Non ci sono abbastanza valori.");
                } else{
                    countNotte = countNotte / countN;
                    tvNotte.setText(String.valueOf(countNotte));
                }
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}