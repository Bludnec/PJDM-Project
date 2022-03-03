package pjdm.pjdm2021.mysugarlevel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuFragment extends Fragment implements View.OnClickListener{

    private ImageButton btBack;
    private Button btDiario;
    private Button btValMedio;
    private Button btAndGiorn;
    private Button btEventi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btBack = view.findViewById(R.id.btMenuBack);
        btDiario = view.findViewById(R.id.btDiario);
        btValMedio = view.findViewById(R.id.btValMedio);
        btAndGiorn = view.findViewById(R.id.btAndGiorn);
        btEventi = view.findViewById(R.id.btEventi);

        btBack.setOnClickListener(this);
        btDiario.setOnClickListener(this);
        btValMedio.setOnClickListener(this);
        btAndGiorn.setOnClickListener(this);
        btEventi.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btDiario){
            NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_diarioFragment);
        }
        if(v.getId() == R.id.btValMedio){
            NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_valMedioFragment);
        }
        if(v.getId() == R.id.btAndGiorn){
            NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_andGiornFragment);
        }
        if(v.getId() == R.id.btEventi){
            NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_eventiFragment);
        }
        if(v.getId() == R.id.btMenuBack){
            NavHostFragment.findNavController(this).navigate(R.id.action_menuFragment_to_mainFragment);
        }
    }
}