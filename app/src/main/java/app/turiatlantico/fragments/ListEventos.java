package app.turiatlantico.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

import app.turiatlantico.Detalle;
import app.turiatlantico.R;
import app.turiatlantico.pojos.Evento;
import app.turiatlantico.recycler.EventosRecyclerAdapter;




/**
 * A simple {@link Fragment} subclass.
 */

public class ListEventos extends Fragment {
 private static  final String EVENTO = "Eventos";

    EventosRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ArrayList <Evento>  lisEventos = new ArrayList<>();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) vw.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestEventos();
        return vw;
    }

    private void requestEventos() {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference myRef = firestore.collection(EVENTO);

        myRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    lisEventos.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       // String even = (String) document.getData().get("Nombre");
                       // Toast.makeText(getContext(), "Nombre: "+document.getId(), Toast.LENGTH_LONG).show();
                        Evento eve = new Evento();
                        eve.setId(document.getId());
                        eve.setDescripcion((String) document.getData().get("Descripcion"));
                        eve.setDirrecion((String) document.getData().get("Dirrecion"));
                        eve.setNombre((String) document.getData().get("Nombre"));
                        eve.setMes((String) document.getData().get("fecha"));
                        listEventos(eve);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to read value." + task.getException(), Toast.LENGTH_LONG).show();
                }

            }
        });

     /*   myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapShot: dataSnapshot.getChildren()){
                    Evento eve = snapShot.getValue(Evento.class);
                    listEventos(eve);
                    //Toast.makeText(getContext(), "Encontrado "+eve.getEvento(), Toast.LENGTH_LONG).show();
                }
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, ""+ value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();

            }
        });*/


    }
    private void listEventos(Evento eve) {

        lisEventos.add(eve);
        adapter= new EventosRecyclerAdapter(lisEventos);
        adapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), Detalle.class);
                intent.putExtra("nombre",lisEventos.get(recyclerView.getChildAdapterPosition(view)).getNombre());
                intent.putExtra("tipo","Municipio: "+lisEventos.get
                        (recyclerView.getChildAdapterPosition(view)).getDirrecion());
                intent.putExtra("dirrecion","");
                intent.putExtra("detalle",lisEventos.get(recyclerView.getChildAdapterPosition(view)).getDescripcion());
                intent.putExtra("municipio","Se celebra en el mes de: "+lisEventos.
                        get(recyclerView.getChildAdapterPosition(view)).getMes());
                intent.putExtra("id",lisEventos.get(recyclerView.getChildAdapterPosition(view)).getId());
                intent.putExtra("Nom_Tipo","Eventos");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
