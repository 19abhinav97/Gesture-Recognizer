package ca.uwaterloo.cs349;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class LibraryFragment extends Fragment implements LibraryAdapter.Click_listen {

    private SharedViewModel mViewModel;
    private MySharedPreference sharedPreference;
    private RecyclerView rec_view;
    private ArrayList<GestureItem> list_gesture_item;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedPreference = new MySharedPreference(requireActivity());
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        rec_view = root.findViewById(R.id.recycler_view);
        setList();
        return root;
    }

    private void setList() {
        list_gesture_item = sharedPreference.getLibraries();
        LibraryAdapter adapter = new LibraryAdapter(list_gesture_item, this);
        rec_view.setHasFixedSize(true);
        rec_view.setAdapter(adapter);
    }

    @Override
    public void onClickReplace(int position) {
        String name = list_gesture_item.get(position).getName();
        Bundle args = new Bundle();
        args.putString("replaceName", name);
        args.putInt("replaceIndex", position);
        findNavController(this).navigate(R.id.action_navigation_library_to_navigation_replace_gesture, args);
    }

    @Override
    public void onClickDelete(int position) {
        list_gesture_item.remove(position);
        rec_view.getAdapter().notifyDataSetChanged();
        sharedPreference.setLibrary(list_gesture_item);
    }
}