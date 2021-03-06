package com.reflect.pofke.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    public String UUID = "";
    public void test (View view) {
        System.out.print("button clicked1");
        //gets text from textbox
        final EditText text = findViewById(R.id.edit);
        Button button = findViewById(                                                                                  R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print(" button clicked 2");
                String sentmessage = text.getText().toString();
                //----> publish and update
                Send(sentmessage);
            } });}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<Collection> THECOLLECTION = new ArrayList<>();
        String[] categories1 = new String[]{"work", "job", "Interview", "1"};
        String key1 = "key";
        String[] categories2 = new String[]{"tests", "programming", "debug", "2"};
        String key2 = "key2";
        String[] categories3 = new String[]{"work", "job", "Interview", "cooking", "3"};
        String key3 = "key3";
        String[] categories4 = new String[]{"tests", "programming", "debug", "open", "position", "hiring", "4"};
        String key4 = "key4";

        Collection test1 = new Collection(categories1, key1);
        THECOLLECTION.add(test1);
        Collection test2 = new Collection(categories2, key2);
        THECOLLECTION.add(test2);
        Collection test3 = new Collection(categories3, key3);
        THECOLLECTION.add(test3);
        Collection test4 = new Collection(categories4, key4);
        THECOLLECTION.add(test4);

        // TODO search, using Database Collection

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter Nickname:");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                UUID = input.getText().toString();
                System.out.println(UUID + "MID");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

        setContentView(R.layout.activity_main);
        recyclerView = this.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();

        adapter = new MyAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-ee625e62-ecd5-11e8-8495-720743810c32");
        System.out.println("subscribe key works");
        pnConfiguration.setPublishKey("pub-c-5bbe6b93-dba4-481e-9914-72dc8b47c427");
        System.out.println("publish key works");
        pnConfiguration.setSecure(false);
        System.out.println("set secure works");
        pnConfiguration.setUuid(UUID);
        PubNub pubnub = new PubNub(pnConfiguration);
        System.out.println("pub nub declared");

        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus status) {
                System.out.println("Listener declared, switch initialized");
                switch (status.getOperation()) {
                    case PNSubscribeOperation:
                        //subbed to channel
                    case PNUnsubscribeOperation:
                        switch (status.getCategory()) {
                            case PNConnectedCategory:
                                // if connected
                            case PNReconnectedCategory:
                                //fail but recconect
                            case PNDisconnectedCategory:
                                //disconnect expected
                            case PNUnexpectedDisconnectCategory:
                                // internet problem
                            case PNAccessDeniedCategory:
                                //not allowed to login ( wrong key/ wrong acc)
                            default:

                        }

                    case PNHeartbeatOperation:

                        if (status.isError()) {
                            System.out.print("Heartbeat dead");
                        } else {
                           System.out.print("Heartbeat ok");
                        }
                    default: {
                        System.out.print("Uknown error");
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                String messagePublisher = message.getPublisher();
                System.out.println("Message publisher: " + messagePublisher);
                System.out.println("Message Payload: " + message.getMessage());
                System.out.println("Message Subscription: " + message.getSubscription());
                System.out.println("Message Channel: " + message.getChannel());
                System.out.println("Message timetoken: " + message.getTimetoken());
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        SubscribeCallback subscribeCallback = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    System.out.print("rekondisconn");
                    pubnub.reconnect();
                } else if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                    System.out.print("rekontimeout");//
                    pubnub.reconnect();
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        };

        pubnub.history()
                .channel(pubnub.getSubscribedChannels().toString())
                .count(100)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {



                        for (int i = 0; i< result.getMessages().size(); i++)
                        {
                            //split sender nickname and the message
                            String[] tests = new String[1];
                            tests = result.getMessages().get(i).getEntry().toString().split(";");
                            ListItem listItemss = new ListItem(tests[0], tests[1]);
                            listItems.add(listItemss);


                        }
                        adapter.notifyDataSetChanged();

                    }
                });
        pubnub.addListener(subscribeCallback);

    }
    public void Send(String sentmessage)
    {
        //connect again
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-ee625e62-ecd5-11e8-8495-720743810c32");
        pnConfiguration.setPublishKey("pub-c-5bbe6b93-dba4-481e-9914-72dc8b47c427");
        pnConfiguration.setUuid(UUID);
        pnConfiguration.setSecure(false);



        final PubNub pubnub= new PubNub(pnConfiguration);

        System.out.println(sentmessage);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, sentmessage, duration);
        toast.show();
        sentmessage = UUID+ ";" + sentmessage;


        pubnub.publish()
                .message(sentmessage)
                .channel(pubnub.getSubscribedChannels().toString())
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    // if sent succes
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // update history
                        pubnub.history()
                                .channel(pubnub.getSubscribedChannels().toString())
                                .count(100)
                                .async(new PNCallback<PNHistoryResult>() {
                                    @Override
                                    // if there is a result ( history downloaded)
                                    public void onResponse(PNHistoryResult result, PNStatus status) {


                                        // One long string from all of the messages
                                        listItems.clear();
                                        for (int i = 0; i< result.getMessages().size(); i++)
                                        {
                                            String[] tests = new String[1];
                                            tests = result.getMessages().get(i).getEntry().toString().split(";");
                                            System.out.println("TESTS: ");
                                            System.out.println(tests[0] +"0");
                                            System.out.println(tests[1] +"1");
                                            ListItem listItemss = new ListItem(tests[0], tests[1]);
                                            listItems.add(listItemss);
                                        }
                                        adapter.notifyDataSetChanged();



                                    }
                                });
                    }
                });
    }}


