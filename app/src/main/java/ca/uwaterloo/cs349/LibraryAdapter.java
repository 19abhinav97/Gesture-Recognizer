package ca.uwaterloo.cs349;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ca.uwaterloo.cs349.view.GestureView;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private final ArrayList<GestureItem> list_gi;
    private final Click_listen clicklisten;

    public LibraryAdapter(ArrayList<GestureItem> list_gi, Click_listen clicklisten) {
        this.list_gi = list_gi;
        this.clicklisten = clicklisten;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_library, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GestureItem gestureItem = list_gi.get(position);

        holder.name.setText(gestureItem.getName());
        holder.image.setPath(gestureItem);
        holder.replace.setTag(position);
        holder.delete.setTag(position);
    }


    @Override
    public int getItemCount() {
        return list_gi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public GestureView image;
        public TextView name;
        public ImageButton replace, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name_text);
            this.replace = itemView.findViewById(R.id.replace_image_button);
            this.delete = itemView.findViewById(R.id.delete_image_button);
            this.replace.setOnClickListener(this);
            this.delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.replace_image_button:
                    int posReplace = (int) view.getTag();
                    LibraryAdapter.this.clicklisten.onClickReplace(posReplace);
                    break;
                case R.id.delete_image_button:
                    int posDelete = (int) view.getTag();
                    LibraryAdapter.this.clicklisten.onClickDelete(posDelete);
                    break;
            }
        }
    }

    interface Click_listen {
        void onClickReplace(int position);

        void onClickDelete(int position);
    }
}  