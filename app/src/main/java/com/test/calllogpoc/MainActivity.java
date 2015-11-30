package com.test.calllogpoc;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;

import java.sql.Date;

public class MainActivity extends Activity {
    TextView textView = null;
    //TODO : get lastId from cache and once done store the last id in cache for it to run next time
    long lastId = 5600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview_call);
        getCallDetails();
    }

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
  /* Query the CallLog Content Provider */
//        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
//                null, null, strOrder);
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                "_ID > " + lastId, null, strOrder);
        int id = managedCursor.getColumnIndex("_ID");
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
            long idVal = managedCursor.getLong(id);
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            sb.append("\nID:---" + idVal +
                    "\nPhone Number:--- " + phNum + " \nCall Type:--- "
                    + callType + " \nCall Date:--- " + callDate
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        textView.setText(sb);
    }

}