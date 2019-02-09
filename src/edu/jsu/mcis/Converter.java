package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    public static final int NUM_FIELDS = 4;
    public static final int NUM_FIELDS_W_ID = 5;
    
    public static String[] getStringArray(ArrayList<String> arr){ 

        String str[] = new String[arr.size()]; 
   
        Object[] objArr = arr.toArray(); 
 
        int i = 0; 
        for (Object obj : objArr) { 
            str[i++] = (String)obj; 
        } 
  
        return str; 
    } 
      
      
      
      
      
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
     
            JSONObject jsonObject = new JSONObject();
            JSONArray colHeaders = new JSONArray();
            JSONArray rowHeaders = new JSONArray();
            JSONArray data = new JSONArray();
            String [] result;
            String [] keys = new String [3];
            keys[0] = "colHeaders";
            keys[1] = "rowHeaders";
            keys[2] = "data";
            
            String [] cols = iterator.next();
            
            for(String col : cols){
                colHeaders.add(col);
            }
            
            while(iterator.hasNext()){
                result = iterator.next();
                ArrayList<Integer> dataList = new ArrayList<>();
                for(int i = 1; i < keys.length; ++i){
                    for(int j = 0; j < result.length; ++j){
                        if(i == 1 && j == 0){
                            rowHeaders.add(result[j]);
                        }         
                        else if(i == 2 && j > 0){  
                            dataList.add(Integer.parseInt(result[j]));
                            
                        }
                    }
                }
                data.add(dataList);
            }
            jsonObject.put(keys[0], colHeaders);
            jsonObject.put(keys[1], rowHeaders);
            jsonObject.put(keys[2], data);
            results = JSONValue.toJSONString(jsonObject);
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
 
            ArrayList<String> colHeadersAL = (ArrayList)jsonObject.get("colHeaders");
            ArrayList<String> rowHeadersAL = (ArrayList)jsonObject.get("rowHeaders");
            ArrayList<String> dataAL = new ArrayList();
            ArrayList<ArrayList> dataList = (ArrayList)jsonObject.get("data"); 
            
            for(ArrayList a : dataList){
                for(Object o : a){
                    String s = o.toString();
                    dataAL.add(s);
                }
            }
            
            String [] colHeaders = getStringArray(colHeadersAL);
            String [] rowHeaders = getStringArray(rowHeadersAL);
            String [] data = getStringArray(dataAL);
            
            
            
            csvWriter.writeNext(colHeaders);
            
            int addCounter = 0;
            
            for(int i = 0; i < rowHeaders.length; ++i){
                String [] csvData = new String [NUM_FIELDS_W_ID];
                csvData [0] = rowHeaders [i];
                for(int j = 0; j < NUM_FIELDS; ++j){
                    csvData[j + 1] = data [addCounter++];
                }
                
                csvWriter.writeNext(csvData);    
            }
            
            results = writer.toString();
            
            
            
            

                       
    }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}