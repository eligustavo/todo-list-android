package com.example.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todolist.db.ContratoTarefas;
import com.example.todolist.db.DbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DbHelper helper;
    private ListView tListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(ContratoTarefas.Tarefa.TABLE,
                new String[]{ContratoTarefas.Tarefa._ID, ContratoTarefas.Tarefa.COL_TAREFAS_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ContratoTarefas.Tarefa.COL_TAREFAS_TITLE);
            Log.d(TAG, "Tarefa: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_tarefa:
                final EditText tarefasEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Adicionar uma nova tarefa")
                        .setMessage("O quê fazer em seguida?")
                        .setView(tarefasEditText)
                        .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String tarefa = String.valueOf(tarefasEditText.getText());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(ContratoTarefas.Tarefa.COL_TAREFAS_TITLE, tarefa);
                                db.insertWithOnConflict(ContratoTarefas.Tarefa.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create();
                dialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delete(View v){
        View parent = (View) v.getParent();
        TextView textView = (TextView) parent.findViewById(R.id.titulo);
        String tarefa = String.valueOf(textView.getText());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(ContratoTarefas.Tarefa.TABLE,
                ContratoTarefas.Tarefa.COL_TAREFAS_TITLE + " = ?",
                new String[] {tarefa});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> tarefas = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(ContratoTarefas.Tarefa.TABLE,
                new String[]{ContratoTarefas.Tarefa._ID, ContratoTarefas.Tarefa.COL_TAREFAS_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ContratoTarefas.Tarefa.COL_TAREFAS_TITLE);
            tarefas.add(cursor.getString(idx));
        }
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this,
                    R.layout.activity_item_todo,
                    R.id.titulo,
                    tarefas);
            tListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(tarefas);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

}


