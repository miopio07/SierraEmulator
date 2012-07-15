package sierra.utils;

import java.util.ArrayList;

import sierra.storage.Storage;

public class BuildQuery {

    public static void doInsertQuery(String Table, ArrayList<String> KeyArray, ArrayList<Object> ValueArray, Storage Instance) {
        int ValueInt32 = 0, KeyInt32 = 0;

        //Inialize the query builder
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + Table + " ");
        
        //Initalize the key builder
        StringBuilder keyBuilder = new StringBuilder("(");
        
        //Initalize the insert builder
        StringBuilder valueBuilder = new StringBuilder(") VALUES (");

        for (String Key : KeyArray) {
            KeyInt32++;

            if (KeyInt32 != KeyArray.size()) {
                keyBuilder.append("`" + Key + "`, ");
            } else {
                keyBuilder.append("`" + Key + "`");
            }
        }
        for (Object Value : ValueArray) {
            ValueInt32++;

            if (ValueInt32 != ValueArray.size()) {
                valueBuilder.append("'" + Filter(Value.toString()) + "', ");
            } else {
                valueBuilder.append("'" + Filter(Value.toString()) + "')");
            }
        }
        
        //Run query
        Instance.ExecuteQuery(queryBuilder.toString() + keyBuilder.toString() + valueBuilder.toString());

        KeyArray.clear();
        ValueArray.clear();
    }

    private static String Filter(String Value) {
        String newValue = Value.replaceAll("'", "\\'");
        newValue = newValue.replaceAll("\"", "\\\"");
        return newValue;
    }
}