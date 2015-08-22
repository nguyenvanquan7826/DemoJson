package nguyenvanquan7826.com.demojson;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.RecyclerViewHolder> {

    private List<Lover> list;
    private Context context;

    public ItemAdapter(Context context, List<Lover> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * connect to item view
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    /**
     * set data for item
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        Lover lover = list.get(position);
        viewHolder.tvName.setText(lover.getName());
        viewHolder.tvPhone.setText(lover.getPhone());
        viewHolder.tvBeginDate.setText(lover.getBeginDate());
        viewHolder.tvEndDate.setText(lover.getEndDate());
    }

    /**
     * ViewHolder for item view of list
     */

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            OnClickListener {

        public CardView container;
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvBeginDate;
        public TextView tvEndDate;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            container = (CardView) itemView.findViewById(R.id.item_container);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvBeginDate = (TextView) itemView.findViewById(R.id.tv_begin_date);
            tvEndDate = (TextView) itemView.findViewById(R.id.tv_end_date);

            container.setOnClickListener(this);
        }

        // click item then display note
        @Override
        public void onClick(View v) {
            Var.showToast(context, list.get(getAdapterPosition()).getName());
        }
    }
}
