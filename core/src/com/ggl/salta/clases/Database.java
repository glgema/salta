package com.ggl.salta.clases;

//General class that needs to be implemented on Android and Desktop Applications
public abstract class Database {

    public static final String BASE_DATOS = "guardado.db";
    public static final String TABLA = "guardado_partida";
    public static final String ID = "id";
    public static final String SELECCIONADO = "seleccionado";
    public static final String TROFEOS = "trofeos";
    public static final String ALTURA = "altura";
    public static final String DESBLOQUEADO = "desbloqueado";

    protected static Database instance = null;
    protected static int version=1;

    //Runs a sql query like "create".
    public abstract void execute(String sql);

    //Identical to execute but returns the number of rows affected (useful for updates)
    public abstract int executeUpdate(String sql);

    //Runs a query and returns an Object with all the results of the query. [Result Interface is defined below]
    public abstract Result query(String sql);

    public void onCreate(){
<<<<<<< HEAD

=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        //Example of Highscore table code (You should change this for your own DB code creation)
        execute("CREATE TABLE IF NOT EXISTS '"+TABLA+"' ('"+ID+"' INTEGER PRIMARY KEY  NOT NULL , '"+SELECCIONADO+"' INTEGER NOT NULL , '"+TROFEOS+"' INTEGER NOT NULL,'"+ALTURA+"' INTEGER NOT NULL,'"+DESBLOQUEADO+"' VARCHAR NOT NULL );");

        Result q=query("SELECT * FROM '"+TABLA+"'");

        if(q.isEmpty())
            execute("INSERT INTO '"+TABLA+"'("+ID+","+SELECCIONADO+","+TROFEOS+","+ALTURA+","+DESBLOQUEADO+") values (1,0,0,0,'0')");
        else {
            q.moveToNext();
            System.out.println("TABLA of "+q.getInt(q.getColumnIndex(ID))+": "+q.getInt(q.getColumnIndex(SELECCIONADO))+": "+q.getInt(q.getColumnIndex(TROFEOS))+": "+q.getInt(q.getColumnIndex(ALTURA))+": "+q.getString(q.getColumnIndex(DESBLOQUEADO)));
        }
    }

    public void onUpgrade(){
        //Example code (You should change this for your own DB code)
        execute("DROP TABLE IF EXISTS 'highscores';");
        onCreate();
        System.out.println("DB Upgrade maded because I changed DataBase.version on code");
    }

    //Interface to be implemented on both Android and Desktop Applications
    public interface Result{
        public boolean isEmpty();
        public boolean moveToNext();
        public int getColumnIndex(String name);
        public int getInt(int columnIndex);
        public String getString(int columnIndex);
    }

    public int getTrofeos(){
        Result q=query("SELECT "+TROFEOS+" FROM '"+TABLA+"'");
        q.moveToNext();
        return q.getInt(q.getColumnIndex(TROFEOS));
    }

    public void setTrofeos(int trofeos){
        execute("UPDATE "+TABLA+ " SET "+TROFEOS+" = "+trofeos);
    }

    public void setSelection(int cont){
        execute("UPDATE "+TABLA+ " SET "+SELECCIONADO+" = "+cont);
    }

    public int getSelection(){
        Result q=query("SELECT "+SELECCIONADO+" FROM '"+TABLA+"'");
        q.moveToNext();
        return q.getInt(q.getColumnIndex(SELECCIONADO));
    }

    public void addTrofeos(int trofeos){
        setTrofeos(trofeos + getTrofeos());
    }

    public String getDesbloqueados(){
        Result q=query("SELECT "+DESBLOQUEADO+" FROM '"+TABLA+"'");
        q.moveToNext();
        return q.getString(q.getColumnIndex(DESBLOQUEADO));
    }

    public boolean estaDesbloqueado(int aspecto){
        String[] desbloqueados = getDesbloqueados().split(" ");

        for(String des : desbloqueados)
            if(Integer.parseInt(des) == aspecto)
                return true;

        return false;
    }

    public void desbloquear(int aspecto){
        execute("UPDATE "+TABLA+ " SET "+DESBLOQUEADO+" = '"+getDesbloqueados()+" "+aspecto+"'");
    }
<<<<<<< HEAD

    public int getAltura(){
        Result q=query("SELECT "+ALTURA+" FROM '"+TABLA+"'");
        q.moveToNext();
        return q.getInt(q.getColumnIndex(ALTURA));
    }

    public void setAltura(int altura){
        execute("UPDATE "+TABLA+ " SET "+ALTURA+" = "+altura);
    }
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
}
