package com.example.Lab08Libros;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtilities {
    //variables de entorno como constantes para configurar la conexión al API de consulta de información de libros de google
    private static final String LOG_TAG = NetUtilities.class.getSimpleName();
    private static final String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String PARAM = "q";
    private static final String PARAM1 = "langRestrict";
    private static final String LIMIT = "maxResults";
    private static final String PRINT_TYPE = "printType";

    //método estático para obtener la información del libro
    static String getBookInfo(String title, String language, String quantity){
        //variables locales, para la conexión a internet y la lectura de la API
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonBook = null;
        try{
            //armamos un Uri y luego el URL para el consumo del APIftgehj
            Uri buildURI = Uri.parse(BOOK_URL).buildUpon().appendQueryParameter(PARAM,title).appendQueryParameter(LIMIT,quantity).appendQueryParameter(PRINT_TYPE,"books").build();
            if (language != null && !language.equals("0")){
                buildURI = Uri.parse(String.valueOf(buildURI)).buildUpon().appendQueryParameter(PARAM1,language).build();
            }
            URL requestURL = new URL(buildURI.toString());

            //Configuramos la petición de conexión mediante loa variable urlConection
            urlConnection = (HttpURLConnection)requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            //Configuramos la respuesta de la conexión
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            //Leemos línea a línea lo obtenido del servicio
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line + "\n");
            }

            //Comparamos si existe respuesta del servicio
            if(builder.length() == 0){
                return null;
            }

            //convertimos el resultado del servicio en string
            jsonBook = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //cierre de conexiones y del reader
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        //agregar un Log.d para comprobar que el servicio está funcionando:
        Log.d(LOG_TAG,jsonBook);

        //retornando resultado del libri en forma de json
        return jsonBook;
    }
}
