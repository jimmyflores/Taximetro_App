package ec.edu.upse.taximetro_app.utiles;

import java.util.List;

import ec.edu.upse.taximetro_app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListViewAdapterConsulta  extends ArrayAdapter<ItemConsulta>{

	Context context;
	
	public CustomListViewAdapterConsulta(Context context, int resourceId, List<ItemConsulta> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    
    private class ViewHolder {
        TextView txtOrigen;
        TextView txtDestino;
        TextView txtDistancia;  
        TextView txtValorCC;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ItemConsulta item = getItem(position);
 // aqui inflamos el layout normal de un adapter es decir lo estmaos personalizando
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	// indicar con que layout vamos a inflar el listview
            convertView = mInflater.inflate(R.layout.activity_item__result, null);
            
            holder = new ViewHolder();
            holder.txtOrigen = (TextView) convertView.findViewById(R.id.textViewOrigenVista);
            holder.txtDestino = (TextView) convertView.findViewById(R.id.textViewDestinoVista);
            holder.txtDistancia = (TextView) convertView.findViewById(R.id.textViewDistanciaVista);
            holder.txtValorCC = (TextView) convertView.findViewById(R.id.textViewValorCCVista);
            
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtOrigen.setText(item.getOrigen());
        holder.txtDestino.setText(item.getDestino());
        holder.txtDistancia.setText(Double.toString(item.getDistancia())+" Km");
        holder.txtValorCC.setText("$ "+Double.toString(item.getValor()));
        
 
        return convertView;
    }
 

}
