package ca.uwaterloo.cs349;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.uwaterloo.cs349.view.GestureView;

public class RecognizedGestureAdapter extends RecyclerView.Adapter<RecognizedGestureAdapter.ViewHolder> {
    private List<GestureItem> gestureItemList;
    private Context context;

    public RecognizedGestureAdapter(List<GestureItem> gestureItemList, Context context) {
        this.gestureItemList = gestureItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_recognized_gestures, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GestureItem gestureItem = gestureItemList.get(position);
        holder.name.setText(gestureItem.getName());
        holder.di.setText("" + gestureItem.getDi());
        holder.image.setPath(gestureItem);

        if (position == 0) {
            holder.constraint.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }
        else {
            holder.constraint.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
}


    @Override
    public int getItemCount() {
        return gestureItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public GestureView image;
        public TextView name;
        public TextView di;
        public ConstraintLayout constraint;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name_text);
            this.di = itemView.findViewById(R.id.di_text);
            this.constraint = itemView.findViewById(R.id.constraint);
        }
    }
}  