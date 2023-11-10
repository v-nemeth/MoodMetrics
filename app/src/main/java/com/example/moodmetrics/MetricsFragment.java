package com.example.moodmetrics;

import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.widget.Switch;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetricsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetricsFragment extends Fragment {

    TextView tResult, tInter;
    EditText eWeight, eHeight;
    Button bCalculate, bSubmit;
    Switch sw;

    double bmi = 0;

    private String username;
    public MetricsFragment(String u) {
        this.username = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetricsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetricsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetricsFragment newInstance(String param1, String param2) {
        MetricsFragment fragment = new MetricsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metrics, container, false);

        eWeight = view.findViewById(R.id.weightInput);
        eHeight = view.findViewById(R.id.heightInput);
        bCalculate = view.findViewById(R.id.calculateButton);
        bSubmit = view.findViewById(R.id.submit);
        tResult = view.findViewById(R.id.bmiResult);
        tInter = view.findViewById(R.id.bmiInterpretation);

        sw = view.findViewById(R.id.unitToggle);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (isChecked) {
                    eWeight.setHint("Weight in Pound (lb)");
                    eHeight.setHint("Height in Inch (in)");

                } else {
                    eWeight.setHint("Weight (kg)");
                    eHeight.setHint("Height (cm)");

                }
            }
        });


        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if (sw.isChecked() == false) {
                        double number1 = Double.parseDouble(eWeight.getText().toString());
                        double number2 = Double.parseDouble(eHeight.getText().toString());
                        bmi = (number1 / (number2 * number2)) * 10000;
                        String sumBMI = String.format("%.2f", bmi);
                        tResult.setText(String.valueOf("Your BMI: " + sumBMI));

                        String inPre = Interpretation(bmi);
                        tInter.setText("Interpretation: " + inPre);

                    } else if (sw.isChecked() == true){
                        double num1 = Double.parseDouble(eWeight.getText().toString());
                        double num2 = Double.parseDouble(eHeight.getText().toString());
                        bmi = (num1 / (num2 * num2)) * 703;
                        String sumBMI = String.format("%.2f", bmi);
                        tResult.setText(String.valueOf("Your BMI: " + sumBMI));

                        String inPre = Interpretation(bmi);
                        tInter.setText("Interpretation: " + inPre);
                    }
                    bSubmit.setEnabled(true);
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "Please Enter A Value", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        bSubmit.setOnClickListener(v -> {
            DBHelper dbHelper = DBHelper.getInstance(getContext());
            dbHelper.addBmiEntryToDB(username, bmi, new Date());
            Toast.makeText(getActivity(), "Your BMI Result is Saved", Toast.LENGTH_SHORT).show();
            bSubmit.setEnabled(false);
        });

        return view;



    }

    public static String Interpretation(double BMI_S) {
        if (BMI_S < 18.5) {
            String result = "UNDERWEIGHT";
            return result;
        } else if (BMI_S<23) {
            String result = "NORMAL";
            return result;
        } else if (BMI_S<25) {
            String result = "OVERWEIGHT";
            return result;
        } else if (BMI_S<30) {
            String result = "OBESE";
            return result;
        } else if (35<BMI_S) {
            String result = "EXTREMELY OBESE";
            return result;
        }
        return null;
    }


}