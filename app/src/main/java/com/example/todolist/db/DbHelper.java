package com.example.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, ContratoTarefas.DB_NAME, null, ContratoTarefas.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ContratoTarefas.Tarefa.TABLE + "(" +
                ContratoTarefas.Tarefa._ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContratoTarefas.Tarefa.COL_TAREFAS_TITLE + "TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContratoTarefas.Tarefa.TABLE);
        onCreate(db);
    }
}
