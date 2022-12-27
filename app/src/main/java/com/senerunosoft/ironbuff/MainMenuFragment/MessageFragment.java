package com.senerunosoft.ironbuff.MainMenuFragment;

import android.content.Context;
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
import com.senerunosoft.ironbuff.MainMenuFragment.adapter.MessageViewAdapter;
import com.senerunosoft.ironbuff.databinding.FragmentMessageBinding;
import com.senerunosoft.ironbuff.table.MessageListTable;
import com.senerunosoft.ironbuff.table.MessageTable;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class MessageFragment extends Fragment {

    private static final String COLLECTION_USER_TABLE = "userTable";
    private static final String COLLECTION_MESSAGE_TABLE = "MessageBox";

    FragmentMessageBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Context context;
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
        context = getContext();

        fabButton();
        getMessageList();


    }

    private void getMessageList() {
        firestore.collection(COLLECTION_MESSAGE_TABLE).whereArrayContains("users", auth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    messageListTables = value.toObjects(MessageListTable.class);
                    setAdapter();

                }
            }
        });


    }

    private void setAdapter() {
        if (binding != null) {
            MessageListAdapter adapter = new MessageListAdapter(context, messageListTables, auth.getUid());
            binding.messageUserList.setAdapter(adapter);


            binding.messageUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MessageListTable message = messageListTables.get(i);
                    String username;
                    if (message.getUsers().get(0).equals(auth.getUid())) {
                        username = message.getUserName().get(1);
                    }
                    else {
                        username = message.getUserName().get(0);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("messageDocID", message.getUuid());
                    bundle.putString("userName", username);
                    NavDirections directions = MessageFragmentDirections.gotoSendMessageUsers();
                    Navigation.findNavController(view).navigate(directions.getActionId(),bundle);
                }
            });
        }
    }


    private void fabButton() {
        binding.fabAddMessageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections directions = MessageFragmentDirections.gotoMessageUserList();
                Navigation.findNavController(view).navigate(directions);
                onDetach();
            }
        });
    }


    @Override

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}