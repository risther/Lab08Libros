package com.example.Lab08Libros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class bookList extends AppCompatActivity {

    private List<Libro> libros;

    String quantityBooks;
    String languageSelected;
    String purchase;
    String bookName;
    final String BLANK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Intent queryIntent = getIntent();
        languageSelected = queryIntent.getStringExtra(MainActivity.LANGUAGE);
        purchase = queryIntent.getStringExtra(MainActivity.PURCHASE);
        bookName = queryIntent.getStringExtra(MainActivity.BOOK_NAME);
        quantityBooks = queryIntent.getStringExtra(MainActivity.QUANTITY_BOOKS);

        searchBooks();
    }

    public void searchBooks(){
        //línea que llama la clase AsyncTask
        new getBooks().execute(bookName,languageSelected,quantityBooks);
    }

    public class getBooks extends AsyncTask<String,Void,String> {

        public getBooks(){
        }

        @Override
        protected String doInBackground(String... strings) {
            return NetUtilities.getBookInfo(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                int i= 0;
                String thumbnail = null;
                String title = null;
                String authors = null;
                String publisher = null;
                String publishedDate = null;
                String saleability = null;
                String textSnippet = null;
                String infoLink = null;
                libros = new ArrayList<>();
                while(i < itemsArray.length()){
                    JSONObject libro = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = libro.getJSONObject("volumeInfo");
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    JSONObject saleInfo = libro.getJSONObject("saleInfo");
                    try {
                        JSONObject searchInfo = libro.getJSONObject("searchInfo");
                        textSnippet = searchInfo.getString("textSnippet");
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if ( textSnippet == null || textSnippet.isEmpty()){
                            textSnippet = getResources().getString(R.string.withoutDescription);
                        }
                    }
                    try{
                        thumbnail = imageLinks.getString("thumbnail");
                        thumbnail = thumbnail.replace("http","https");
                        if (thumbnail.isEmpty() || thumbnail == null){
                            thumbnail = "- - -";
                        }
                        title = volumeInfo.getString("title");
                        if (title.isEmpty() || title == null){
                            title = "- - -";
                        }
                        authors = volumeInfo.getString("authors");
                        authors = authors.substring(1, authors.length() - 1);
                        authors = authors.replace("\"",BLANK);
                        authors = authors.replace(",",", ");
                        if (authors.isEmpty() || authors == null){
                            authors = "- - -";
                        }
                        publisher = volumeInfo.getString("publisher");
                        if (publisher.isEmpty() || publisher == null){
                            publisher = "- - -";
                        }
                        publishedDate = volumeInfo.getString("publishedDate");
                        if (publishedDate.isEmpty() || publishedDate == null){
                            publishedDate = "- - -";
                        }
                        saleability = saleInfo.getString("saleability");
                        if (saleability.equals("FOR_SALE")) {
                            JSONObject priceInfo = saleInfo.getJSONObject("retailPrice");
                            saleability =priceInfo.getString("amount");
                        }
                        infoLink = volumeInfo.getString("infoLink");
                        if (infoLink.isEmpty() || infoLink == null){
                            infoLink = "- - -";
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    i++;
                    if (purchase.equals("1")){
                        if (!saleability.equals("NOT_FOR_SALE")){
                            libros.add(new Libro(title,authors,publisher,publishedDate,thumbnail,textSnippet,saleability,infoLink));
                        }
                    }else{
                        libros.add(new Libro(title,authors,publisher,publishedDate,thumbnail,textSnippet,saleability,infoLink));
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            finally {
                if (libros.size()==0){
                    libros.add(new Libro(getResources().getString(R.string.anyResult),"-","-","-","yik","-","-","https://google.com"));
                }
                startRecyclerView();
            }
        }

        private void startRecyclerView() {
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            RVAdapter rvAdapter = new RVAdapter(libros,getApplicationContext());
            recyclerView.setAdapter(rvAdapter);
        }
    }
}