package com.example.Lab08Libros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class MainActivity extends AppCompatActivity {

    public static String QUANTITY_BOOKS = "QUANTITY_BOOKS";
    public static String LANGUAGE = "LANGUAGE";
    public static String PURCHASE = "PURCHASE";
    public static String BOOK_NAME = "BOOK_NAME";

    private EditText edtBookName;
    private Spinner spnLanguage;
    private CheckBox cbOnlyPurchase;
    private TextView tvQuantityBooks;
    private SeekBar sbQuantity;
    private Button btnSearch;

    private AwesomeValidation validation = new AwesomeValidation(ValidationStyle.COLORATION);

    String quantityBooks = "40";
    String language = "0";
    String purchase;
    String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtBookName = (EditText)findViewById(R.id.edtBookName);
        spnLanguage = (Spinner)findViewById(R.id.spnLanguage);
        cbOnlyPurchase = (CheckBox)findViewById(R.id.cbOnlyPurchase);
        tvQuantityBooks = (TextView)findViewById(R.id.tvQuantityBooks);
        sbQuantity = (SeekBar)findViewById(R.id.sbQuantity);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        validation.addValidation(this,R.id.edtBookName, RegexTemplate.NOT_EMPTY,R.string.errorBookName);

        //para el boton
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation.validate()){
                    bookName = edtBookName.getText().toString();
                    purchase = (cbOnlyPurchase.isChecked())?"1":"0";
                    Intent intent = new Intent(getApplicationContext(),bookList.class);
                    intent.putExtra(QUANTITY_BOOKS,quantityBooks);
                    intent.putExtra(LANGUAGE,language);
                    intent.putExtra(PURCHASE,purchase);
                    intent.putExtra(BOOK_NAME,bookName);
                    startActivity(intent);
                }
            }
        });

        //para el spinner
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: language = "0"; break;
                    case 1: language = "es"; break;
                    case 2: language = "en"; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //para el seekbar
        sbQuantity.setMax(39);
        sbQuantity.setProgress(sbQuantity.getMax());
        sbQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showQuantity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void showQuantity(int progress) {
        quantityBooks = String.valueOf(progress+1);
        if (progress == sbQuantity.getMax()){
            tvQuantityBooks.setText(getResources().getString(R.string.allBooks));
        } else if(progress == sbQuantity.getMin()){
            tvQuantityBooks.setText(getResources().getString(R.string.quantityBooks) + quantityBooks + getResources().getString(R.string.book));
        } else{
            tvQuantityBooks.setText(getResources().getString(R.string.quantityBooks) + quantityBooks + getResources().getString(R.string.books));
        }
    }
}