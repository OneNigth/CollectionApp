package com.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.vf.MyApplication;
import com.android.vf.R;
import com.android.view.XListView;
import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.manager.CalendarManager;

import org.joda.time.LocalDate;

public class ThirdFragment extends Fragment implements CollapseCalendarView.OnDateSelect {

    View view;
    CollapseCalendarView mCalendarView;
    XListView xListView;
    Context context;
    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_third, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        xListView = (XListView) view.findViewById(R.id.third_list);
        mCalendarView = (CollapseCalendarView) view.findViewById(R.id.calendar_view);

        context = MyApplication.getContext();

        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));
        mCalendarView.init(manager);
        mCalendarView.setListener(this);
    }

    @Override
    public void onDateSelected(LocalDate date) {
        Toast.makeText(context,"选择的日期是"+date,Toast.LENGTH_SHORT).show();
    }
}
