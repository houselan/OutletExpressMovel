package eduardo.mariana.ilanna.paulo.outletexpressmovel.fragment;

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

import java.util.List;

import eduardo.mariana.ilanna.paulo.outletexpressmovel.R;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.activity.HomeActivity;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.OfertasAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.adapter.PesquisaAdapter;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.model.HomeViewModel;
import eduardo.mariana.ilanna.paulo.outletexpressmovel.object.Produto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfertasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfertasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OfertasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfertasFragment newInstance() {
        OfertasFragment fragment = new OfertasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ofertas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivity homeActivity = (HomeActivity) getActivity();
        HomeViewModel homeViewModel = new ViewModelProvider(homeActivity).get(HomeViewModel.class);


        RecyclerView rvMaisComprados = (RecyclerView) view.findViewById(R.id.rvMaisComprados);
        rvMaisComprados.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        LiveData<List<Produto>> maisCompradosLD = homeViewModel.getMaisCompradosLD();
        maisCompradosLD.observe(getViewLifecycleOwner(), new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> produtos) {
                OfertasAdapter ofertasAdapter = new OfertasAdapter(homeActivity, produtos);
                rvMaisComprados.setAdapter(ofertasAdapter);
            }
        });


        RecyclerView rvMelhoresAvaliados = (RecyclerView) view.findViewById(R.id.rvMelhoresAvaliados);
        rvMelhoresAvaliados.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        LiveData<List<Produto>> melhoresAvaliadosLD = homeViewModel.getMelhoresAvaliadosLD();
        melhoresAvaliadosLD.observe(getViewLifecycleOwner(), new Observer<List<Produto>>() {
            @Override
            public void onChanged(List<Produto> produtos) {
                OfertasAdapter ofertasAdapter = new OfertasAdapter(homeActivity, produtos);
                rvMelhoresAvaliados.setAdapter(ofertasAdapter);
            }
        });

    }
}