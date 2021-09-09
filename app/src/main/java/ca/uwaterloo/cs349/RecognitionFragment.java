package ca.uwaterloo.cs349;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uwaterloo.cs349.view.DrawGestureView;

public class RecognitionFragment extends Fragment implements DrawGestureView.IStrokeListener {

    private SharedViewModel mViewModel;
    private MySharedPreference mySharedPreference;
    private DrawGestureView drawing_panel;
    private RecyclerView recycler_view;
    private TextView no_pattern_saved_text;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mySharedPreference = new MySharedPreference(requireActivity());
        View root = inflater.inflate(R.layout.fragment_recognition, container, false);
        drawing_panel = root.findViewById(R.id.drawing_panel);
        recycler_view = root.findViewById(R.id.recycler_view);
        no_pattern_saved_text = root.findViewById(R.id.no_pattern_saved_text);

        if (mySharedPreference.getLibraries().isEmpty()) {
            no_pattern_saved_text.setVisibility(View.VISIBLE);
        }
        else {
            no_pattern_saved_text.setVisibility(View.GONE);
        }

        drawing_panel.setTouchEventListener(this);
        return root;
    }

    private void setList(List<GestureItem> list) {
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }

        RecognizedGestureAdapter adapter = new RecognizedGestureAdapter(list, requireContext());
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adapter);
    }

    private ArrayList<GestureItem> findPatternFromLibrary(ArrayList<PointF> sP) {
        ArrayList<GestureItem> arrayList = mySharedPreference.getLibraries();

        for (GestureItem gi : arrayList) {
            ArrayList<PointF> tP = gi.getPathPointArray();

            float distance = 0;
            for (int i = 0; i < tP.size(); i++) {
                try {
                    distance += Math.sqrt((Math.pow((sP.get(i).x - tP.get(i).x), 2) + Math.pow((sP.get(i).y - tP.get(i).y), 2)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            float di = distance / 128f;
            gi.setDi(di);
        }
        return arrayList;
    }

    @Override
    public void end(ArrayList<PointF> xyList) {
        ArrayList<PointF> pointList = drawing_panel.getNPoints();
        ArrayList<GestureItem> resultList = findPatternFromLibrary(pointList);

        Collections.sort(resultList);
        setList(resultList);
    }

    @Override
    public void start() {
        setList(new ArrayList<GestureItem>());
    }
}