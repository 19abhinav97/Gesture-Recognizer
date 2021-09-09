package ca.uwaterloo.cs349;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import ca.uwaterloo.cs349.view.DrawGestureView;

public class AdditionFragment extends Fragment implements View.OnClickListener {
    private SharedViewModel mViewModel;
    private MySharedPreference mySharedPreference;
    private DrawGestureView drawing_panel;
    private EditText gesture_name_edit_text;
    private Button save_gesture_button;
    private ArrayList<GestureItem> libList;
    private String replaceName = null;
    private int replaceIndex = -100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        mySharedPreference = new MySharedPreference(requireActivity());
        initAlLibrary();
        Bundle arg = getArguments();
        if (arg != null) {
            replaceName = arg.getString("replaceName", null);
            replaceIndex = arg.getInt("replaceIndex", -100);
        }

        View root = inflater.inflate(R.layout.fragment_addition, container, false);
        drawing_panel = root.findViewById(R.id.drawing_panel);
        gesture_name_edit_text = root.findViewById(R.id.gesture_name_edit_text);
        save_gesture_button = root.findViewById(R.id.save_gesture_button);
        save_gesture_button.setOnClickListener(this);

        if (isForReplaceGesture()) {
            gesture_name_edit_text.setText(replaceName);
            gesture_name_edit_text.setEnabled(false);
        }


        return root;
    }

    private boolean isForReplaceGesture() {
        return replaceName != null && replaceIndex != -100;
    }

    private void initAlLibrary() {
        libList = mySharedPreference.getLibraries();
    }


    @Override
    public void onClick(View view) {
        initAlLibrary();
        String invalidMsg = hasInvalidFieldMsg();
        if (invalidMsg != null)
            Toast.makeText(getActivity(), invalidMsg, Toast.LENGTH_SHORT).show();
        else {
            ArrayList<PointF> originalShapePointArr = drawing_panel.getOriginalShape();
            ArrayList<PointF> pointArr = drawing_panel.getNPoints();
            String strPoints = GestureItem.getPathString(pointArr);
            String strOriginalShapePoints = GestureItem.getPathString(originalShapePointArr);
            String strGestureName = gesture_name_edit_text.getText().toString().trim();
            GestureItem gestureItem = new GestureItem(strGestureName, strPoints, strOriginalShapePoints, 0f);
            if (isForReplaceGesture()) {
                libList.set(replaceIndex, gestureItem);
            } else {
                libList.add(gestureItem);
            }

            mySharedPreference.setLibrary(libList);
            Toast.makeText(getActivity(), "Gesture saved successfully", Toast.LENGTH_SHORT).show();

            // Clear
            drawing_panel.clear();
            gesture_name_edit_text.setText("");
            if (isForReplaceGesture()) {
                requireActivity().onBackPressed();
            }
        }
    }


    private String hasInvalidFieldMsg() {
        String strGestureName = gesture_name_edit_text.getText().toString().trim();
        if (strGestureName.isEmpty()) return "Gesture name cannot be empty";
        else if (!drawing_panel.isValidGesture()) return "Please enter proper Gesture";

        if (!isForReplaceGesture()) {
            for (GestureItem gi : libList) {
                if (gi.getName().equalsIgnoreCase(strGestureName))
                    return "This gesture name already exists in the library, please try out with different name";
            }
        }
        return null;
    }
}