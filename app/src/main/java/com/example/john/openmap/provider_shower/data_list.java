package com.example.john.openmap.provider_shower;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.john.openmap.R;
import com.example.john.openmap.UIconnector.go_to_gallery;
import com.example.john.openmap.log_out;
import com.example.john.openmap.provider_editor.image_edit;
import com.example.john.openmap.provider_editor.story_edit;
import com.example.john.openmap.provider_editor.title_edit;
import com.example.john.openmap.provider_editor.title_image_edit;

import java.io.InputStream;
import java.util.ArrayList;

public class data_list extends RecyclerView.Adapter {

    private ArrayList<Data_from_server> data_from_servers;

    public static class at_provider_Image_view extends RecyclerView.ViewHolder {
        ImageView ImageView;

        at_provider_Image_view(View itemView) {
            super(itemView);
            this.ImageView = itemView.findViewById(R.id.imageView);
        }

    }

    public static class at_provider_Story_view extends RecyclerView.ViewHolder{
        TextView textView;

        at_provider_Story_view(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
        }
    }

    public static class owners_info_window extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        Button button;
        owners_info_window(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.owners_head_image);
            this.textView = itemView.findViewById(R.id.owners_head_title);
            this.button = itemView.findViewById(R.id.head_info_update);
        }
    }

    public static class owners_info extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;

        owners_info(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.user_name);
            this.email = itemView.findViewById(R.id.user_email);
        }
    }

    public static class logout extends RecyclerView.ViewHolder{
        Button logout;
        logout(View itemView) {
            super(itemView);
            this.logout = itemView.findViewById(R.id.logout);
        }
    }

    public data_list(ArrayList<Data_from_server> data){
        this.data_from_servers = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch(viewType){
            case Data_from_server.TITLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_story_view, parent, false);
                return new at_provider_Story_view(view);
            case Data_from_server.TITLE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_story_image_view, parent,false);
                return new at_provider_Image_view(view);
            case Data_from_server.STORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_story_view, parent, false);
                return new at_provider_Story_view(view);
            case Data_from_server.IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_story_image_view, parent,false);
                return new at_provider_Image_view(view);
            case Data_from_server.OWNER_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owners_info, parent,false);
                return new owners_info(view);
            case Data_from_server.HEAD_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.make_info_window,parent,false);
                return new owners_info_window(view);
            case Data_from_server.LOG_OUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logout_button,parent,false);
                return new logout(view);
            }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (data_from_servers.get(position).getContext()){
            case 0:
                return Data_from_server.TITLE;
            case 1:
                return Data_from_server.TITLE_IMAGE;
            case 2:
                return Data_from_server.STORY;
            case 3:
                return Data_from_server.IMAGE;
            case 4:
                return Data_from_server.OWNER_INFO;
            case 5:
                return Data_from_server.HEAD_INFO;
            case 6:
                return Data_from_server.LOG_OUT;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Data_from_server data_from_server = data_from_servers.get(position);
            switch (data_from_server.getContext()) {
                case Data_from_server.TITLE:
                    ((at_provider_Story_view) holder).textView.setText(data_from_server.get_Story());
                    ((at_provider_Story_view) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), title_edit.class);
                            v.getContext().startActivity(intent);
                        }
                    });
                    break;

                case Data_from_server.TITLE_IMAGE:
                    new DownloadImageFromInternet(((at_provider_Image_view)holder).ImageView).execute(data_from_server.get_Image());
                    ((at_provider_Image_view) holder).ImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), title_image_edit.class);
                            v.getContext().startActivity(intent);
                        }
                    });
                    break;

                case Data_from_server.STORY:
                    ((at_provider_Story_view) holder).textView.setText(data_from_server.get_Story());
                    ((at_provider_Story_view) holder).textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), story_edit.class);
                            v.getContext().startActivity(intent);
                        }
                    });
                    break;

                case Data_from_server.IMAGE:
                    new DownloadImageFromInternet(((at_provider_Image_view)holder).ImageView).execute(data_from_server.get_Image());
                    ((at_provider_Image_view) holder).ImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), image_edit.class);
                            v.getContext().startActivity(intent);
                        }
                    });
                    break;

                case Data_from_server.OWNER_INFO:
                    ((owners_info)holder).name.setText(data_from_server.getOwner_name());
                    ((owners_info)holder).email.setText(data_from_server.getOwner_info());
                    break;

                case Data_from_server.HEAD_INFO:
                    ((owners_info_window)holder).textView.setText(data_from_server.get_Story());
                    new DownloadImageFromInternet(((owners_info_window)holder).imageView).execute(data_from_server.get_Image());
                    ((owners_info_window)holder).button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(),go_to_gallery.class);
                            v.getContext().startActivity(intent);
                        }
                    });
                    break;

                case Data_from_server.LOG_OUT:
                    ((logout)holder).logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), log_out.class);
                            v.getContext().startActivity(intent);
                        }
                    });
            }
    }

    @Override
    public int getItemCount() {
        return data_from_servers.size();
    }

    private static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView imageView;

        DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
