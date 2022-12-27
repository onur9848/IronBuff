package com.senerunosoft.ironbuff.MainMenuFragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.MessageViewAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.activity.MainMenuActivity;
import com.senerunosoft.ironbuff.databinding.FragmentMessageUserBinding;
import com.senerunosoft.ironbuff.table.MessageListTable;
import com.senerunosoft.ironbuff.table.MessageTable;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static androidx.core.content.ContextCompat.getSystemService;


public class MessageUserFragment extends Fragment {

    private static final String COLLECTION_NAME_MESSAGE = "messageTable";
    private static final String COLLECTION_NAME_MESSAGE_BOX = "MessageBox";
    private static final String COLLECTION_MESSAGE = "Message";
    FragmentMessageUserBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    MessageTable message;
    private String CurrentUserDocId, GetterMessageDocId;
    List<MessageTable> messageList;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        messageList = new ArrayList<>();
        CurrentUserDocId = auth.getCurrentUser().getUid();
        context = getContext();

        assert getArguments() != null;
        GetterMessageDocId = getArguments().getString("messageDocID");
        String label = getArguments().getString("userName");
        ((MainMenuActivity) getActivity()).getSupportActionBar().setTitle(label);


        listMessageV2();
        sendMessageV2();


    }

    private void sendMessageV2() {
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.messageText.getText().toString().isEmpty()) {

                    SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                    Date now = Date.from(Instant.now());


                    message = new MessageTable();
                    message.setMessage(binding.messageText.getText().toString());
                    message.setSendMessageByUser(auth.getCurrentUser().getUid());
                    message.setMessageClock(timeformat.format(now));
                    message.setMessageDate(now);
                    message.setViewType(0);
                    binding.messageText.setText("");

                    setMessageBoxDetail(message);

                    firestore.collection(COLLECTION_NAME_MESSAGE_BOX).document(GetterMessageDocId).collection(COLLECTION_MESSAGE).add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
//                        Toast.makeText(getContext(), "send message", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void listMessageV2() {

        firestore.collection(COLLECTION_NAME_MESSAGE_BOX).document(GetterMessageDocId).collection(COLLECTION_MESSAGE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    messageList = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        MessageTable table = doc.toObject(MessageTable.class);

                        if (table.getSendMessageByUser().equals(auth.getCurrentUser().getUid())) {
                            table.setViewType(1);
                        } else {
                            table.setViewType(2);
                        }
                        messageList.add(table);
                    }
                    if (messageList != null) {

                        MessageViewAdapter messageAdapter = new MessageViewAdapter(getContext(), messageList);
                        binding.chatMessage.setAdapter(messageAdapter);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        manager.setOrientation(RecyclerView.VERTICAL);
                        manager.setStackFromEnd(true);
                        binding.chatMessage.setLayoutManager(manager);
                    }
                }
            }
        });

    }


    private void setMessageBoxDetail(MessageTable message) {
        firestore.collection(COLLECTION_NAME_MESSAGE_BOX).document(GetterMessageDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                MessageListTable listTable = task.getResult().toObject(MessageListTable.class);
                listTable.setLastMessage(message.getMessage());
                listTable.setLastMessageTime(message.getMessageDate());

                firestore.collection(COLLECTION_NAME_MESSAGE_BOX).document(GetterMessageDocId).set(listTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                    }
                });
            }
        });

    }
//    private void messageTextControl() { // Textbox Boş olduğunda mesaj gönderme butonunu devre dışı bırakılması
//        binding.messageText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0) {
//                    binding.sendMessageButton.setEnabled(true);
//                } else binding.sendMessageButton.setEnabled(false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }
//
//    private void getDataToFirebase() {
//        firestore.collection(COLLECTION_NAME_MESSAGE).document(CurrentUserDocId).collection(GetterUserDocId).orderBy("messageDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    messageList = new ArrayList<>();
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        MessageTable message = new MessageTable();
//                        setNewMessage(doc, message);
//                        message.setViewType(1);
//                        Log.d(getTag(), "messageText: " + doc.get("message"));
////                        Log.d(getTag(), "messageDate: " + doc.get("messageDate"));
//                        if (!messageList.contains(message)) {
//                            messageList.add(message);
//                        }
//                    }
//                    firestore.collection(COLLECTION_NAME_MESSAGE).document(GetterUserDocId).collection(CurrentUserDocId).orderBy("messageDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot doc : task.getResult()) {
//                                    MessageTable message = new MessageTable();
//                                    setNewMessage(doc, message);
//                                    message.setViewType(2);
//                                    Log.d(getTag(), "messageText: " + doc.get("message"));
////                                    Log.d(getTag(), "messageDate: " + doc.getDate("messageDate"));
//                                    if (!messageList.contains(message)) {
//                                        messageList.add(message);
//                                        listMessage();
//                                    }
//                                }
//                            }
//                        }
//                    });
//
//                }
//            }
//        });
//
//    }
//
//
//    private void setNewMessage(DocumentSnapshot doc, MessageTable message) {
//
//        message.setMessage(doc.get("message").toString());
//        message.setMessageDate((Date) doc.getDate("messageDate"));
//        message.setMessageClock(doc.getDate("messageDate").getHours() + ":" + doc.getDate("messageDate").getMinutes());
//        message.setSendMessageByUser("sendMessageByUser");
////        message.setGetMessageByUser("getMessageByUser");
//    }
//
//    private void listMessage() {
//
//        adapter = new MessageViewAdapter(getContext(), messageList);
//        binding.chatMessage.setAdapter(adapter);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(RecyclerView.VERTICAL);
//        manager.setStackFromEnd(true);
//        binding.chatMessage.setLayoutManager(manager);
//    }
//
//    private void newMessageListener() {
//        firestore.collection(COLLECTION_NAME_MESSAGE).document(GetterUserDocId).collection(CurrentUserDocId).orderBy("messageDate", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//                if (error == null) {
//                    for (DocumentSnapshot doc : value.getDocuments()) {
//                        MessageTable message = new MessageTable();
//                        setNewMessage(doc, message);
//                        message.setViewType(2);
//
//
//                        messageList.add(message);
//                        listMessage();
//
//                        break;
//                    }
//
//                }
//
//            }
//        });
//
//
//    }
//
//    private void sendMessage() {
//        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                calendar = Calendar.getInstance();
//                message = new MessageTable();
//                message.setMessage(binding.messageText.getText().toString());
//                binding.messageText.setText("");
//                message.setSendMessageByUser(auth.getCurrentUser().getUid());
//                message.setMessageClock(calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes());
////                message.setGetMessageByUser(GetterUserDocId);
//                message.setMessageDate(calendar.getTime());
//                message.setViewType(1);
//                messageList.add(message);
//                listMessage();
//                Map map = new HashMap();
//                map.put("message", message.getMessage());
//                map.put("sendMessageByUser", message.getSendMessageByUser());
////                map.put("getMessageByUser", message.getGetMessageByUser());
//                map.put("messageDate", message.getMessageDate());
//
//                firestore.collection(COLLECTION_NAME_MESSAGE).document(CurrentUserDocId).collection(GetterUserDocId).document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//
//                    }
//                });
//
//
//            }
//        });
//
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}