package eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eduardo.mariana.ilanna.paulo.outletexpressmovel.R;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.activity.HomeActivity;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.activity.ProdutoActivity;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.Produto;

public class PesquisaAdapter extends RecyclerView.Adapter {
    public HomeActivity homeActivity;
    public List<Produto> produtos;

    public PesquisaAdapter(HomeActivity homeActivity, List<Produto> produtos) {
        this.homeActivity = homeActivity;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(homeActivity);
        View v = inflater.inflate(R.layout.itemlist_horizontal,parent,false);
        return new ProdutoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //parei aqui na ultima aula de mobile
        //devo copiar o que fiz na CategoriasAdapter
        //pra poder preencher a lista de produtos, quando alguem pesquisar uma categoria ou quando pesquisarem um produto
        Produto produto = produtos.get(position);
        View v = holder.itemView;

        //preenche a imagem do produto
        ImageView imvProduto = v.findViewById(R.id.imvProduto);
        imvProduto.setImageURI(Uri.parse(produto.imagem));

        //preenche o nome do produto
        TextView tvNomeProduto = v.findViewById(R.id.tvNomeProduto);
        tvNomeProduto.setText(produto.nome_produto);

        TextView tvValorProduto = v.findViewById(R.id.tvValorProduto);
        tvValorProduto.setText(Float.toString(produto.valor_atual));

        RatingBar rbAvaliacaoProduto = v.findViewById(R.id.rbAvaliacaoProduto);
        rbAvaliacaoProduto.setStepSize(0.1f);
        rbAvaliacaoProduto.setRating(produto.avaliacao);

        TextView tvDesconto = v.findViewById(R.id.tvDesconto);
        tvDesconto.setText(Float.toString(produto.desconto));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(homeActivity, ProdutoActivity.class);
                i.putExtra("produto_id",produto.id);
                homeActivity.startActivity(i);
                homeActivity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }
}