package com.senerunosoft.ironbuff.MainMenuFragment;

import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.MessageViewAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentMessageUserBinding;
import com.senerunosoft.ironbuff.table.MessageTable;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.*;


public class MessageUserFragment extends Fragment {

    private static final String COLLECTION_NAME_MESSAGE = "messageTable";
    FragmentMessageUserBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Calendar calendar;
    MessageTable message;
    MessageViewAdapter adapter;
    private String CurrentUserDocId, GetterUserDocId;
    List<MessageTable> getMessageList, setMessageList, messageList;


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

        CurrentUserDocId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        assert getArguments() != null;
        GetterUserDocId = getArguments().getString("userDocID");

        sendMessage();
        getDataToFirebase();
        newMessageListener();
        listMessage();


    }

    private void getDataToFirebase() {
        firestore.collection(COLLECTION_NAME_MESSAGE).document(CurrentUserDocId).collection(GetterUserDocId).orderBy("messageDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    messageList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        MessageTable message = new MessageTable();
                        setNewMessage(doc, message);
                        message.setViewType(1);
                        Log.d(getTag(), "messageText: " + doc.get("message"));
//                        Log.d(getTag(), "messageDate: " + doc.get("messageDate"));
                        if (!messageList.contains(message)) {
                            messageList.add(message);
                        }
                    }
                    firestore.collection(COLLECTION_NAME_MESSAGE).document(GetterUserDocId).collection(CurrentUserDocId).orderBy("messageDate", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    MessageTable message = new MessageTable();
                                    setNewMessage(doc, message);
                                    message.setViewType(2);
                                    Log.d(getTag(), "messageText: " + doc.get("message"));
//                                    Log.d(getTag(), "messageDate: " + doc.getDate("messageDate"));
                                    if (!messageList.contains(message)) {
                                        messageList.add(message);
                                        listMessage();
                                    }
                                }
                            }
                        }
                    });

                }
            }
        });

    }


    private void setNewMessage(DocumentSnapshot doc, MessageTable message) {

        message.setMessage(doc.get("message").toString());
        message.setMessageDate((Date) doc.getDate("messageDate"));
        message.setMessageClock(doc.getDate("messageDate").getHours() + ":" + doc.getDate("messageDate").getMinutes());
        message.setSendMessageByUser("sendMessageByUser");
        message.setGetMessageByUser("getMessageByUser");
    }

    private void listMessage() {

        adapter = new MessageViewAdapter(getContext(), messageList);
        binding.chatMessage.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setStackFromEnd(true);
        binding.chatMessage.setLayoutManager(manager);
    }

    private void newMessageListener() {
        firestore.collection(COLLECTION_NAME_MESSAGE).document(GetterUserDocId).collection(CurrentUserDocId).orderBy("messageDate", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        MessageTable message = new MessageTable();
                        setNewMessage(doc, message);
                        message.setViewType(2);
                        messageList.add(message);
                        listMessage();
                        break;
                    }

                }

            }
        });


    }

    private void sendMessage() {
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                firestore.collection(COLLECTION_NAME_MESSAGE).document()
                calendar = Calendar.getInstance();
                message = new MessageTable();
                message.setMessage(binding.messageText.getText().toString());
                message.setSendMessageByUser(auth.getCurrentUser().getUid());
                message.setMessageClock(calendar.getTime().getHours()+ ":" + calendar.getTime().getMinutes());
                message.setGetMessageByUser(GetterUserDocId);
                message.setMessageDate(calendar.getTime());
                message.setViewType(1);
                messageList.add(message);
                listMessage();
                Map map = new HashMap();
                map.put("message", message.getMessage());
                map.put("sendMessageByUser", message.getSendMessageByUser());
                map.put("getMessageByUser", message.getGetMessageByUser());
                map.put("messageDate", message.getMessageDate());

                firestore.collection(COLLECTION_NAME_MESSAGE).document(CurrentUserDocId).collection(GetterUserDocId).document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        binding.messageText.setText("");
                    }
                });


            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}