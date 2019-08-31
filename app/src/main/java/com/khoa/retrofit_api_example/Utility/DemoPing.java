package com.khoa.retrofit_api_example.Utility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DemoPing {

    public static float getPing(String ip) {

        String time = "";

        //The command to execute
        String pingCmd = "ping " + ip;

        //get the runtime to execute the command
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(pingCmd);

            //Gets the inputstream to read the output of the command
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //reads the outputs
            String inputLine = in.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("time")) {
                    time = inputLine.substring(inputLine.indexOf("time"));
                    time = time.substring(time.indexOf("=")+1, time.indexOf(" "));
                    break;
                }
                inputLine = in.readLine();
            }
        } catch (Exception ex) {
            Log.e("Loi", ex.getMessage());
        }
        return Float.valueOf(time);
//        String time ="";
//        try {
//
//            InetAddress inet = InetAddress.getByName(ip);
//
//            long finish = 0;
//            long start = new GregorianCalendar().getTimeInMillis();
//
//            if (inet.isReachable(5000)){
//                finish = new GregorianCalendar().getTimeInMillis();
//                time = "Ping RTT: " + (finish - start + "ms");
//            } else {
//                Log.e("Loi", ip + " NOT reachable.");
//            }
//        } catch ( Exception e ) {
//            Log.e("Loi", "Exception:" + e.getMessage());
//        }
//        Log.e("Loi", "time: " + time);
//        return time;
    }
}
