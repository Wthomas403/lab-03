package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    public interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(City city);
    }

    static AddCityFragment newInstance(City city){
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        //Conditional to determine if the fragment opened should be for editing or adding
        City existingCity = null;
        if (getArguments() != null) {
            existingCity = (City) getArguments().getSerializable("city");
        }

        if (existingCity != null) {
            //show the name and province of selected city
            editCityName.setText(existingCity.getName());
            editProvinceName.setText(existingCity.getProvince());
        }

        City finalExistingCity = existingCity;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(existingCity == null ? "Add a city" : "Edit city")
                .setNegativeButton("Cancel", null)
                //Expand the lambda given in the lab to have edit functionality
                .setPositiveButton(existingCity == null ? "Add" : "Update", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (finalExistingCity == null) {
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        finalExistingCity.setName(cityName);
                        finalExistingCity.setProvince(provinceName);
                        listener.updateCity(finalExistingCity);
                    }
                })
                .create();
    }
}
