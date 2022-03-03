package pjdm.pjdm2021.mysugarlevel;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventiFragment extends Fragment implements View.OnClickListener{

    private ImageButton btBack;
    private TextView tvBasso;
    private TextView tvAlto;
    private int contatoreAlto;
    private int contatoreBasso;

    public EventiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btBack = view.findViewById(R.id.btEventiBack);
        tvBasso = view.findViewById(R.id.tvNumeroBasso);
        tvAlto = view.findViewById(R.id.tvNumeroAlto);
        btBack.setOnClickListener(this);

        /* Faccio il calcolo degli eventi di glucosio alto/basso per poi inserire i risultati nelle TextView */
        calcolo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eventi, container, false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btEventiBack) {
            NavHostFragment.findNavController(this).navigate(R.id.action_eventiFragment_to_menuFragment);
        }
    }

    public void calcolo(){
        contatoreAlto = 0;
        contatoreBasso = 0;

        int id_utente = 1;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
        Calendar currentCal = Calendar.getInstance();
        String currentdate = dateFormat.format(currentCal.getTime());
        /* Prendo in considerazione i 7 giorni prima dalla data attuale */
        currentCal.add(Calendar.DATE, -7);
        String toDate = dateFormat.format(currentCal.getTime());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int valoreAlto = Integer.parseInt(preferences.getString("valore_alto","0"));
        int valoreBasso = Integer.parseInt(preferences.getString("valore_basso","0"));
        Log.d("SERGIO", String.valueOf(valoreAlto));

        String url = getResources().getString(R.string.urlLoadValoreByDate) + "?id_utente=" + id_utente + "&data1="+ toDate + "&data2="+currentdate;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener <JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray ja = response;
                    for (int j = 0; j < ja.length(); j++) {
                        JSONObject jb = ja.getJSONObject(j);

                        int valore = jb.getInt("valore");

                        if(valore> valoreAlto){
                            contatoreAlto++;
                        }
                        if(valore<valoreBasso){
                            contatoreBasso++;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tvBasso.setText(String.valueOf(contatoreBasso));
                tvAlto.setText(String.valueOf(contatoreAlto));
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