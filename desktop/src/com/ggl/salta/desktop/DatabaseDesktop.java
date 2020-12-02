package com.ggl.salta.desktop;

import com.ggl.salta.clases.Database;

import java.io.File;
import java.sql.*;

public class DatabaseDesktop extends Database{
    protected Connection db_connection;
    protected Statement stmt;
    protected boolean nodatabase=false;

    public DatabaseDesktop() {
<<<<<<< HEAD
        /*
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        loadDatabase();
        if (isNewDatabase()){
            onCreate();
            upgradeVersion();
        } else if (isVersionDifferent()){
            onUpgrade();
            upgradeVersion();
        }
<<<<<<< HEAD
*/
    }

    public void execute(String sql){
        /*
=======

    }

    public void execute(String sql){
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD

         */
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    }

    public int executeUpdate(String sql){
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Result query(String sql) {
<<<<<<< HEAD
        /*
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        try {
            return new ResultDesktop(stmt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD

         */
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        return null;
    }

    private void loadDatabase(){
<<<<<<< HEAD
        /*
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        File file = new File (database_name+".db");
        if(!file.exists())
            nodatabase=true;
        try {
            Class.forName("org.sqlite.JDBC");
            db_connection = DriverManager.getConnection("jdbc:sqlite:"+database_name+".db");
            stmt = db_connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD

         */
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    }

    private void upgradeVersion() {
        execute("PRAGMA user_version="+version);
    }

    private boolean isNewDatabase() {
        return nodatabase;
    }

    private boolean isVersionDifferent(){
<<<<<<< HEAD
        /*
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
        Result q=query("PRAGMA user_version");
        if (!q.isEmpty())
            return (q.getInt(1)!=version);
        else
            return true;
<<<<<<< HEAD

         */
        return true;
=======
>>>>>>> ab324cf272ec849cf5d21e55b0e9b85b79f5e20c
    }

    public class ResultDesktop implements Result{

        ResultSet res;
        boolean called_is_empty=false;

        public ResultDesktop(ResultSet res) {
            this.res = res;
        }

        public boolean isEmpty() {
            try {
                if (res.getRow()==0){
                    called_is_empty=true;
                    return !res.next();
                }
                return res.getRow()==0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean moveToNext() {
            try {
                if (called_is_empty){
                    called_is_empty=false;
                    return true;
                } else
                    return res.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public int getColumnIndex(String name) {
            try {
                return res.findColumn(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public int getInt(int columnIndex) {
            return 0;
        }

        @Override
        public String getString(int columnIndex) {
            return null;
        }

        public float getFloat(int columnIndex) {
            try {
                return res.getFloat(columnIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }


    }

}