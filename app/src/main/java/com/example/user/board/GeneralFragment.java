package com.example.user.board;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GeneralFragment extends Fragment {
    private View Generalview;
    private RecyclerView myGeneralList;

    private DatabaseReference GeneralRef, UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public GeneralFragment(){
        // required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        Generalview = inflater.inflate(R.layout.activity_general_fragment,container,false);

        myGeneralList = (RecyclerView) Generalview.findViewById(R.id.general_post);
        myGeneralList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        GeneralRef = FirebaseDatabase.getInstance().getReference().child("News").child(currentUserID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return Generalview;
        }

        @Override
        public void onStart()
        {

            super.onStart();

            FirebaseRecyclerOptions <News> options =
                    new FirebaseRecyclerOptions.Builder<News>()
                    .setQuery(GeneralRef, News.class)
                    .build();

            FirebaseRecyclerAdapter<News, GeneralViewHolder> adapter =
                    new FirebaseRecyclerAdapter<News, GeneralViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final GeneralViewHolder holder, int position, @NonNull News model)
                        {
                            final String usersIDs = getRef(position).getKey();

                            UserRef.child(usersIDs).addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    if (dataSnapshot.hasChild("image"))
                                    {
                                        final String retImage = dataSnapshot.child("image").getValue().toString();
                                        Picasso.get().load(retImage).into(holder.contImage);
                                    }
                                    final String retName = dataSnapshot.child("name").getValue().toString();

                                    holder.posterName.setText(retName);
                                   
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @NonNull
                        @Override
                        public GeneralViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                        {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news, viewGroup, false);
                            return new GeneralViewHolder(view);
                        }
                    };
            myGeneralList.setAdapter(adapter);
            adapter.startListening();
        }

        public static class GeneralViewHolder extends RecyclerView.ViewHolder
        {
            ImageView contImage;
            TextView print, posterName,postDate,cont;

            public GeneralViewHolder(@NonNull View itemView)
            {
                super(itemView);

                contImage = itemView.findViewById(R.id.img_poster);
                print = itemView.findViewById(R.id.txt_content);
                posterName = itemView.findViewById(R.id.txt_fineprint);
                postDate = itemView.findViewById(R.id.txt_date);
                cont = itemView.findViewById(R.id.txt_content);
            }
        }
}