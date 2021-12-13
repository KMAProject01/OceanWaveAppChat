package com.example.honahlappchat.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honahlappchat.Listener.Userlistener;
import com.example.honahlappchat.databinding.ItemUserBinding;
import com.example.honahlappchat.models.UsersM;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private  List<UsersM> users;
    private final Userlistener userlistener;
    private  List<UsersM> usersOld;

    public UserAdapter(List<UsersM> users, Userlistener userlistener) {
        this.users = users;
        this.userlistener = userlistener;
        this.usersOld = users;
    }


    // đây là các phương thức bắt buộc của adapter recycle view
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding itemUserBinding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new UserViewHolder(itemUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.SetUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }




    //tao view holder cho recycle view
    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemUserBinding binding;

        UserViewHolder(ItemUserBinding itemUserBinding){
            super(itemUserBinding.getRoot());
            binding = itemUserBinding;
        }

        void SetUserData(UsersM users){
            binding.TextName.setText(users.name);
            binding.textEmail.setText(users.email);
            binding.imageProfile.setImageBitmap(getImage(users.image));
            binding.getRoot().setOnClickListener(v -> userlistener.onUsetClicked(users));
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (search.isEmpty()){
                   users = usersOld;
                } else {
                    List<UsersM> list = new ArrayList<>();
                    for (UsersM usersM : usersOld){
                        if (usersM.name.toLowerCase().contains(search.toLowerCase())){
                            list.add(usersM);
                        }
                    }

                    users = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = users;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users = (List<UsersM>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    // de decode anh ve ding dang string cua bit map
    private Bitmap getImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }
}
