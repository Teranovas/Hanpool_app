package com.example.joinn.homefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.startTextView.setText("출발: " + route.getStart().getLatitude() + ", " + route.getStart().getLongitude());
        holder.endTextView.setText("도착: " + route.getEnd().getLatitude() + ", " + route.getEnd().getLongitude());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView startTextView;
        TextView endTextView;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            startTextView = itemView.findViewById(R.id.route_start);
            endTextView = itemView.findViewById(R.id.route_end);
        }
    }
}
