package com.example.todolist.db;

import android.provider.BaseColumns;

public class ContratoTarefas {
      public static final String DB_NAME = "com.example.todolist.db";
    public static final int DB_VERSION = 1;

    public class Tarefa implements BaseColumns {
        public static final String TABLE = "tarefas";
        public static final String COL_TAREFAS_TITLE = "titulo";
    }

}
