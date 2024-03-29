package eduardo.mariana.ilanna.paulo.outletexpressmovel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eduardo.mariana.ilanna.paulo.outletexpressmovel.R;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.CarrinhoAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.ComprasAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.ItemCompraAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.fragment.CarrinhoFragment;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.model.CompraViewModel;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.model.HomeViewModel;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.model.ProdutoViewModel;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.ItemCarrinho;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.ItemCompra;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.Produto;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.util.ImageCache;

public class CompraActivity extends AppCompatActivity {
    CompraViewModel compraViewModel;
    String codigo_produto;
    int codigo_produto_int;
    String quantidade_produto;
    int quantidade_produto_int;
    //int codigo_cliente_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        // Aqui nós obtemos o codigo do produto passado pela ProdutoActivity.
        Intent i = getIntent();

        //caso a tela tenha sido aberta atraves do botao ComprarAgora
        if(i.hasExtra("codigo_produto") && i.hasExtra("quantidade")){
            this.codigo_produto = i.getStringExtra("codigo_produto");
            this.codigo_produto_int = Integer.parseInt(codigo_produto);
            this.quantidade_produto = i.getStringExtra("quantidade");
            this.quantidade_produto_int = Integer.parseInt(quantidade_produto);

            //buscar valor do produto selecionado
            // obtemos o ViewModel pois é nele que está o método que se conecta ao servior web.
            ProdutoViewModel produtoViewModel = new ViewModelProvider(this).get(ProdutoViewModel.class);
            LiveData<Produto> produto = produtoViewModel.getProductDetailsLD(String.valueOf(codigo_produto_int));
            produto.observe(this, new Observer<Produto>() {
                @Override
                public void onChanged(Produto produto) {

                    if(produto != null) {
                        //calculo do valor total da compra
                        float valor_produto = Float.parseFloat(produto.valor_atual);
                        float valor_total = valor_produto * quantidade_produto_int;
                        String valor = String.format("%.2f", valor_total);

                        //setando na textview o valor total
                        TextView total = findViewById(R.id.tvTotal);
                        total.setText("R$ " + valor);

                        String nome_produto = produto.nome_produto;

                        ItemCompra itemCompra = new ItemCompra(quantidade_produto_int, nome_produto, valor_produto, codigo_produto_int);

                        //exibindo os produtos selecionados para a compra
                        RecyclerView rvProdutosSelecionados = findViewById(R.id.rvProdutosSelecionados);
                        rvProdutosSelecionados.setLayoutManager(new LinearLayoutManager(CompraActivity.this));

                        List<ItemCompra> listItemCompra = new ArrayList<ItemCompra>();
                        listItemCompra.add(itemCompra);
                        ItemCompraAdapter itemCompraAdapter = new ItemCompraAdapter(CompraActivity.this,listItemCompra);

                        rvProdutosSelecionados.setAdapter(itemCompraAdapter);
                    }
                    else {
                        Toast.makeText(CompraActivity.this, "Não foi possível obter os detalhes do produto", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            this.codigo_produto = "";
            this.quantidade_produto = "";

            HomeViewModel homeViewModel = new ViewModelProvider(CompraActivity.this).get(HomeViewModel.class);

            List<ItemCarrinho> itens_carrinho= new ArrayList<ItemCarrinho>();
            LiveData<List<ItemCarrinho>> prodLiveData = homeViewModel.getCarrinhoLD();
            prodLiveData.observe(this, new Observer<List<ItemCarrinho>>() {
                @Override
                public void onChanged(List<ItemCarrinho> itensCarrinho) {
                    List<ItemCompra> listItemCompra = new ArrayList<ItemCompra>();

                    List<ItemCarrinho> carrinho = prodLiveData.getValue();
                    float total = 0;
                    for (int i = 0; i < carrinho.size(); i++) {
                        ItemCarrinho itemCarrinho = carrinho.get(i);
                        float preco = Float.parseFloat(itemCarrinho.produto.valor_atual);
                        total += preco * itemCarrinho.quantidade;

                        ItemCompra itemCompra = new ItemCompra(itemCarrinho.quantidade,itemCarrinho.produto.nome_produto,Float.parseFloat(itemCarrinho.produto.valor_atual),itemCarrinho.produto.codigo);
                        listItemCompra.add(itemCompra);
                    }

                    RecyclerView rvProdutosSelecionados = findViewById(R.id.rvProdutosSelecionados);
                    rvProdutosSelecionados.setLayoutManager(new LinearLayoutManager(CompraActivity.this));

                    ItemCompraAdapter itemCompraAdapter = new ItemCompraAdapter(CompraActivity.this,listItemCompra);
                    rvProdutosSelecionados.setAdapter(itemCompraAdapter);

                    TextView tvTotal = findViewById(R.id.tvTotal);
                    String valor = String.format("%.2f", total);
                    tvTotal.setText("R$ " + valor);

                }
            });

        }


        //pegando todos os campos declarados pelo usuario na activity_compra quando clicar em finalizar compra
        Button btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //selecionando forma de pagamento
                RadioGroup rgFormaPagamento = findViewById(R.id.rgFormaPagamento);
                int pagamentoId = rgFormaPagamento.getCheckedRadioButtonId();
                System.out.println("id pagamento: " + pagamentoId);
                RadioButton rb = (RadioButton) rgFormaPagamento.getChildAt(rgFormaPagamento.indexOfChild(findViewById(pagamentoId)));
                String pagamentoSelecionado = rb.getText().toString();
                //System.out.println("pagamentoSelecionado: " + pagamentoSelecionado);

                //selecionando dados do endereco
                EditText cep = findViewById(R.id.etCep);
                String cepSelecionado = cep.getText().toString();
                EditText numero = findViewById(R.id.etNumero);
                Integer numeroSelecionado = Integer.parseInt(numero.getText().toString());
                EditText rua = findViewById(R.id.etRua);
                String ruaSelecionada = rua.getText().toString();

                //declarando cpf
                EditText cpf = findViewById(R.id.etCpf);
                String cpfSelecionado = cpf.getText().toString();

                CompraViewModel compraViewModel = new ViewModelProvider(CompraActivity.this).get(CompraViewModel.class);
                LiveData<Boolean> resultLD = compraViewModel.compra(pagamentoSelecionado, cpfSelecionado, cepSelecionado, ruaSelecionada, numeroSelecionado, codigo_produto, quantidade_produto);

                resultLD.observe(CompraActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean) {
                            Toast.makeText(CompraActivity.this, "Nova compra registrada com sucesso", Toast.LENGTH_LONG).show();
                            // Navega para tela principal
                            Intent i = new Intent(CompraActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                        else {
                            // Se o cadastro não deu certo, apenas continuamos na tela de cadastro e
                            // indicamos com uma mensagem ao usuário que o cadastro não deu certo.
                            Toast.makeText(CompraActivity.this, "Erro ao registrar nova compra", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}