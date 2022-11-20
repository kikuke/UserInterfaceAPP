package com.echo.echofarm.Activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class UserWantProductAdapter extends RecyclerView.Adapter<UserWantProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> wantedProductsList;
    private ArrayList<Integer> wantedTagsList;
    private String[] tags;

    public UserWantProductAdapter(Context context, ArrayList<String> productsList, ArrayList<Integer> tagsList, String[] tags) {
        this.context = context;
        this.wantedProductsList = productsList;
        this.wantedTagsList = tagsList;
        this.tags = tags;
    }

    @NonNull
    @Override
    public UserWantProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.user_want_product_layout, parent, false),
                new EditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull UserWantProductAdapter.ViewHolder holder, int position) {
        holder.wantedProduct.setHint("교환을 원하는 물품" + (position+1));
        holder.wantedProductTagText.setText("물품" + (position+1) + " 태그 설정");
        holder.editTextListener.updatePosition(holder.getAdapterPosition());
        holder.wantedProduct.setText(wantedProductsList.get(holder.getAdapterPosition()));

        holder.wantedTagsSpinner.setSelection(wantedTagsList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return wantedProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText wantedProduct;
        TextView wantedProductTagText;
        Spinner wantedTagsSpinner;
        EditTextListener editTextListener;

        public ViewHolder(@NonNull View itemView, EditTextListener editTextListener) {
            super(itemView);

            this.editTextListener = editTextListener;
            wantedProduct = itemView.findViewById(R.id.wanted_product_edittext);
            wantedProductTagText = itemView.findViewById(R.id.wanted_product_tag_text);
            wantedTagsSpinner = itemView.findViewById(R.id.wanted_tags_spinner);

            wantedProduct.addTextChangedListener(editTextListener);

            ArrayAdapter<String> exAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tags);
            exAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            wantedTagsSpinner.setAdapter(exAdapter);

            wantedTagsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    wantedTagsList.set(getAdapterPosition(), i);
                    Log.i("my", "tag 변경", null);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    wantedTagsList.set(getAdapterPosition(), 0);
                }
            });
        }
    }
    private class EditTextListener implements  TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            wantedProductsList.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
