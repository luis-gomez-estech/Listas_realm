package com.luisgomez.listas_realm.vistas.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luisgomez.listas_realm.R;
import com.luisgomez.listas_realm.adaptadores.RVUsuarios;
import com.luisgomez.listas_realm.modelos.LibroModelo;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<LibroModelo>> {


    public Realm realm;

    private FloatingActionButton fabNuevoUsuario;

    private Button btnNuevoUsuario;

    private RealmResults<LibroModelo> listaUsuarios;
    private RecyclerView recyclerViewUsuarios;
    private com.luisgomez.listas_realm.adaptadores.RVUsuarios RVUsuarios;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this); // Esto es lo que he tenido que añadir para que no me diera el error

        realm = Realm.getDefaultInstance();


        findViewID();

    }

    private void findViewID() {

        fabNuevoUsuario = findViewById(R.id.fabNuevoCategoria);

        btnNuevoUsuario = findViewById(R.id.fabNuevoCategoria2);

        listaUsuarios = realm.where(LibroModelo.class).findAll();
        listaUsuarios.addChangeListener((RealmChangeListener<RealmResults<LibroModelo>>) this);
        recyclerViewUsuarios = findViewById(R.id.recyclerCategoria);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RVUsuarios = new RVUsuarios(getApplicationContext(), listaUsuarios);
        recyclerViewUsuarios.setAdapter(RVUsuarios);

        listenOnClick();

    }

    private void listenOnClick() {

        fabNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNuevoLibro();
            }
        });



        btnNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNuevoLibro();
            }
        });


        RVUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LibroModelo libroModelo = listaUsuarios.get(recyclerViewUsuarios.getChildAdapterPosition(view));
                alertEditarLibro(libroModelo);
            }
        });

        RVUsuarios.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                eliminarLibro(view);
                return false;
            }
        });

    }

    public void alertNuevoLibro(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.agregar_usuario, null);
        builder.setView(view);

        final EditText edtTitulo = view.findViewById(R.id.edtTitulo);
        final EditText edtDescripcicon = view.findViewById(R.id.edtDescripcion);
        builder.setMessage(getString(R.string.mensaje_alerta));
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombre = edtTitulo.getText().toString().trim();
                String descripcion = edtDescripcicon.getText().toString();
                if (nombre.length()>0){
                    guardarLibro(nombre, descripcion);

                }else
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_mensaje_nombre_vacio), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertEditarLibro(final LibroModelo libroModelo){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.agregar_usuario, null);
        builder.setView(view);

        final EditText edtTitulo = view.findViewById(R.id.edtTitulo);
        final EditText edtDescripcicon = view.findViewById(R.id.edtDescripcion);
        builder.setMessage(getString(R.string.mensaje_editar_alert));

        edtTitulo.setText(libroModelo.getNombre());
        edtDescripcicon.setText(libroModelo.getDescripcion());

        edtTitulo.setSelection(edtTitulo.getText().length());//posicionamos el cursos al final

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombreLibro = edtTitulo.getText().toString().trim();
                String descripcion = edtDescripcicon.getText().toString();
                if (nombreLibro.length()>0){
                    realm.beginTransaction();
                    libroModelo.setNombre(nombreLibro);
                    libroModelo.setDescripcion(descripcion);
                    realm.commitTransaction();
                }else
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_mensaje_nombre_vacio)
                            , Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void guardarLibro(String nombre, String descripcion){
        realm.beginTransaction();
        LibroModelo libroModelo = new LibroModelo(nombre, descripcion);
        realm.copyToRealm(libroModelo);
        realm.commitTransaction();
    }

    public void eliminarLibro(View view){
        LibroModelo libroModelo = listaUsuarios.get(recyclerViewUsuarios.getChildAdapterPosition(view));
        realm.beginTransaction();
        assert libroModelo != null;
        libroModelo.deleteFromRealm();
        realm.commitTransaction();
    }



    @Override
    public void onChange(RealmResults<LibroModelo> libroModelo) {

    }


}
