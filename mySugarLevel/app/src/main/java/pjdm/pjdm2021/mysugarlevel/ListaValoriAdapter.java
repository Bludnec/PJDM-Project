package pjdm.pjdm2021.mysugarlevel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaValoriAdapter extends RecyclerView.Adapter<ListaValoriAdapter.ListaSpesaViewHolder> {
    private ArrayList<ElementoLista> lista;
    private Context context;
    private LayoutInflater inflater;

    public ListaValoriAdapter(Context context) {
        this.context = context;
        lista = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void addToList(ElementoLista elementoLista){
        lista.add(elementoLista);
    }

    public void rimuoviTutto(){
        lista.clear();
    }
    public void remove(int position){
        lista.remove(lista.get(position));
        notifyItemRemoved(position);
    }

    public String getOrarioElemento(int posizione){
        return lista.get(posizione).getOrario();
    }

    @NonNull
    @Override
    public ListaSpesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = inflater.inflate(R.layout.simple_row,parent,false);
        ListaSpesaViewHolder vh = new ListaSpesaViewHolder(row);
        return vh;
    }

    /* Grandezza della lista */
    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ListaSpesaViewHolder holder, int position) {
        /* Imposto i valori "valore" e "orario" nelle card */
        String valore = String.valueOf(lista.get(position).getValore());
        holder.tvValore.setText(valore);
        String orario = String.valueOf(lista.get(position).getOrario());
        holder.tvOrario.setText(orario);

        /* Controllo se insulina o cibo è true, cambio immagine con checkbox on/off */
        Boolean insul = lista.get(position).isInsulina();
        Boolean cib = lista.get(position).isCibo();
        if(insul) { holder.ivInsulina.setImageResource(android.R.drawable.checkbox_on_background );}
        if(cib){ holder.ivCibo.setImageResource(android.R.drawable.checkbox_on_background );}
    }

    class ListaSpesaViewHolder extends RecyclerView.ViewHolder {
        TextView tvValore;
        TextView tvOrario;
        ImageView ivInsulina;
        ImageView ivCibo;

        public ListaSpesaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvValore = itemView.findViewById(R.id.tvValore);
            tvOrario = itemView.findViewById(R.id.tvOrario);
            ivInsulina = itemView.findViewById(R.id.ivInsulina);
            ivCibo = itemView.findViewById(R.id.ivCibo);
        }
    }
}
