package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.MessageListAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentMessageBinding;
import com.senerunosoft.ironbuff.table.MessageListTable;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class MessageFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_MESSAGE_TABLE = "MessageBox";

    FragmentMessageBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    List<UserTable> userTables;
    List<MessageListTable> messageListTables;
    UserTable CurrentUser;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        CurrentUser = new UserTable();


        getUserList();
        getMessageDatabase();

    }

    private void getUserList() {
        firestore.collection(COLLECTION_USER_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    userTables = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        userTables.add(doc.toObject(UserTable.class));
                    }
                    if (userTables.size() > 0) {
                        listUsers();
                    }
                }
            }
        });
    }

    private void listUsers() {
        for (int i = 0; i < userTables.size(); i++) {
            if (userTables.get(i).getDocId().equals(auth.getUid())) {
                CurrentUser = userTables.get(i);
                userTables.remove(i);
            }
        }

        if (CurrentUser.isAdmin()) {
            for (int i = 0; i < userTables.size(); i++) {
                if (userTables.get(i).isAdmin())
                    userTables.remove(i);
                setAdapter();
            }
        } else {
            for (int i = 0; i < userTables.size(); i++) {
                if (!userTables.get(i).isAdmin())
                    userTables.remove(i);
                setAdapter();
            }
        }

    }

    private void setAdapter() {
        if (userTables != null) {
            MessageListAdapter adapter = new MessageListAdapter(getContext(), userTables);
            binding.messageUserList.setAdapter(adapter);
        }
    }


    private void getMessageDatabase() {
        firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection("messageList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    messageListTables = task.getResult().toObjects(MessageListTable.class);
                    gotoMessageScreenV2();
                }
            }
        });


    }

    private void gotoMessageScreenV2() {


        binding.messageUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isFind = false;
                MessageListTable selectUsers = new MessageListTable();
                for (MessageListTable table : messageListTables) {
                    if (table.getUsers().contains(userTables.get(i).getDocId())) {
//                        Toast.makeText(getContext(), "find", Toast.LENGTH_SHORT).show();
                        selectUsers = table;
                        isFind = true;
                    }
                }

                if (isFind) {

                    Bundle bundle = new Bundle();
                    bundle.putString("messageDocID", selectUsers.getUuid());
                    bundle.putString("userName", userTables.get(i).getUserName());
                    NavDirections directions = MessageFragmentDirections.gotoSendMessageUsers();
                    Navigation.findNavController(getView()).navigate(directions.getActionId(), bundle);


                } else {
                    MessageListTable messageListTable = new MessageListTable();
                    List<String> strings = new ArrayList<>();

                    strings.add(auth.getCurrentUser().getUid());
                    strings.add(userTables.get(i).getDocId());

                    messageListTable.setUuid(UUID.randomUUID().toString());
                    messageListTable.setUsers(strings);


                    firestore.collection(COLLECTION_USER_TABLE).document(auth.getCurrentUser().getUid()).collection("messageList").document(messageListTable.getUuid()).set(messageListTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    });
                    firestore.collection(COLLECTION_USER_TABLE).document(userTables.get(i).getDocId()).collection("messageList").document(messageListTable.getUuid()).set(messageListTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    });
                    firestore.collection(COLLECTION_MESSAGE_TABLE).document(messageListTable.getUuid()).set(messageListTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("messageDocID", messageListTable.getUuid());
                    bundle.putString("userName", userTables.get(i).getUserName());
                    NavDirections directions = MessageFragmentDirections.gotoSendMessageUsers();
                    Navigation.findNavController(getView()).navigate(directions.getActionId(), bundle);


                }
            }
        });

    }

    @Override

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}