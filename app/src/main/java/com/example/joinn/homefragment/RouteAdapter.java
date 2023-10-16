package com.example.joinn.homefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joinn.homefragment.Route;
import com.example.joinn.R;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;
    private Context context;



    public RouteAdapter(Context context, List<Route> routes) {
        this.context = context;
        this.routes = routes;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.startTextView.setText(route.getStartAddress());
        holder.endTextView.setText(route.getEndAddress());
        holder.nicknameTextView.setText(route.getNickname());
        holder.spotTextView.setText("직위: " + route.getSpottxt());
        holder.levelTextView.setText("레벨: " + route.getLevel());


    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView startTextView;
        TextView endTextView;

        TextView nicknameTextView;
        TextView spotTextView;

        ImageView imageView;
        TextView levelTextView;

        Button submitBtn;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            startTextView = itemView.findViewById(R.id.route_start);
            endTextView = itemView.findViewById(R.id.route_end);
            nicknameTextView = itemView.findViewById(R.id.NickNameText);
            spotTextView = itemView.findViewById(R.id.SpotText);

            levelTextView = itemView.findViewById(R.id.LevelText);

            imageView = itemView.findViewById(R.id.ImageText);

            submitBtn = itemView.findViewById(R.id.SummitBtn);

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                }
            });

            Glide.with(context).load(Route.getImageURL()).into(imageView);
        }
    }
}
