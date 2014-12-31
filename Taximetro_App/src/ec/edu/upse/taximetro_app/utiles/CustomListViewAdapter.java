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


public class CustomListViewAdapter extends ArrayAdapter<ItemTablita> {
	Context context;
	 
    public CustomListViewAdapter(Context context, int resourceId, List<ItemTablita> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    
    private class ViewHolder {
        
        TextView txtOrigen;
        TextView txtDestino;
        TextView txtCosto;      
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ItemTablita item = getItem(position);
 // aqui inflamos el layout normal de un adapter es decir lo estmaos personalizando
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	// indicar con que layout vamos a inflar el listview
            convertView = mInflater.inflate(R.layout.activity_item_tabla, null);
            
            holder = new ViewHolder();
            holder.txtOrigen = (TextView) convertView.findViewById(R.id.textViewOrig);
            holder.txtDestino = (TextView) convertView.findViewById(R.id.textViewDest);
            holder.txtCosto = (TextView) convertView.findViewById(R.id.textViewCost);
            
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtOrigen.setText(item.getOrigen());
        holder.txtDestino.setText(item.getDestino());
        holder.txtCosto.setText(Double.toString(item.getCosto()));
        
 
        return convertView;
    }
 
    
}

