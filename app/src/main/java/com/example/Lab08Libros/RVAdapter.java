package com.example.Lab08Libros;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LibroViewHolder>{

    List<Libro> libros;
    Context context;

    public RVAdapter(List<Libro> libros, Context context) {
        this.libros = libros;
        this.context = context;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro,parent,false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        //elementos
        holder.tvBookTitle.setText(libros.get(position).title);
        holder.tvBookAuthor.setText(context.getResources().getString(R.string.author) + libros.get(position).authors);
        holder.tvBookPublisher.setText(context.getResources().getString(R.string.publisher) + libros.get(position).publisher);
        holder.tvBookPublishedDate.setText(context.getResources().getString(R.string.publishedDate) + libros.get(position).publishedDate);
        if (libros.get(position).saleability.equals("NOT_FOR_SALE")){
            holder.tvBookPrice.setText(context.getResources().getString(R.string.notForSale));
        }else{
            holder.tvBookPrice.setText(context.getResources().getString(R.string.price) + libros.get(position).saleability);
        }
        holder.tvTextSnippet.setText(libros.get(position).textSnippet);
        holder.tvInfoLink.setText(libros.get(position).infoLink);
        if (libros.get(position).thumbnail.equals("")){
            holder.ivBookThumbnail.setImageResource(R.drawable.sinportada);
        }else{
            Picasso.get().load(libros.get(position).thumbnail).into(holder.ivBookThumbnail);
        }
        //eventos del boton
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }


    public class LibroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView ivBookThumbnail;
        TextView tvBookTitle;
        TextView tvBookAuthor;
        TextView tvBookPublisher;
        TextView tvBookPublishedDate;
        TextView tvBookPrice;
        TextView tvTextSnippet;
        TextView tvInfoLink;
        Button btnSeeMore;
        Dialog dialog;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView  = (CardView)itemView.findViewById(R.id.cv);
            ivBookThumbnail  = (ImageView)itemView.findViewById(R.id.ivBookThumbnail);
            tvBookTitle  = (TextView)itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor  = (TextView)itemView.findViewById(R.id.tvBookAuthor);
            tvBookPublisher  = (TextView)itemView.findViewById(R.id.tvBookPublisher);
            tvBookPublishedDate  = (TextView)itemView.findViewById(R.id.tvBookPublishedDate);
            tvBookPrice  = (TextView)itemView.findViewById(R.id.tvBookPrice);
            tvTextSnippet  = (TextView)itemView.findViewById(R.id.tvTextSnippet);
            tvInfoLink  = (TextView)itemView.findViewById(R.id.tvInfoLink);
           // btnSeeMore  = (Button)itemView.findViewById(R.id.btnSeeMore);
        }

        public void setOnClickListeners() {
            try {
                btnSeeMore.setOnClickListener(this);
            }catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onClick(View v) {
           /* switch (v.getId()){
                case R.id.btnSeeMore:
                    createPopUp(itemView,tvBookTitle,tvTextSnippet,tvInfoLink);
                    break;
            }*/
        }

        private void createPopUp(final View itemView, TextView tvBookTitle, TextView tvtextSnippet, final TextView tvInfoLink) {
            //creando dialog
            dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.pop_up_info_book);
            dialog.setTitle(tvBookTitle.getText().toString());
            //creando objetos
            TextView title = (TextView) dialog.findViewById(R.id.tvTitleBook);
            TextView description = (TextView) dialog.findViewById(R.id.tvInfoBook);
            Button btnViewInfo = (Button)dialog.findViewById(R.id.btnSeeInfo);
            //dando valor a los objetos
            title.setText(tvBookTitle.getText().toString());
            description.setText(tvtextSnippet.getText().toString());
            description.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            //evento del boton
            btnViewInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse((String)tvInfoLink.getText());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    itemView.getContext().startActivity(intent);
                }
            });
            //Iniciando pop-up
            dialog.show();
        }
    }
}
