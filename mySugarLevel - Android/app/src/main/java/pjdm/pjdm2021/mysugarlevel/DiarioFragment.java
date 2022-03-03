package pjdm.pjdm2021.mysugarlevel;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class DiarioFragment extends Fragment implements View.OnClickListener{
    private Button btCalendario;
    private DatePickerDialog datePickerDialog;
    private ImageButton btDiarioBack;
    private RecyclerView rvLista;
    private ListaValoriAdapter adapter;
    private String dataDB;

    public DiarioFragment() {// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btDiarioBack = view.findViewById(R.id.btDiarioBack);
        btDiarioBack.setOnClickListener(this);

        btCalendario = view.findViewById(R.id.btCalendario);
        btCalendario.setOnClickListener(this);

        /*Imposto la data corrente sul pulsante*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());
        String dateFormatDB =  sdfDB.format(new Date());
        btCalendario.setText(currentDateandTime);

        initDatePicker();

        rvLista = view.findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListaValoriAdapter(getActivity());
        rvLista.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                String currentString = (String) btCalendario.getText();

                /* Prendo l'orario dell'elemento selezionato */
                String el = adapter.getOrarioElemento(pos);

                int id_utente = 1;

                swip(id_utente,pos,currentString,el);
            }
        });

        itemTouchHelper.attachToRecyclerView(rvLista);

        /* Prendere dati dal DB e mostrarli all'apertura del fragment*/
        int id_utente = 1;
        letturaDB(id_utente, dateFormatDB);
    }

    public void letturaDB(int id_utente, String dataDB){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = getResources().getString(R.string.urlLoadValoreByUnaData) + "?id_utente="+ id_utente + "&data="+dataDB;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener <JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray ja = response;
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jb = ja.getJSONObject(j);
                        String orario = jb.getString("orario");
                        int valore = jb.getInt("valore");
                        int insulina = jb.getInt("insulina");
                        int cibo = jb.getInt("cibo");

                        boolean ins = false;
                        boolean cib = false;
                        if(insulina == 1){
                            ins = true;
                        }
                        if(cibo == 1){
                            cib = true;
                        }

                        ElementoLista elementoLista = new ElementoLista(valore,orario,ins,cib);
                        adapter.addToList(elementoLista);
                    }
                    adapter.notifyDataSetChanged();
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
                btCalendario.setText(date);

                dataDB = year + "/" + month + "/" +dayOfMonth;

                adapter.rimuoviTutto();
                letturaDB(1,dataDB);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener,year,month,day);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btCalendario){
            datePickerDialog.show();
        }
        if(v.getId() == R.id.btDiarioBack){
            NavHostFragment.findNavController(this).navigate(R.id.action_diarioFragment_to_menuFragment);
        }
    }

    /* Funzione per la cancellazione di un valore con lo swipe verso destra*/
    public void swip(int id_utente,int pos,String currentString,String el){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        /* Splitto la stringa per prendere i valori del giorno,mese,anno per la richiesta */
        String[] separated = currentString.split("/");

        String url = getResources().getString(R.string.urlValore) + "?id_utente="+ id_utente + "&data="+  separated[2] + "/" + separated[1] + "/" + separated[0]+ "&orario=" + el;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast toast = Toast.makeText(getActivity(), "Cancellazione andata a buon fine.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getActivity(), "Cancellazione andata a buon fine.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjRequest);
        adapter.remove(pos);
        adapter.notifyDataSetChanged();
    }

}