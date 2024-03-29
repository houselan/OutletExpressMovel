package eduardo.mariana.ilanna.paulo.outletexpressmovel.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eduardo.mariana.ilanna.paulo.outletexpressmovel.R;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.activity.CompraActivity;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.activity.HomeActivity;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.CarrinhoAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.PesquisaAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.model.HomeViewModel;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.ItemCarrinho;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.Produto;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.util.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarrinhoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarrinhoFragment extends Fragment {

    HomeViewModel mViewModel;
    HomeActivity homeActivity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarrinhoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarrinhoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarrinhoFragment newInstance(String param1, String param2) {
        CarrinhoFragment fragment = new CarrinhoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CarrinhoFragment newInstance() {
        return new CarrinhoFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_carrinho, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvCarrinho = (RecyclerView) view.findViewById(R.id.rvCarrinho);
        rvCarrinho.setLayoutManager(new LinearLayoutManager(getContext()));

        homeActivity = (HomeActivity) getActivity();

        mViewModel = new ViewModelProvider(homeActivity).get(HomeViewModel.class);

        List<ItemCarrinho> itens_carrinho= new ArrayList<ItemCarrinho>();
        LiveData<List<ItemCarrinho>> prodLiveData = mViewModel.getCarrinhoLD();
        prodLiveData.observe(getViewLifecycleOwner(), new Observer<List<ItemCarrinho>>() {
            @Override
            public void onChanged(List<ItemCarrinho> itensCarrinho) {
                CarrinhoAdapter carrinhoAdapter = new CarrinhoAdapter(homeActivity, itensCarrinho, CarrinhoFragment.this);
                rvCarrinho.setAdapter(carrinhoAdapter);

                List<ItemCarrinho> carrinho = prodLiveData.getValue();
                float total = 0;
                for (int i = 0; i < carrinho.size(); i++) {
                    float preco = Float.parseFloat(carrinho.get(i).produto.valor_atual);
                    total += preco * carrinho.get(i).quantidade;
                }

                TextView tvItemCarPreco =  homeActivity.findViewById(R.id.tvTotalCarrinho);
                tvItemCarPreco.setText("R$ " + String.format("%.2f",total));

            }
        });

        Button btnComprarTudo = homeActivity.findViewById(R.id.btnComprarTudo);
        btnComprarTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(homeActivity, CompraActivity.class);
                startActivity(i);
                homeActivity.finish();
            }
        });

    }

    public void excluirItemCarrinho(int id) {
        LiveData<Boolean> delLiveData = mViewModel.getResultDeleteLD(id);

        delLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean delLiveData) {
                if(delLiveData){
                    Toast.makeText(getActivity(), "Item removido do Carrinho com Sucesso", Toast.LENGTH_LONG).show();
                    HomeActivity homeActivity = (HomeActivity) getActivity();

                    homeActivity.setFragment(new CarrinhoFragment(),R.id.fragContainer);
                }
                else {
                    Toast.makeText(getActivity(), "Erro ao remover item do Carrinho", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void atualizarQtdItem(int id, int qtd) {

        LiveData<Boolean> delLiveData = mViewModel.getUpdateQtd(id, qtd);

        delLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean delLiveData) {
                if(delLiveData){
                    Toast.makeText(getActivity(), "Item atualizado no Carrinho com Sucesso", Toast.LENGTH_LONG).show();
                    HomeActivity homeActivity = (HomeActivity) getActivity();

                    TextView tvItemCarQtd = homeActivity.findViewById(R.id.tvItemCarQtd);
                    tvItemCarQtd.setText(Integer.toString(qtd));
                    homeActivity.setFragment(new CarrinhoFragment(),R.id.fragContainer);
                }
                else {
                    Toast.makeText(getActivity(), "Erro ao atualizar item no Carrinho", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}