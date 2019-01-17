package com.wolfenterprisesllc.prisongourmet;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wolfenterprisesllc.prisongourmet.dummy.DummyContent.DummyItem;

import java.util.Objects;

public class ItemFragment extends Fragment {

    public RecyclerView recyclerView;
    private DBHandler dbHelper;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;
    RecyclerViewAdapter mAdapter;
    Button close;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHandler(getActivity());
        if (getArguments() != null) {
            int mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).setTitle("Your Favorites");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        close = view.findViewById(R.id.btnClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.myList2);
        mAdapter = new RecyclerViewAdapter(dbHelper.getAllRecipies());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()).getApplicationContext(), LinearLayoutManager.VERTICAL));
        }
        recyclerView.setAdapter(mAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(Objects.requireNonNull(getActivity()).getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Recipie.class);
                    intent.putExtra("recipieName", dbHelper.getAllRecipies().get(position).getRecipie());
                    intent.putExtra("starValue",true);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {
                }
            }));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DummyItem item);
    }
}

