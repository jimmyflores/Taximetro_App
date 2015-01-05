package ec.edu.upse.taximetro_app.utiles;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ec.edu.upse.taximetro_app.R;

public class CustomListCarrera extends ArrayAdapter<ItemCarrera> {
	Context context;
	 
    public CustomListCarrera(Context context, int resourceId, List<ItemCarrera> items) {
        super(context, resourceId, items);
        this.context = context;
    }
    
    private class ViewHolder {
        
        TextView txtOrigen;
        TextView txtDestino;
        TextView txtFecha;      
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ItemCarrera item = getItem(position);
 // aqui inflamos el layout normal de un adapter es decir lo estmaos personalizando
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	// indicar con que layout vamos a inflar el listview
            convertView = mInflater.inflate(R.layout.activity_item_carrera, null);
            
            holder = new ViewHolder();
            holder.txtOrigen = (TextView) convertView.findViewById(R.id.textViewOri2);
            holder.txtDestino = (TextView) convertView.findViewById(R.id.textViewDes2);
            holder.txtFecha = (TextView) convertView.findViewById(R.id.textViewFe2);
            
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.txtOrigen.setText(item.getOrigen());
        holder.txtDestino.setText(item.getDestino());
        holder.txtFecha.setText(item.getFecha());
        
 
        return convertView;
    }
}
