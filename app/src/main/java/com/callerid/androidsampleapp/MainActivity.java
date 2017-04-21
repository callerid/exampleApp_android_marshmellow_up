package com.callerid.androidsampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // -------------------------------------------------
    // Required Memory
    // -------------------------------------------------
    // Required objects
    private static TextView lbStatus;

    static ImageView callPhonePics[] = new ImageView[5];
    static TextView callTimes[] = new TextView[5];
    static TextView callNumbers[] = new TextView[5];
    static TextView callNames[] = new TextView[5];
    static TableRow callPanels[] = new TableRow[5];

    // Memory of program when minimized
    static int memPhoneStatus[] = new int[5];
    static String memCallTimes[] = new String[5];
    static String memCallNumbers[] = new String[5];
    static String memCallNames[] = new String[5];
    static int memCallPanels[] = new int[5];

    // Required Variables
    static int lightBlue = Color.rgb(223, 246, 247);
    static int lightGreen = Color.rgb(211,245,211);
    static int lightestGreen = Color.rgb(232, 250, 233);
    static int lightGrey = Color.rgb(227,229,230);

    // Images
    static int imgRing;
    static int imgOffHook;
    static int imgOnHook;

    // -------------------------------------------------
    // Update UI
    // -------------------------------------------------

    public void updateUI(String inData, boolean visible){

        // Setup variables for use
        String myData = inData;

        String command;
        Integer myLine=0;
        String myType="";
        String myIndicator="";

        // Unused in this app but available for other custom apps
        String myDuration="";
        String myCheckSum="";
        String myRings="";
        //------------------------------------------------------

        String myDateTime="";
        String myNumber="";
        String myName="";

        // Check if matches a call record
        Pattern myPattern = Pattern.compile(".*(\\d\\d) ([IO]) ([ES]) (\\d{4}) ([GB]) (.)(\\d) (\\d\\d/\\d\\d \\d\\d:\\d\\d [AP]M) (.{8,15})(.*)");
        Matcher matcher = myPattern.matcher(myData);

        if(matcher.find()){

            myLine = Integer.parseInt(matcher.group(1));
            myType = matcher.group(2);

            if(myType.equals("I")||myType.equals("O")){

                myIndicator = matcher.group(3);

                // Unused in this app but available for other custom apps
                myDuration = matcher.group(4);
                myCheckSum = matcher.group(5);
                myRings = matcher.group(6);
                //------------------------------------------------------

                myDateTime = matcher.group(8);
                myNumber = matcher.group(9);
                myName = matcher.group(10);

            }

        }

        // Check to see if call information is from a DETAILED record
        Pattern myPatternDetailed = Pattern.compile(".*(\\d\\d) ([NFR]) {13}(\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d)");
        Matcher matcherDetailed = myPatternDetailed.matcher(myData);

        if(matcherDetailed.find()){

            myLine = Integer.parseInt(matcherDetailed.group(1));
            myType = matcherDetailed.group(2);

            if(myType.equals("N")||myType.equals("F")||myType.equals("R")){
                myDateTime = matcherDetailed.group(3);
            }

        }

        // Combine type and indicator to allow for 'findCommandValue()' function
        command = myType + myIndicator;

        // Get unique value for commands
        int commandUnique = findCommandValue(command);

        // Store call information and if app is in foreground show changes
        if(!visible){

            // Application is NOT in foreground so remember all activity
            // for later display when app is resumed.
            switch(commandUnique){

                case 1: // 'R' for Ring

                    // Incoming Call ------------------
                    // Remember all call information
                    memPhoneStatus[myLine] = imgRing;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    break;

                case 2: // 'IS' for Inbound Start

                    // Ring answered ---------------------

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOffHook;
                    memCallPanels[myLine] = lightestGreen;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Send notification to lock screen
                    //sendNotifications(true,myLine,myName,myNumber);

                    break;

                case 3: // 'F' for Off Hook

                    // Phone pickup --------------------

                    /// Remember needed call information
                    memPhoneStatus[myLine] = imgOffHook;

                    break;

                case 4: // 'N' for On Hook

                    // Remember needed call information
                    memCallPanels[myLine] = lightGrey;
                    memPhoneStatus[myLine] = imgOnHook;

                    break;

                case 5: // 'IE' for Inbound End

                    // Remember all call information
                    memCallPanels[myLine] = lightGrey;
                    memPhoneStatus[myLine] = imgOnHook;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Send notification to lock screen
                    //sendNotifications(false,myLine,myName,myNumber);

                    break;

                case 6: // 'OS' for Outbound Start

                    // Outgoing Call -------------------

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOffHook;
                    memCallPanels[myLine] = lightBlue;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    break;

                case 7: // 'OE' for Outbound End

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOnHook;
                    memCallPanels[myLine] = lightGrey;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    break;
            }

        }else{

            // App. IS in foreground so display all changes
            switch(commandUnique){

                case 1: // 'R' for Ring

                    // Remember all call information
                    memPhoneStatus[myLine] = imgRing;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Incoming Call ------------------

                    // Change picture of phone to ringing
                    callPhonePics[myLine].setImageResource(imgRing);

                    // Light up panel green for incoming call
                    callPanels[myLine].setBackgroundColor(lightGreen);

                    // Show time then clear name & number
                    callTimes[myLine].setText(myDateTime);
                    callNumbers[myLine].setText(myNumber);
                    callNames[myLine].setText(myName);

                    break;

                case 2: // 'IS' for Inbound Start

                    // Ring answered ---------------------

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOffHook;
                    memCallPanels[myLine] = lightestGreen;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Light-up panel green for incoming call
                    callPanels[myLine].setBackgroundColor(lightestGreen);

                    // Show time on panel
                    callTimes[myLine].setText(myDateTime);

                    // Show name and number on panel
                    callNumbers[myLine].setText(myNumber);
                    callNames[myLine].setText(myName);

                    // Send notification about new call
                    //sendNotifications(true,myLine,myName,myNumber);

                    break;

                case 3: // 'F' for Off Hook

                    // Phone pickup --------------------

                    // Remember needed call information
                    memPhoneStatus[myLine] = imgOffHook;

                    // Change picture of phone to off hook
                    callPhonePics[myLine].setImageResource(imgOffHook);

                    break;

                case 4: // 'N' for On Hook

                    // Remember needed call information
                    memCallPanels[myLine] = lightGrey;
                    memPhoneStatus[myLine] = imgOnHook;

                    // Phone hangup
                    callPanels[myLine].setBackgroundColor(lightGrey);

                    // Change picture of phone to not-ringing
                    callPhonePics[myLine].setImageResource(imgOnHook);

                    break;

                case 5: // 'IE' for Inbound End

                    // Remember all call information
                    memCallPanels[myLine] = lightGrey;
                    memPhoneStatus[myLine] = imgOnHook;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Phone hangup
                    callPanels[myLine].setBackgroundColor(lightGrey);

                    // Change picture of phone to not-ringing
                    callPhonePics[myLine].setImageResource(imgOnHook);

                    // Show time on panel
                    callTimes[myLine].setText(myDateTime);

                    // Show name and number on panel
                    callNumbers[myLine].setText(myNumber);
                    callNames[myLine].setText(myName);

                    //sendNotifications(false,myLine,myName,myNumber);

                    break;
                case 6: // 'OS' for Outbound Start

                    // Outgoing Call -------------------

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOffHook;
                    memCallPanels[myLine] = lightBlue;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Change picture of phone to off hook
                    callPhonePics[myLine].setImageResource(imgOffHook);

                    callPanels[myLine].setBackgroundColor(lightBlue);

                    // Show time on panel
                    callTimes[myLine].setText(myDateTime);

                    // Show name and number on panel
                    callNumbers[myLine].setText(myNumber);
                    callNames[myLine].setText(myName);

                    break;

                case 7: // 'OE' for Outbound End

                    // Remember all call information
                    memPhoneStatus[myLine] = imgOnHook;
                    memCallPanels[myLine] = lightGrey;
                    memCallTimes[myLine] = myDateTime;
                    memCallNumbers[myLine] = myNumber;
                    memCallNames[myLine] = myName;

                    // Phone hangup
                    callPanels[myLine].setBackgroundColor(lightGrey);

                    // Change picture of phone to not-ringing
                    callPhonePics[myLine].setImageResource(imgOnHook);

                    break;
            }

        }

    }

    private int findCommandValue(String str){

        // Since the SWITCH command does not allow for strings
        // we return the correct INT value by looking at the string
        if(str.equals("R"))return 1;
        if(str.equals("IS"))return 2;
        if(str.equals("F"))return 3;
        if(str.equals("N"))return 4;
        if(str.equals("IE"))return 5;
        if(str.equals("OS"))return 6;
        if(str.equals("OE"))return 7;

        return 0;

    }

    // -------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Associations
        lbStatus = (TextView)findViewById(R.id.lbStatus);

        callPhonePics[1] = (ImageView)findViewById(R.id.picLine1);
        callPhonePics[2] = (ImageView)findViewById(R.id.picLine2);
        callPhonePics[3] = (ImageView)findViewById(R.id.picLine3);
        callPhonePics[4] = (ImageView)findViewById(R.id.picLine4);

        callTimes[1] = (TextView)findViewById(R.id.lbTime1);
        callTimes[2] = (TextView)findViewById(R.id.lbTime1);
        callTimes[3] = (TextView)findViewById(R.id.lbTime1);
        callTimes[4] = (TextView)findViewById(R.id.lbTime1);

        callNumbers[1] = (TextView)findViewById(R.id.lbNumber1);
        callNumbers[2] = (TextView)findViewById(R.id.lbNumber1);
        callNumbers[3] = (TextView)findViewById(R.id.lbNumber1);
        callNumbers[4] = (TextView)findViewById(R.id.lbNumber1);

        callNames[1] = (TextView)findViewById(R.id.lbName1);
        callNames[2] = (TextView)findViewById(R.id.lbName2);
        callNames[3] = (TextView)findViewById(R.id.lbName3);
        callNames[4] = (TextView)findViewById(R.id.lbName4);

        //callPanels[1] = (TableRow)findViewById(R.id.panLine1);
        //callPanels[2] = (TableRow)findViewById(R.id.panLine2);
        //callPanels[3] = (TableRow)findViewById(R.id.panLine3);
        //callPanels[4] = (TableRow)findViewById(R.id.panLine4);

        // Image references
        imgRing = getResources().getIdentifier("phonering" , "mipmap", getPackageName());
        imgOffHook = getResources().getIdentifier("phoneoffhook" , "mipmap", getPackageName());
        imgOnHook = getResources().getIdentifier("phoneonhook" , "mipmap", getPackageName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
