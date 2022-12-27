package com.senerunosoft.ironbuff.MainMenuFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.senerunosoft.ironbuff.AdminPage.Adapter.UserListAdapter;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentMessageUserListBinding;
import com.senerunosoft.ironbuff.table.MessageListTable;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class MessageUserListFragment extends Fragment {
    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_MESSAGE_TABLE = "MessageBox";
    FragmentMessageUserListBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    List<UserTable> userList;
    UserTable currentUser;
    List<MessageListTable> messageList;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageUserListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userList = new ArrayList<>();
        currentUser = new UserTable();
        context = getContext();

        getUserList();


    }

    private void selectedUser() {
        firestore.collection(COLLECTION_MESSAGE_TABLE).whereArrayContains("users", auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   messageList = task.getResult().toObjects(MessageListTable.class);
               }
               else {
                   messageList = new ArrayList<>();
               }

                binding.messageListUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        MessageListTable select = new MessageListTable();
                        UserTable user = userList.get(i);
                        for (MessageListTable table : messageList) {
                            if (table.getUsers().contains(user.getDocId())) {
                                select = table;
                            }
                        }
                        Bundle bundle = new Bundle();

                        if (select.getUuid() == null ) {
                            select.setLastMessage("");
                            select.setUuid(UUID.randomUUID().toString());
                            select.setLastMessageTime(Date.from(Instant.now()));

                            select.getUserImg().add(currentUser.getImageUrl());
                            select.getUserImg().add(user.getImageUrl());

                            select.getUserName().add(currentUser.getUserName());
                            select.getUserName().add(user.getUserName());

                            select.getUsers().add(currentUser.getDocId());
                            select.getUsers().add(user.getDocId());

                            firestore.collection(COLLECTION_MESSAGE_TABLE).document(select.getUuid()).set(select).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                }
                            });


                        }
                        bundle.putString("messageDocID",select.getUuid());
                        bundle.putString("userName",user.getUserName());

                        NavDirections directions = MessageUserListFragmentDirections.gotoMessageView();
                        Navigation.findNavController(view).navigate(directions.getActionId(),bundle);
                        onDetach();


                    }
                });


            }
        });


    }


    private void getUserList() {
        firestore.collection(COLLECTION_USER_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                userList = new ArrayList<>();
                List<UserTable> list = value.toObjects(UserTable.class);
                for (UserTable table : list) {
                    if (table.getDocId().equals(auth.getUid())) {
                        currentUser = table;
                        list.remove(table);
                        break;
                    }
                }
                for (UserTable table:list){
                    if (currentUser.isAdmin()){
                        userList.add(table);
                    }else{
                     if (table.isAdmin()){
                         userList.add(table);
                     }
                    }

                }


                if (userList != null) {
                    UserListAdapter adapter = new UserListAdapter(context, userList);
                    binding.messageListUser.setAdapter(adapter);
                    selectedUser();
                }
            }
        });
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}